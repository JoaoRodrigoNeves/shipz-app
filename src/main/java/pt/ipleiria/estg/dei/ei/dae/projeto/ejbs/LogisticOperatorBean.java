package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class LogisticOperatorBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email,
                       double latitude, double longitude, int temperature, int humidity, int acceleration,
                       int otherAmbientalData, boolean isOpened, boolean isAutorized)
    throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("LogisticOperator with username: " + username + " already exists");
        }

        LogisticOperator logisticOperator = new LogisticOperator(username, password, name, email, latitude, longitude, temperature, humidity, acceleration, otherAmbientalData, isOpened, isAutorized);
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

    public void update(String username, String password, String name, String email,
                       double latitude, double longitude, int temperature, int humidity, int acceleration,
                       int otherAmbientalData, boolean isOpened, boolean isAutorized) throws MyEntityNotFoundException{
        var logisticOperator = em.find(LogisticOperator.class, username);
        if (logisticOperator == null) {
            throw new MyEntityNotFoundException("LogisticOperator with username: " + username + " doesn't exist");
        }
        em.lock(logisticOperator, LockModeType.OPTIMISTIC);
        logisticOperator.setPassword(password);
        logisticOperator.setName(name);
        logisticOperator.setEmail(email);
        logisticOperator.setLatitude(latitude);
        logisticOperator.setLongitude(longitude);
        logisticOperator.setTemperature(temperature);
        logisticOperator.setHumidity(humidity);
        logisticOperator.setAcceleration(acceleration);
        logisticOperator.setOtherAmbientalData(otherAmbientalData);
        logisticOperator.setOpened(isOpened);
        logisticOperator.setAutorized(isAutorized);
    }

    public void delete(String username) throws MyEntityNotFoundException{
        var logisticOperator = em.find(LogisticOperator.class, username);
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

}
