package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;

    //TODO CRUD operations for Sensor entity
    public Sensor create(String type) {
        SensorType sensorType = SensorType.fromString(type);
        Sensor sensor = new Sensor(sensorType);
        entityManager.persist(sensor);
        return sensor;
    }

    public Sensor find(long code) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, code);
        if (sensor == null)
            throw new MyEntityNotFoundException("Sensor with code: " + code + " not found");
        return sensor;
    }

    public Sensor update(long code, String type) throws MyEntityNotFoundException, MyConstraintViolationException {
        Sensor sensor = this.find(code);
        try {
            SensorType sensorType = SensorType.fromString(type);
            sensor.setType(sensorType);
            entityManager.persist(sensor);
            return sensor;
        } catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public Sensor delete(long code) throws MyEntityNotFoundException {
        Sensor sensor = this.find(code);
        if (sensor.getPackages() != null)
            sensor.getPackages().forEach(aPackage -> aPackage.removeSensor(sensor));
        if (sensor.getObservations() != null)
            sensor.getObservations().forEach(observation -> observation.setSensor(null));
        entityManager.remove(sensor);
        return sensor;
    }

    //TODO get all Sensors
    public List<Sensor> getAll() {
        return entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
    }

    //TODO associate / disassociate sensor with package
    public void addToPackage(long code, long packageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        Package p = entityManager.find(Package.class, packageCode);

        if (p == null)
            throw new MyEntityNotFoundException("Package with code: " + packageCode + " not found");

        Sensor sensor = this.find(code);
        if (sensor.getPackages().contains(p))
            throw new MyEntityExistsException("Sensor with code: " + code + " already added to Package: " + packageCode);

        sensor.addPackage(p);
        p.addSensor(sensor);
    }

    public void removeFromPackage(long code, long packageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        Package p = entityManager.find(Package.class, packageCode);

        if (p == null)
            throw new MyEntityNotFoundException("Package with code: " + packageCode + " not found");
        Sensor sensor = this.find(code);
        if (!sensor.getPackages().contains(p))
            throw new MyEntityExistsException("Sensor with code: " + code + " not added to Package: " + packageCode);

        sensor.removePackage(p);
        p.removeSensor(sensor);
    }

    //TODO get packages
    public Sensor getPackages(long code) throws MyEntityNotFoundException {
        Sensor sensor = this.find(code);
        Hibernate.initialize(sensor.getPackages());
        return sensor;
    }

    //TODO get observations
    public Sensor getObservations(long code) throws MyEntityNotFoundException {
        Sensor sensor = this.find(code);
        Hibernate.initialize(sensor.getObservations());
        return sensor;
    }

    public List<Observation> getFilteredObservations(long code, LocalDateTime startDate, LocalDateTime endDate) throws MyEntityNotFoundException {
        Sensor sensor = this.find(code);

        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor not found with code: " + code);
        }

        Hibernate.initialize(sensor.getObservations());

        return sensor.getObservations().stream()
                .filter(observation -> isWithinDateRange(observation.getCreatedAt(), startDate, endDate))
                .collect(Collectors.toList());
    }

    private boolean isWithinDateRange(LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate) {
        return date != null && (!date.isBefore(startDate) && !date.isAfter(endDate) || date.isEqual(startDate) || date.isEqual(endDate));
    }
}
