package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

@Stateless
public class ProductBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        Product product = entityManager.find(Product.class, code);
        return product != null;
    }

    //TODO CRUD operations for Product entity
    public void create(long code, String name, long productCatalogCode) throws MyEntityExistsException {
        if (exists(code))
            throw new MyEntityExistsException("Product with code: " + code + " already exists");

        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);

        if (productCatalog == null)
            throw new MyEntityExistsException("Product Catalog with code: " + productCatalogCode + " already exists");

        try {
            Product product = new Product(code, name, productCatalog);
            productCatalog.addProduct(product);
            entityManager.persist(product);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            System.out.println(e);
        }
    }

    public Product find(long code) throws MyEntityNotFoundException {
        Product product = entityManager.find(Product.class, code);
        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + code + " not found");

        return product;
    }

    public void update(long code, String name, long productCatalogCode) throws MyEntityNotFoundException {
        Product product = this.find(code);
        product.setName(name);

        if (product.getProductCatalog().getCode() != productCatalogCode) {
            ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);
            if (productCatalog == null)
                throw new MyEntityNotFoundException("ProductCatalog with code: " + productCatalogCode + " not found");
            product.setProductCatalog(productCatalog);
        }

        entityManager.merge(product);
    }

    public void delete(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + code + " not found");

        entityManager.remove(product);
        product.getProductCatalog().removeProduct(product);
        product.getProductPackages().forEach(productPackage -> productPackage.removeProduct(product));
    }
}