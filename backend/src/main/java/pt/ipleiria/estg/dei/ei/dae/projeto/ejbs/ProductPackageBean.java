package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
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

    public void create(long code, String type, String material, String status, Date manufacturingDate) throws MyEntityExistsException {
        if (exists(code))
            throw new MyEntityExistsException("Package with code: " + code + " already exists");
        ProductPackage productPackage = new ProductPackage(code, type, material, status, manufacturingDate);
        entityManager.persist(productPackage);
    }

    public ProductPackage find(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, code);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return productPackage;
    }

    public void update(long code, String type, String material, String status, Date manufacturingDate) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        productPackage.setType(type);
        productPackage.setMaterial(material);
        productPackage.setStatus(status);
        productPackage.setManufacturingDate(manufacturingDate);
        entityManager.merge(productPackage);
    }

    public void delete(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        entityManager.remove(productPackage);
        productPackage.getProducts().forEach(product -> product.removeProductPackage(productPackage));
    }

    //TODO get all productPackages

    public List<ProductPackage> getProductPackages() {
        return entityManager.createNamedQuery("getAllProductPackages", ProductPackage.class).getResultList();
    }

    //TODO get all products of a product package
    public ProductPackage getProducts(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        if (productPackage != null)
            Hibernate.initialize(productPackage.getProducts());

        return productPackage;
    }
}
