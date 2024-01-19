package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.TransportPackageCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackageCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
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

    public boolean create(TransportPackageCreateDTO transportPackageCreateDTO) {
        Order order = entityManager.find(Order.class, transportPackageCreateDTO.getOrderCode());
        TransportPackageCatalog transportPackageCatalog = entityManager.find(TransportPackageCatalog.class, transportPackageCreateDTO.getTransportPackageCatalogCode());

        long productsVolume = order.getProducts().stream().mapToLong(product -> product.getProductCatalog().getPrimaryPackageVolume()).sum();
        long transportPackagesVolume = order.getTransportPackages().stream().mapToLong(Package::getVolume).sum();
        boolean dontNeedAddPackage = transportPackagesVolume > productsVolume;

        TransportPackage transportPackage = new TransportPackage(PackageType.TRANSPORT, transportPackageCatalog.getMaterial(), transportPackageCatalog.getVolume(), transportPackageCatalog);
        order.addTransportPackage(transportPackage);
        transportPackage.addOrder(order);
        entityManager.persist(transportPackage);
        entityManager.flush();
        return dontNeedAddPackage;
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

    public void delete(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        List<Order> orders = this.getOrders(code);
        if (!orders.isEmpty()) {
            orders.get(0).removeTransportPackage(transportPackage);
            transportPackage.removeClientOrder(orders.get(0));
        }
        entityManager.remove(transportPackage);
    }

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

    public TransportPackage getSensors(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = this.find(code);
        Hibernate.initialize(transportPackage.getSensors());
        return transportPackage;
    }
}
