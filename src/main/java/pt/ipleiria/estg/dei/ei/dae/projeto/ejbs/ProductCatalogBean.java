package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
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

    public void create(long code, String name, String productManufacterUsername) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        ProductCatalog productCatalogCheck = entityManager.find(ProductCatalog.class, code);
        if(productCatalogCheck != null)
            throw new MyEntityExistsException("O product catalog " + code + " já existe.");

        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, productManufacterUsername);

        if(productManufacter == null)
            throw new MyEntityNotFoundException("O product manufacter com o nome de utilizador " + productManufacterUsername + " não existe.");

        try {
            var productCatalog = new ProductCatalog(code, name, productManufacter);
            productManufacter.addProductCatalog(productCatalog);
            entityManager.persist(productCatalog);
            entityManager.flush();

        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<ProductCatalog> getAll() {
        return entityManager.createNamedQuery("getAllProductCatalogs", ProductCatalog.class).getResultList();
    }

    public ProductCatalog find(long code) {
        return entityManager.find(ProductCatalog.class, code);
    }

    public void update(long code, String name, List<Product> products) {

        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, code);

        if (productCatalog == null) {
            System.err.println("ERROR_PRODUCT_CATALOG_NOT_FOUND: " + code);
            return;
        }

        entityManager.lock(productCatalog, LockModeType.OPTIMISTIC);

        productCatalog.setName(name);
        productCatalog.setProducts(products);

        entityManager.merge(productCatalog);
    }

    public boolean remove(long code) {

        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, code);

        if (productCatalog == null) {
            System.err.println("ERROR_PRODUCT_CATALOG_NOT_FOUND: " + code);
            return false;
        }

        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, productCatalog.getProductManufacter());

        if (productManufacter == null) {
            System.err.println("ERROR_PRODUCT_MANUFACTER_NOT_FOUND: " + productCatalog.getProductManufacter());
            return false;
        }

        productManufacter.removeProductCatalog(productCatalog);

        entityManager.remove(productCatalog);

        ProductCatalog productCatalogFind = entityManager.find(ProductCatalog.class, code);
        return productCatalogFind != null;
    }

    public boolean addProductToProductCatalog(long productCatalogCode, long productCode){
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);
        Product product = entityManager.find(Product.class, productCode);

        if(productCatalog != null && product != null){
            if(!productCatalog.getProducts().contains(product)){
                productCatalog.addProduct(product);
                return true;
            }
        }

        return false;
    }

    public boolean removeProductToProductCatalog(long productCatalogCode, long productCode){
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);
        Product product = entityManager.find(Product.class, productCode);

        if(productCatalog != null && product != null){
            if(productCatalog.getProducts().contains(product)){
                productCatalog.removeProduct(product);
                return true;
            }
        }

        return false;
    }

}
