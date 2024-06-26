package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import javax.xml.stream.Location;
import java.util.Date;

@Stateless
public class PackageBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        Package packageCheck = entityManager.find(Package.class, code);
        return packageCheck != null;
    }

    //TODO CRUD operations for Package entity
    /*public void create(String type, String material, String status, String location, Date manufacturingDate) throws MyEntityExistsException {
        Package pack = new Package(type, material, status, location, manufacturingDate);
        entityManager.persist(pack);
    }*/

    public Package find(long code) throws MyEntityNotFoundException {
        Package pack = entityManager.find(Package.class, code);
        if (pack == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return pack;
    }

    public void update(long code, PackageType type, String material) throws MyEntityNotFoundException {
        Package pack = this.find(code);
        pack.setType(type);
        pack.setMaterial(material);
        entityManager.merge(pack);
    }

    public void delete(long code) throws MyEntityNotFoundException {
        Package pack = this.find(code);
        entityManager.remove(pack);
    }

    //TODO find package with sensors
    public Package findWithSensors(long code) throws MyEntityNotFoundException {
        Package pack = this.find(code);
        Hibernate.initialize(pack.getSensors());
        return pack;
    }

}
