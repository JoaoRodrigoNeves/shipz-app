package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

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

    public ProductPackage find(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, code);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return productPackage;
    }

    //TODO get all productPackages

    public List<ProductPackage> getProductPackages() {
        return entityManager.createNamedQuery("getAllPackages", ProductPackage.class).getResultList();
    }

    //TODO get all products of a product package
    public ProductPackage getProducts(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        if (productPackage != null)
            Hibernate.initialize(productPackage.getProducts());

        return productPackage;
    }
}
