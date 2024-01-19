package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class OrderBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(co.code) FROM Order co WHERE co.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long) query.getSingleResult() > 0L;
    }

    public void create(String finalCostumerUsername, String logisticOperatorUsername, List<ProductOrderDTO> products, List<TransportPackageCatalog> listTransportPackageCatalogs) throws MyEntityNotFoundException, NoStockException, MyConstraintViolationException {
        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, finalCostumerUsername);
        if (finalCostumer == null) {
            throw new MyEntityNotFoundException("Final Costumer with username: " + finalCostumerUsername + " not found");
        }

        LogisticOperator logisticOperator = entityManager.find(LogisticOperator.class, logisticOperatorUsername);
        if (logisticOperator == null)
            throw new MyEntityNotFoundException("Logistic Operator with username: " + logisticOperatorUsername + " not found");

        try {
            Order clientOrder = new Order(finalCostumer, logisticOperator);

            long volumeTotal = 0;

            for (ProductOrderDTO product : products) {
                ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, product.getCode());
                List<Product> productsList = productCatalog.getProducts()
                        .stream()
                        .filter(prod -> prod.getOrder() == null)
                        .collect(Collectors.toList());
                if (productsList.size() >= product.getQuantity()) {
                    volumeTotal += productCatalog.getPrimaryPackageVolume() * product.getQuantity();
                    for (int i = 0; i < product.getQuantity(); i++) {
                        productsList.get(i).setOrder(clientOrder);
                        List<ProductPackage> productPackages = productsList.get(i).getProductPackages().stream().filter(productPackage -> productPackage.getType() == PackageType.PRIMARY).collect(Collectors.toList());
                        productPackages.get(0).getSensors().forEach(sensor -> sensor.setInUse(true));
                        clientOrder.addProduct(productsList.get(i));
                    }
                } else {
                    throw new NoStockException("Não há quantidade suficiente do produto " + productCatalog.getName() + ". Stock: " + productsList.size() + (productsList.size() > 1 ? " unidades." : "unidade."));
                }
            }
            finalCostumer.addOrder(clientOrder);
            entityManager.persist(clientOrder);


            long finalVolumeTotal = volumeTotal;
            for (int i = 0; i < listTransportPackageCatalogs.size(); i++) {
                if (listTransportPackageCatalogs.get(i).getVolume() > finalVolumeTotal || i == listTransportPackageCatalogs.size() - 1) {
                    TransportPackage transportPackage = new TransportPackage(PackageType.TRANSPORT, listTransportPackageCatalogs.get(i).getMaterial(), listTransportPackageCatalogs.get(i).getVolume(), listTransportPackageCatalogs.get(i));
                    clientOrder.addTransportPackage(transportPackage);
                    transportPackage.addOrder(clientOrder);
                    finalVolumeTotal -= transportPackage.getVolume();
                    i = 0;
                    entityManager.persist(transportPackage);
                }
                if (finalVolumeTotal <= 0) break;
            }

            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        } catch (NoStockException e) {
            throw new NoStockException(e.getMessage());
        }
    }

    public List<Order> getAll() {
        return entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
    }

    public Order find(long code) throws MyEntityNotFoundException {
        if (!exists(code)) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " not found");
        }
        return entityManager.find(Order.class, code);
    }

    public Order getProducts(long code) throws MyEntityNotFoundException {
        if (!exists(code)) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " not found");
        }
        Order clientOrder = entityManager.find(Order.class, code);
        Hibernate.initialize(clientOrder.getProducts());
        return clientOrder;
    }

    public Order getTransportPackages(long code) throws MyEntityNotFoundException {
        Order order = this.find(code);
        Hibernate.initialize(order.getTransportPackages());
        return order;
    }

    public void changeStatus(long code, String status) throws MyEntityNotFoundException, NotEnoughTransportPackageException {
        Order order = this.find(code);
        long transportPackagesVolume = 0;
        long productsVolume = 0;
        for (TransportPackage transportPackage: order.getTransportPackages()) {
            transportPackagesVolume += transportPackage.getVolume();
        }

        for (Product product: order.getProducts()) {
            productsVolume += product.getProductCatalog().getPrimaryPackageVolume();
        }
        if(productsVolume > transportPackagesVolume){
            throw new NotEnoughTransportPackageException("Not enough Transport Packages");
        }
        OrderStatus orderStatus = OrderStatus.fromString(status);
        order.setStatus(orderStatus);
        if (orderStatus == OrderStatus.STATUS_2) {
            order.setDeliveredAt(LocalDateTime.now());
            order.getTransportPackages().forEach(transportPackage -> transportPackage.getSensors().forEach(sensor -> sensor.setInUse(false)));
            order.getProducts().forEach(product -> product.getProductPackages().forEach(productPackage -> {
                if (productPackage.getType() == PackageType.PRIMARY) {
                    productPackage.getSensors().forEach(sensor -> sensor.setInUse(false));
                }
            }));
        }
        if (orderStatus == OrderStatus.STATUS_1){
            order.getTransportPackages().forEach(transportPackage -> transportPackage.getSensors().forEach(sensor -> sensor.setInUse(true)));
            order.getProducts().forEach(product -> product.getProductPackages().forEach(productPackage -> {
                if (productPackage.getType() == PackageType.PRIMARY) {
                    productPackage.getSensors().forEach(sensor -> sensor.setInUse(true));
                }
            }));
        }
    }


    public void changeLocation(long code, String location) throws MyEntityNotFoundException {
        Order order = this.find(code);
        order.setLocation(location);
    }
}
