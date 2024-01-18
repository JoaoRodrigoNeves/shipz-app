package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
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
                    transportPackage.addClientOrder(clientOrder);
                    finalVolumeTotal -= transportPackage.getVolume();
                    i = 0;
                    entityManager.persist(transportPackage);
                }
                if (finalVolumeTotal <= 0) break;
            }

            entityManager.flush();
        } catch (ConstraintViolationException e){
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

    public void addProduct(long code, long productCode) throws MyEntityNotFoundException {
        Order clientOrder = find(code);
        Product product = entityManager.find(Product.class, productCode);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with code: " + productCode + " not found");
        }
        clientOrder.addProduct(product);
        product.setOrder(clientOrder);
    }

    public void removeProduct(long code, long productCode) throws MyEntityNotFoundException {
        Order order = find(code);
        Product product = entityManager.find(Product.class, productCode);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with code: " + productCode + " not found");
        }
        order.removeProduct(product);
        product.setOrder(null);
    }

    public Order getTransportPackages(long code) throws MyEntityNotFoundException {
        Order order = this.find(code);
        Hibernate.initialize(order.getTransportPackages());
        return order;
    }

    public void changeStatus(long code, String status) throws MyEntityNotFoundException {
        Order order = this.find(code);
        OrderStatus orderStatus = OrderStatus.fromString(status);
        order.setStatus(orderStatus);
        if (orderStatus == OrderStatus.STATUS_3)
            order.setDeliveredAt(LocalDateTime.now());
    }


    public void changeLocation(long code, String location) throws MyEntityNotFoundException {
        Order order = this.find(code);
        order.setLocation(location);
    }
}
