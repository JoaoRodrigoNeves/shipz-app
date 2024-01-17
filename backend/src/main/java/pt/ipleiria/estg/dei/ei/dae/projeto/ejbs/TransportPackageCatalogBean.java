package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackageCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.ListNotEmptyException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
@Stateless
public class TransportPackageCatalogBean {
    @PersistenceContext
    private EntityManager entityManager;

    public TransportPackageCatalog create(String name, String material, long volume) throws MyEntityExistsException {
        TransportPackageCatalog transportPackageCatalog = new TransportPackageCatalog(name, material, volume);
        entityManager.persist(transportPackageCatalog);
        return transportPackageCatalog;
    }

    public TransportPackageCatalog find(long code) throws MyEntityNotFoundException {
        TransportPackageCatalog transportPackageCatalog = entityManager.find(TransportPackageCatalog.class, code);
        if (transportPackageCatalog == null)
            throw new MyEntityNotFoundException("Transport Package Catalog with code: " + code + " not found");
        return transportPackageCatalog;
    }

    public void delete(long code) throws MyEntityNotFoundException, ListNotEmptyException {
        TransportPackageCatalog transportPackageCatalog = this.find(code);
        if (!transportPackageCatalog.getTransportPackages().isEmpty()){
            throw new ListNotEmptyException("Transport Package Catalog cannot be deleted");
        }

        entityManager.remove(transportPackageCatalog);
    }

    //TODO get all Transport Packages Catalog
    public List<TransportPackageCatalog> getAll() {
        return entityManager.createNamedQuery("getAllTransportPackageCatalogs", TransportPackageCatalog.class).getResultList();
    }
}
