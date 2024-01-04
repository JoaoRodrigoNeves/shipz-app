package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class TransportPackageBean {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        return transportPackage != null;
    }

    //TODO CRUD operations for TransportPackage entity
    public void create(String type, String material, String status, String location, String manufacturingDate) throws MyEntityExistsException {
        TransportPackage transportPackage = new TransportPackage(type, material, status, location, manufacturingDate);
        entityManager.persist(transportPackage);
    }

    public TransportPackage find(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        if (transportPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return transportPackage;
    }

    public void update(long code, String type, String material, String status, String manufacturingDate) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        transportPackage.setType(type);
        transportPackage.setMaterial(material);
        transportPackage.setStatus(status);
        transportPackage.setManufacturingDate(manufacturingDate);
        entityManager.merge(transportPackage);
    }

    /*public void delete(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        entityManager.remove(transportPackage);
        transportPackage.getPackages().forEach(pack -> pack.removeTransportPackage(transportPackage));
    }*/

    //TODO get all transportPackages
    public List<TransportPackage> getTransportPackages() {
        return entityManager.createNamedQuery("getAllTransportPackages", TransportPackage.class).getResultList();
    }
}
