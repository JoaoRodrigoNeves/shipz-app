package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.FinalCostumer;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

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
    public void create(String finalCostumerUsername, List<Long> products) throws MyEntityNotFoundException{
        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, finalCostumerUsername);
        if (finalCostumer == null) {
            throw new MyEntityNotFoundException("Final Costumer with username: " + finalCostumerUsername + " not found");
        }

        try{

            ClientOrder clientOrder = new ClientOrder(finalCostumer);
            entityManager.persist(clientOrder);
            entityManager.flush();

            finalCostumer.addOrder(clientOrder);

            for (Long productCode: products) {
                Product product = entityManager.find(Product.class, productCode);
                if(product != null){
                    clientOrder.addProduct(product);
                    product.setClientOrder(clientOrder);
                }
            }


        }catch (ConstraintViolationException e){
            throw new MyEntityNotFoundException("Invalid Data");
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
}
