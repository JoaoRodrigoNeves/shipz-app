package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class LogisticOperatorBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email)
    throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("LogisticOperator with username: " + username + " already exists");
        }
        LogisticOperator logisticOperator = new LogisticOperator(username, password, name, email);
        em.persist(logisticOperator);
    }

    public boolean exists(String username){
        Query query = em.createQuery(
                "SELECT COUNT(s.username) FROM LogisticOperator s WHERE s.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;

    }

    public void update(String username, String password, String name, String email) throws MyEntityNotFoundException{
        var logisticOperator = em.find(LogisticOperator.class, username);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + username + " doesn't exist");
        }
        em.lock(logisticOperator, LockModeType.OPTIMISTIC);
        logisticOperator.setPassword(password);
        logisticOperator.setName(name);
        logisticOperator.setEmail(email);
        em.merge(logisticOperator);
    }

    public void delete(String username) throws MyEntityNotFoundException{
        LogisticOperator logisticOperator = em.find(LogisticOperator.class, username);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + username + " doesn't exist");
        }
        em.remove(logisticOperator);
    }

    public List<LogisticOperator> getAll() {
        return em.createNamedQuery("getAllLogisticOperators", LogisticOperator.class).getResultList();
    }

    public LogisticOperator find(String username) {
        return em.find(LogisticOperator.class, username);
    }

    public LogisticOperator findLogisticOperatorWithClientOrder(String username) throws MyEntityNotFoundException {
        if (!exists(username)) {
            throw new MyEntityNotFoundException("Logistic Operator with username '" + username + "' not found");
        }
        LogisticOperator logisticOperator = em.find(LogisticOperator.class, username);
        Hibernate.initialize(logisticOperator.getClientorders());
        return logisticOperator;
    }
}
