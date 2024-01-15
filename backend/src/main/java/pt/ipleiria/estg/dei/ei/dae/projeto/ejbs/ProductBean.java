package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductBean {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * TODO CRUD operations for Product entity
     */
    public Product create(long productCatalogCode) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {;
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);

        if (productCatalog == null)
            throw new MyEntityNotFoundException("Product Catalog with code: " + productCatalogCode + " not found");

        try {
            Product product = new Product(productCatalog, productCatalog.getProductManufacter());
            entityManager.persist(product);

            productCatalog.addProduct(product);
            productCatalog.getProductManufacter().addProduct(product);

            ProductPackage productPackage = new ProductPackage(PackageType.PRIMARY, "Plástico");
            entityManager.persist(productPackage);

            return product;
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

    public Product update(long code, long productCatalogCode) throws MyEntityNotFoundException {
        Product product = this.find(code);
        if (product.getProductCatalog().getCode() != productCatalogCode) {
            // remove old catalog and old manufacter
            ProductCatalog productCatalogToRemove = product.getProductCatalog();
            productCatalogToRemove.removeProduct(product);
            productCatalogToRemove.getProductManufacter().removeProduct(product);

            // add new catalog and old manufacter
            ProductCatalog newProductCatalog = entityManager.find(ProductCatalog.class, productCatalogCode);
            if (newProductCatalog == null)
                throw new MyEntityNotFoundException("ProductCatalog with code: " + productCatalogCode + " not found");
            newProductCatalog.addProduct(product);
            newProductCatalog.getProductManufacter().addProduct(product);

            product.setProductCatalog(newProductCatalog);
            product.setProductManufacter(newProductCatalog.getProductManufacter());
        }
        entityManager.merge(product);
        return product;
    }

    public void delete(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        ProductCatalog productCatalog = product.getProductCatalog();
        ProductManufacter  productManufacter = product.getProductManufacter();

        // remove mandatory associations
        productCatalog.removeProduct(product);
        productManufacter.removeProduct(product);

        // in case it has no optional associations
        if (product.getClientOrder() != null)
            product.getClientOrder().removeProduct(product);
        if (product.getProductPackages() != null)
            product.getProductPackages().forEach(productPackage -> productPackage.removeProduct(product));

        entityManager.remove(product);
    }

    //TODO get / associate / disassociate product with product package
    public Product getProductPackages(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        Hibernate.initialize(product.getProductPackages());
        return product;
    }

    public void addProductToPackage(long code, long productPackageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, productPackageCode);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + productPackageCode + " not found");

        Product product = this.find(code);
        if (product.getProductPackages().contains(productPackage))
            throw new MyEntityExistsException("Product with code: " + code + " already added to Package: " + productPackageCode);

        product.addProductPackage(productPackage);
        productPackage.addProduct(product);
    }

    public void removeProductFromPackage(long code, long productPackageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, productPackageCode);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + productPackageCode + " not found");

        Product product = this.find(code);

        if (!product.getProductPackages().contains(productPackage))
            throw new MyEntityExistsException("Product with code: " + code + " not added to Package: " + productPackage);

        product.removeProductPackage(productPackage);
        productPackage.removeProduct(product);
    }

    //TODO get / associate / disassociate product with client order
    public ClientOrder getOrder(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        return product.getClientOrder();
    }

    public void addProductToOrder(long code, long clientOrderCode) throws MyEntityExistsException, MyEntityNotFoundException {
        ClientOrder clientOrder = entityManager.find(ClientOrder.class, clientOrderCode);

        if (clientOrder == null)
            throw new MyEntityNotFoundException("Client Order with code: " + clientOrderCode + " not found");

        Product product = this.find(code);

        if (clientOrder.getProducts().contains(product))
            throw new MyEntityExistsException("Product with code: " + code + " already added to Order: " + clientOrderCode);

        product.setClientOrder(clientOrder);
        clientOrder.addProduct(product);
    }

    public void removeProductFromOrder(long code, long clientOrderCode) throws MyEntityNotFoundException, MyEntityExistsException {
        ClientOrder clientOrder = entityManager.find(ClientOrder.class, clientOrderCode);
        if (clientOrder == null)
            throw new MyEntityNotFoundException("Client Order with code: " + clientOrderCode + " not found");

        Product product = this.find(code);

        if (!clientOrder.getProducts().contains(product))
            throw new MyEntityExistsException("Product with code: " + code + " not added to Client Order: " + clientOrderCode);

        product.setClientOrder(null);
        clientOrder.removeProduct(product);
    }

    //TODO get all products
    public List<Product> getAllProducts() {
        return entityManager.createNamedQuery("getAllProducts", Product.class).getResultList();
    }

    //TODO get product catalog
    public ProductCatalog getProductCatalog(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        return product.getProductCatalog();
    }

    //TODO get product manufacter
    public ProductManufacter getProductManufacter(long code) throws MyEntityNotFoundException {
        Product product = this.find(code);
        return product.getProductManufacter();
    }
}