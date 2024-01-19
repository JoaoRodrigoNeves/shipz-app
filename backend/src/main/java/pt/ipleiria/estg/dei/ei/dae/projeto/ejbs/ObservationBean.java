package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
@Stateless
public class ObservationBean {

    @PersistenceContext
    private EntityManager entityManager;

    public Observation create(long sensorCode, String value) throws MyEntityExistsException, MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, sensorCode);

        if(sensor == null)
            throw new MyEntityNotFoundException("Sensor with code: " + sensorCode + " not found");

        Observation observation = new Observation(value, sensor);
        sensor.addObservation(observation);
        entityManager.persist(observation);
        return observation;
    }
}
