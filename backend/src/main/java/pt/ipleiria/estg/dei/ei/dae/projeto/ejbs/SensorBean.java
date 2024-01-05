package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Sensor create(SensorType type) throws MyEntityExistsException {
        Sensor sensor = new Sensor(type);
        entityManager.persist(sensor);
        return sensor;
    }

    public Sensor find(long code) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, code);
        if (sensor == null)
            throw new MyEntityNotFoundException("Sensor with code: " + code + " not found");
        return sensor;
    }

    public List<Sensor> getAll() {
        return entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
    }
}
