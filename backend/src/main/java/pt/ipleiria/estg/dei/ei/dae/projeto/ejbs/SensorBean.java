package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
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

    //TODO CRUD operations for Sensor entity
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
}
