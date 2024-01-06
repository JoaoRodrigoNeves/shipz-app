package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class ProductPackageBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, code);
        return productPackage != null;
    }

    //TODO CRUD operations for ProductPackage entity

    public ProductPackage create(String type, String material, String manufacturingDate) {
        ProductPackage productPackage = new ProductPackage(type, material, manufacturingDate);
        entityManager.persist(productPackage);
        return productPackage;
    }

    public ProductPackage find(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, code);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return productPackage;
    }

    public void update(long code, String type, String material, String manufacturingDate) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        productPackage.setType(type);
        productPackage.setMaterial(material);
        productPackage.setManufacturingDate(manufacturingDate);
        entityManager.merge(productPackage);
    }

    public ProductPackage delete(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        entityManager.remove(productPackage);
        productPackage.getProducts().forEach(product -> product.removeProductPackage(productPackage));
        return productPackage;
    }

    //TODO get all productPackages
    public List<ProductPackage> getAll() {
        return entityManager.createNamedQuery("getAllProductPackages", ProductPackage.class).getResultList();
    }

    //TODO get all products of a product package
    public ProductPackage getProducts(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        Hibernate.initialize(productPackage.getProducts());
        return productPackage;
    }

    //TODO get sensors
    public ProductPackage getSensors(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        Hibernate.initialize(productPackage.getSensors());
        return productPackage;
    }
}
