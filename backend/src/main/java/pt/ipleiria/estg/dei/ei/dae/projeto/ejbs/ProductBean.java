package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long code) {
        Product product = entityManager.find(Product.class, code);
        return product != null;
    }

    /**
     * TODO CRUD operations for Product entity
     */
    public void create(long code, long productCatalogCode) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        if (exists(code))
            throw new MyEntityExistsException("Product with code: " + code + " already exists");

        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);

        if (productCatalog == null)
            throw new MyEntityNotFoundException("Product Catalog with code: " + productCatalogCode + " not found");

        try {
            Product product = new Product(code, productCatalog);
            entityManager.persist(product);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Product find(long code) throws MyEntityNotFoundException {
        Product product = entityManager.find(Product.class, code);
        if (product == null)
            throw new MyEntityNotFoundException("Product with code: " + code + " not found");

        return product;
    }

    public void update(long code, long productCatalogCode) throws MyEntityNotFoundException {
        Product product = this.find(code);

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
        // caso seja um produto recente sem qualquer associação
        if (product.getClientOrder() != null)
            product.getClientOrder().removeProduct(product);
        if (product.getProductPackages() != null)
            product.getProductPackages().forEach(productPackage -> productPackage.removeProduct(product));
    }

    //TODO associate / disassociate product with product package

    public void addProductToPackage(long productCode, long productPackageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, productPackageCode);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + productPackageCode + " not found");

        Product product = this.find(productCode);

        if (product.getProductPackages().contains(productPackage))
            throw new MyEntityExistsException("Product with code: " + productCode + " already added to Package: " + productPackageCode);

        product.addProductPackage(productPackage);
        productPackage.addProduct(product);
    }

    public void removeProductFromPackage(long productCode, long productPackageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, productPackageCode);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + productPackageCode + " not found");

        Product product = this.find(productCode);

        if (!product.getProductPackages().contains(productPackage))
            throw new MyEntityExistsException("Product with code: " + productCode + " not added to Package: " + productPackage);

        product.removeProductPackage(productPackage);
        productPackage.removeProduct(product);
    }

    //TODO associate / disassociate product with client order
    public void addProductToOrder(long productCode, long clientOrderCode) throws MyEntityExistsException, MyEntityNotFoundException {
        ClientOrder clientOrder = entityManager.find(ClientOrder.class, clientOrderCode);

        if (clientOrder == null)
            throw new MyEntityNotFoundException("Client Order with code: " + clientOrderCode + " not found");

        Product product = this.find(productCode);

        if (clientOrder.getProducts().contains(product))
            throw new MyEntityExistsException("Product with code: " + productCode + " already added to Order: " + clientOrderCode);

        product.setClientOrder(clientOrder);
        clientOrder.addProduct(product);
    }

    public void removeProductFromOrder(long productCode, long clientOrderCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ClientOrder clientOrder = entityManager.find(ClientOrder.class, clientOrderCode);
        if (clientOrder == null)
            throw new MyEntityNotFoundException("Client Order with code: " + clientOrderCode + " not found");

        Product product = this.find(productCode);

        if (!clientOrder.getProducts().contains(product))
            throw new MyEntityExistsException("Product with code: " + productCode + " not added to Client Order: " + clientOrderCode);

        product.setClientOrder(null);
        clientOrder.removeProduct(product);
    }

    //TODO get all products
    public List<Product> getAllProducts() {
        return entityManager.createNamedQuery("getAllProducts", Product.class).getResultList();
    }
}