package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
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
    public void create(String type, String material, String status, Date manufacturingDate) throws MyEntityExistsException {
        Package pack = new Package(type, material, status, manufacturingDate);
        entityManager.persist(pack);
    }

    public Package find(long code) throws MyEntityNotFoundException {
        Package pack = entityManager.find(Package.class, code);
        if (pack == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return pack;
    }

    public void update(long code, String type, String material, String status, Date manufacturingDate) throws MyEntityNotFoundException {
        Package pack = this.find(code);
        pack.setType(type);
        pack.setMaterial(material);
        pack.setStatus(status);
        pack.setManufacturingDate(manufacturingDate);
        entityManager.merge(pack);
    }

    public void delete(long code) throws MyEntityNotFoundException {
        Package pack = this.find(code);
        entityManager.remove(pack);
    }

    //TODO associate / disassociate order with transport package

}
