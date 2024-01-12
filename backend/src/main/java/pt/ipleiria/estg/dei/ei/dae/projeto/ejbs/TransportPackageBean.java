package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

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
    public TransportPackage create(String type, String material, String location, String manufacturingDate) throws MyEntityExistsException {
        TransportPackage transportPackage = new TransportPackage(type, material, location, manufacturingDate);
        entityManager.persist(transportPackage);
        return transportPackage;
    }

    public TransportPackage find(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        if (transportPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return transportPackage;
    }

    public void update(long code, String type, String material, String manufacturingDate) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        transportPackage.setType(type);
        transportPackage.setMaterial(material);
        transportPackage.setManufacturingDate(manufacturingDate);
        entityManager.merge(transportPackage);
    }

    public TransportPackage delete(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        entityManager.remove(transportPackage);
        transportPackage.getClientOrders().forEach(clientOrder -> clientOrder.removeTransportPackage(transportPackage));
        return transportPackage;
    }

    //TODO get all transportPackages
    public List<TransportPackage> getAll() {
        return entityManager.createNamedQuery("getAllTransportPackages", TransportPackage.class).getResultList();
    }

    //TODO get all clientOrders of a transport package
    public List<ClientOrder> getClientOrders(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        Hibernate.initialize(transportPackage.getClientOrders());
        return transportPackage.getClientOrders();
    }
}
