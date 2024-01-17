package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackageCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.ListNotEmptyException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
@Stateless
public class TransportPackageCatalogBean {
    @PersistenceContext
    private EntityManager entityManager;

    public TransportPackageCatalog create(String name, String material, long volume, String logisticOperatorUsername) throws MyEntityExistsException, MyEntityNotFoundException {
        LogisticOperator logisticOperator = entityManager.find(LogisticOperator.class, logisticOperatorUsername);
        if (logisticOperator == null)
            throw new MyEntityNotFoundException("Logistic Operator with username: " + logisticOperatorUsername + " not found");
        TransportPackageCatalog transportPackageCatalog = new TransportPackageCatalog(name, material, volume, logisticOperator);
        entityManager.persist(transportPackageCatalog);
        return transportPackageCatalog;
    }

    public TransportPackageCatalog find(long code) throws MyEntityNotFoundException {
        TransportPackageCatalog transportPackageCatalog = entityManager.find(TransportPackageCatalog.class, code);
        if (transportPackageCatalog == null)
            throw new MyEntityNotFoundException("Transport Package Catalog with code: " + code + " not found");
        Hibernate.initialize(transportPackageCatalog.getTransportPackages());
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