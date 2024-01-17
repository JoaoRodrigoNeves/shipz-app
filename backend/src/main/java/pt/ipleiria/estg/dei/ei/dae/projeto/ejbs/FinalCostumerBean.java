package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.FinalCostumer;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Hasher;
import org.hibernate.Hibernate;

import java.util.List;

@Stateless
public class FinalCostumerBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(fc.username) FROM FinalCostumer fc WHERE fc.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(String username, String password, String name, String email, String address) throws MyEntityNotFoundException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityNotFoundException("Username '" + username + "' already exists");
        }

        try {
            var finalCostumer = new FinalCostumer(username, hasher.hash(password), name, email, address);
            entityManager.persist(finalCostumer);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public FinalCostumer find(String username) {
        return entityManager.find(FinalCostumer.class, username);
    }

    public void update(String username, String password, String name, String email, String address) throws MyEntityNotFoundException, MyConstraintViolationException {

        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, username);

        if (finalCostumer == null) {
            throw new MyEntityNotFoundException("FinalCostumer with username '" + username + "' not found");
        }
        entityManager.lock(finalCostumer, LockModeType.OPTIMISTIC);

        try {
            if(password != null){
                finalCostumer.setPassword(hasher.hash(password));
            }
            finalCostumer.setName(name);
            finalCostumer.setEmail(email);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        entityManager.merge(finalCostumer);
    }

    public boolean delete(String username) throws MyEntityNotFoundException {
        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, username);

        if (finalCostumer != null) {
            throw new MyEntityNotFoundException("FinalCostumer with username '" + username + "' not found");
        }
        entityManager.remove(finalCostumer);

        FinalCostumer finalCostumerFind = entityManager.find(FinalCostumer.class, username);
        return finalCostumerFind != null;
    }

    //TODO: get all
    public List<FinalCostumer> getAll() {
        return entityManager.createNamedQuery("getAllFinalCostumers", FinalCostumer.class).getResultList();
    }

    //TODO get final costumer -> clients orders
    public FinalCostumer getClientOrders(String username) throws MyEntityNotFoundException {
        FinalCostumer finalCostumer = this.find(username);
        Hibernate.initialize(finalCostumer.getClientOrders());
        return finalCostumer;
    }

    public FinalCostumer findFinalCostumerWithClientOrder(String username) throws MyEntityNotFoundException {
        if (!exists(username)) {
            throw new MyEntityNotFoundException("Final Costumer with username '" + username + "' not found");
        }
        FinalCostumer finalCostumer = entityManager.find(FinalCostumer.class, username);
        Hibernate.initialize(finalCostumer.getClientOrders());
        return finalCostumer;
    }
}
