package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.NoStockException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ClientOrderBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(co.code) FROM ClientOrder co WHERE co.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }
    public void create(String finalCostumerUsername, List<ProductOrderDTO> products) throws MyEntityNotFoundException, NoStockException {
        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, finalCostumerUsername);
        if (finalCostumer == null) {
            throw new MyEntityNotFoundException("Final Costumer with username: " + finalCostumerUsername + " not found");
        }

        try{
            ClientOrder clientOrder = new ClientOrder(finalCostumer);
            entityManager.persist(clientOrder);
            entityManager.flush();

            finalCostumer.addOrder(clientOrder);

            for (ProductOrderDTO product: products) {
                ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, product.getCode());
                List<Product> productsList = productCatalog.getProducts()
                        .stream()
                        .filter(prod -> prod.getClientOrder() == null)
                        .collect(Collectors.toList());

                if(productsList.size() >= product.getQuantity()){
                    for (int i = 0; i < product.getQuantity(); i++) {
                        productsList.get(i).setClientOrder(clientOrder);
                        clientOrder.addProduct(productsList.get(i));
                    }
                }else{
                    throw new NoStockException("Não há quantidade suficiente do produto " + productCatalog.getName() + ". Stock: " + productsList.size() + (productsList.size() > 1 ? " unidades." : "unidade."));
                }
            }
        }catch (ConstraintViolationException | NoStockException e){
            throw new NoStockException(e.getMessage());
        }

    }

    public List<ClientOrder> getAll(){
        return entityManager.createNamedQuery("getAllOrders", ClientOrder.class).getResultList();
    }
    
    public ClientOrder find (long code) throws MyEntityNotFoundException {
        if (!exists(code)) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " not found");
        }
        return entityManager.find(ClientOrder.class, code);
    }

    public ClientOrder findClientOrderWithProducts(long code) throws MyEntityNotFoundException {
        if (!exists(code)) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " not found");
        }
        ClientOrder clientOrder = entityManager.find(ClientOrder.class, code);
        Hibernate.initialize(clientOrder.getProducts());
        return clientOrder;
    }

    public void addProduct(long code, long productCode) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(code);
        Product product = entityManager.find(Product.class, productCode);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with code: " + productCode + " not found");
        }
        clientOrder.addProduct(product);
        product.setClientOrder(clientOrder);
    }

    public void removeProduct(long code, long productCode) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(code);
        Product product = entityManager.find(Product.class, productCode);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with code: " + productCode + " not found");
        }
        clientOrder.removeProduct(product);
        product.setClientOrder(null);
    }


    public void changeLogistic(long clientOrderCode, String logisticOperatorCode) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(clientOrderCode);
        LogisticOperator logisticOperator = entityManager.find(LogisticOperator.class, logisticOperatorCode);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("Logistic Operator with code: " + logisticOperatorCode + " not found");
        }
        if(clientOrder.getLogisticOperator() != null){
            logisticOperator.removeOrder(clientOrder);
        }
        clientOrder.setLogisticOperator(logisticOperator);
        logisticOperator.addOrder(clientOrder);
    }

    public void changeLocation(long clientOrderCode, String location) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(clientOrderCode);
        clientOrder.setLocation(location);
    }

    public void changeFinalCostumer(long clientOrderCode, String finalCostumerUsername) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(clientOrderCode);
        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, finalCostumerUsername);
        if (finalCostumer == null) {
            throw new MyEntityNotFoundException("Final Costumer with username: " + finalCostumerUsername + " not found");
        }
        if(clientOrder.getFinalCostumer() != null){
            finalCostumer.removeOrder(clientOrder);
        }
        clientOrder.setFinalCostumer(finalCostumer);
        finalCostumer.addOrder(clientOrder);
    }
}
