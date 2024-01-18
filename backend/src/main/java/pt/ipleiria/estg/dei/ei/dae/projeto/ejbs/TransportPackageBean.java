package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
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

    public TransportPackage find(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        if (transportPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return transportPackage;
    }

    /*public void update(long code, PackageType type, String material) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        transportPackage.setType(type);
        transportPackage.setMaterial(material);
        entityManager.merge(transportPackage);
    }*/

    /*public TransportPackage delete(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        entityManager.remove(transportPackage);
        transportPackage.getClientOrders().forEach(clientOrder -> clientOrder.removeTransportPackage(transportPackage));
        return transportPackage;
    }*/

    //TODO get all transportPackages
    public List<TransportPackage> getAll() {
        return entityManager.createNamedQuery("getAllTransportPackages", TransportPackage.class).getResultList();
    }

    //TODO get orders of a transport package
    public List<Order> getOrders(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        Hibernate.initialize(transportPackage.getOrders());
        return transportPackage.getOrders();
    }
}
