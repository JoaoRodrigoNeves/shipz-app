package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductCatalogBean {
    @PersistenceContext
    private EntityManager entityManager;

    private boolean exists(Long code) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p.code) FROM ProductCatalog p WHERE p.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long) query.getSingleResult() > 0L;
    }

    public void create(long code, String name, String productManufacterUsername) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        if (exists(code))
            throw new MyEntityExistsException("Product Catalog with code: " + code + " already exists");

        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, productManufacterUsername);

        if (productManufacter == null)
            throw new MyEntityNotFoundException("Product Manufacter with username: '" + productManufacterUsername + "' not found");

        try {
            var productCatalog = new ProductCatalog(code, name, productManufacter);
            productManufacter.addProductCatalog(productCatalog);
            entityManager.persist(productCatalog);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public ProductCatalog find(long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, code);
        if (productCatalog == null)
            throw new MyEntityNotFoundException("Product Catalog with code: " + code + " not found");
        return productCatalog;
    }

    public void update(long code, String name, List<Product> products) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = this.find(code);
        entityManager.lock(productCatalog, LockModeType.OPTIMISTIC);
        productCatalog.setName(name);
        productCatalog.setProducts(products);
        entityManager.merge(productCatalog);
    }

    public void remove(long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = this.find(code);

        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, productCatalog.getProductManufacter());
        entityManager.remove(productCatalog);
        productManufacter.removeProductCatalog(productCatalog);
    }

    public List<ProductCatalog> getAll() {
        return entityManager.createNamedQuery("getAllProductCatalogs", ProductCatalog.class).getResultList();
    }

    public void addProduct(long productCatalogCode, long productCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);
        Product product = entityManager.find(Product.class, productCode);

        if (productCatalog == null)
            throw new MyEntityNotFoundException("Product Catalog with code: " + productCatalogCode + " not found");

        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + productCode + " not found");

        if (productCatalog.getProducts().contains(product))
            throw new MyEntityExistsException("Product with code: " + productCode + " already associated with Product Catalog: " + productCatalogCode);

        product.setProductCatalog(productCatalog);
        productCatalog.addProduct(product);
    }

    public void removeProduct(long productCatalogCode, long productCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);
        Product product = entityManager.find(Product.class, productCode);

        if (productCatalog == null)
            throw new MyEntityNotFoundException("Product Catalog with code: " + productCatalogCode + " not found");

        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + productCode + " not found");

        if (!productCatalog.getProducts().contains(product))
            throw new MyEntityExistsException("Product with code: " + productCode + " not associated with Product Catalog: " + productCatalogCode);

        product.setProductCatalog(null);
        productCatalog.removeProduct(product);
    }
}
