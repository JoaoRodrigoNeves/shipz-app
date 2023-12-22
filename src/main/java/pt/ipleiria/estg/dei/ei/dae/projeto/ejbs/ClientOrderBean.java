package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ClientOrderBean {

    @PersistenceContext
    private EntityManager em;

    public void create(long code, String LO) throws MyEntityNotFoundException{
        LogisticOperator logisticOperator = em.find(LogisticOperator.class, LO);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + LO + " doesn't exist");
        }
        var clientOrderCode = em.createQuery(
                "SELECT COUNT(s.code) FROM ClientOrder s WHERE s.code = :code",
                Long.class
        );
        clientOrderCode.setParameter("code", code);
        if (clientOrderCode.getSingleResult() > 0L) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " already exists");
        }
        try{
            ClientOrder clientOrder = new ClientOrder(code, logisticOperator);
            logisticOperator.addOrder(clientOrder);
            em.persist(clientOrder);
            em.flush();
        }catch (ConstraintViolationException e){
            throw new MyEntityNotFoundException("Invalid Data");
        }

    }
    public List<ClientOrder> getAll(){
        return em.createNamedQuery("getAllOrders", ClientOrder.class).getResultList();
    }
    
    public ClientOrder find (long code) throws MyEntityNotFoundException {
        if (!exists(code)) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " doesn't exist");
        }
        return em.find(ClientOrder.class, code);
    }

    public boolean exists(long code) {
        Query query = em.createQuery(
                "SELECT COUNT(co.code) FROM ClientOrder co WHERE co.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    public ClientOrder findClientOrderWithProducts(long code) throws MyEntityNotFoundException {
        if (!exists(code)) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " doesn't exist");
        }
        ClientOrder clientOrder = em.find(ClientOrder.class, code);
        Hibernate.initialize(clientOrder.getProducts());
        return clientOrder;
    }

    public void enrollProduct(long code, long productCode) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(code);
        Product product = em.find(Product.class, productCode);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with code: " + productCode + " doesn't exist");
        }
        clientOrder.addProduct(product);
        product.setClientOrder(clientOrder);
    }

    public void unrollProduct(long code, long productCode) throws MyEntityNotFoundException {
        ClientOrder clientOrder = find(code);
        Product product = em.find(Product.class, productCode);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with code: " + productCode + " doesn't exist");
        }
        clientOrder.removeProduct(product);
        product.setClientOrder(null);
    }
}
