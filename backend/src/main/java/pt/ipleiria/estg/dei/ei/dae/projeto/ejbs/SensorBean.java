package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Sensor create(String type) throws MyEntityExistsException {
        Sensor sensor = new Sensor(type);
        entityManager.persist(sensor);
        return sensor;
    }
}
