package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Hasher;

import java.util.List;

@Stateless
public class LogisticOperatorBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public void create(String username, String password, String name, String email)
    throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("LogisticOperator with username: " + username + " already exists");
        }
        LogisticOperator logisticOperator = new LogisticOperator(username, hasher.hash(password), name, email);
        entityManager.persist(logisticOperator);
    }

    public boolean exists(String username){
        Query query = entityManager.createQuery(
                "SELECT COUNT(s.username) FROM LogisticOperator s WHERE s.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;

    }

    public void update(String username, String password, String name, String email) throws MyEntityNotFoundException{
        var logisticOperator = entityManager.find(LogisticOperator.class, username);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + username + " doesn't exist");
        }
        entityManager.lock(logisticOperator, LockModeType.OPTIMISTIC);
        if(password != null){
            logisticOperator.setPassword(hasher.hash(password));
        }
        logisticOperator.setName(name);
        logisticOperator.setEmail(email);
        entityManager.merge(logisticOperator);
    }

    public void delete(String username) throws MyEntityNotFoundException{
        LogisticOperator logisticOperator = entityManager.find(LogisticOperator.class, username);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + username + " doesn't exist");
        }
        entityManager.remove(logisticOperator);
    }

    public List<LogisticOperator> getAll() {
        return entityManager.createNamedQuery("getAllLogisticOperators", LogisticOperator.class).getResultList();
    }

    public LogisticOperator find(String username) throws MyEntityNotFoundException {
        LogisticOperator logisticOperator = entityManager.find(LogisticOperator.class, username);
        if (logisticOperator == null)
            throw new MyEntityNotFoundException("Logistic Operator with username: '" + username +  "' not found");
        return logisticOperator;
    }

    public LogisticOperator getOrders(String username) throws MyEntityNotFoundException {
        LogisticOperator logisticOperator = this.find(username);
        Hibernate.initialize(logisticOperator.getOrders());
        return logisticOperator;
    }
}
