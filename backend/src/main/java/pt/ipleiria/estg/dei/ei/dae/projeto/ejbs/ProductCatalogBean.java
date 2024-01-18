package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.ListNotEmptyException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public ProductCatalog create(String name, String catalogArea, String category, String description, String productManufacterUsername, Integer maxSecondaryPackage, Integer maxTertiaryPackage, long primaryPackageVolume, String primaryPackageMaterial, String secondaryPackageMaterial, String tertiaryPackageMaterial, List<String> sensors) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, productManufacterUsername);

        if (productManufacter == null)
            throw new MyEntityNotFoundException("Product Manufacter with username: '" + productManufacterUsername + "' not found");

        try {
            var productCatalog = new ProductCatalog(name, catalogArea, category, description, productManufacter, maxSecondaryPackage, maxTertiaryPackage, primaryPackageVolume, primaryPackageMaterial, secondaryPackageMaterial, tertiaryPackageMaterial);
            if (sensors != null) {
                sensors.forEach(sensor -> {
                    SensorType sensorType = SensorType.fromString(sensor);
                    if (sensorType == SensorType.HUMIDITY)
                        productCatalog.setHumiditySensor(true);
                    if (sensorType == SensorType.PRESSURE)
                        productCatalog.setPressureSensor(true);
                    if (sensorType == SensorType.TEMPERATURE)
                        productCatalog.setTemperatureSensor(true);
                    if (sensorType == SensorType.GPS)
                        productCatalog.setGpsSensor(true);
                    if (sensorType == SensorType.DAMAGE)
                        productCatalog.setDamageSensor(true);
                });
            }
            productManufacter.addProductCatalog(productCatalog);
            entityManager.persist(productCatalog);
            entityManager.flush();
            return productCatalog;
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

    public ProductCatalog update(long code, String name, String catalogArea, String category, String description, String productManufacterUsername, Integer maxSecondaryPackage, Integer maxTertiaryPackage, String primaryPackageMaterial, String secondaryPackageMaterial, String tertiaryPackageMaterial) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = this.find(code);
        entityManager.lock(productCatalog, LockModeType.OPTIMISTIC);
        productCatalog.setName(name);
        productCatalog.setCatalogArea(catalogArea);
        productCatalog.setCategory(category);
        productCatalog.setDescription(description);
        productCatalog.setMaxSecondaryPackage(maxSecondaryPackage);
        productCatalog.setMaxTertiaryPackage(maxTertiaryPackage);
        productCatalog.setSecondaryPackageMaterial(secondaryPackageMaterial);
        productCatalog.setTertiaryPackageMaterial(tertiaryPackageMaterial);
        productCatalog.setPrimaryPackageMaterial(primaryPackageMaterial);

        if (!Objects.equals(productCatalog.getProductManufacter().getUsername(), productManufacterUsername)) {
            // remove old product manufacter
            ProductManufacter oldProductManufacter = productCatalog.getProductManufacter();
            productCatalog.getProducts().forEach(oldProductManufacter::removeProduct);
            oldProductManufacter.removeProductCatalog(productCatalog);

            // add new product manufacter
            ProductManufacter newProductManufacter = entityManager.find(ProductManufacter.class, productManufacterUsername);
            if (newProductManufacter == null)
                throw new MyEntityNotFoundException("Product Manufacter with username: '" + productManufacterUsername + "' not found");

            productCatalog.setProductManufacter(newProductManufacter);
            newProductManufacter.addProductCatalog(productCatalog);
            productCatalog.getProducts().forEach(newProductManufacter::addProduct);
        }

        entityManager.merge(productCatalog);
        return productCatalog;
    }

    public void remove(long code) throws MyEntityNotFoundException, ListNotEmptyException {
        ProductCatalog productCatalog = this.find(code);
        ProductManufacter productManufacter = entityManager.find(ProductManufacter.class, productCatalog.getProductManufacter().getUsername());

        if (!productCatalog.getProducts().isEmpty()) {
            throw new ListNotEmptyException("Product Catalog cannot be deleted");
        }

        productManufacter.removeProductCatalog(productCatalog);
        entityManager.remove(productCatalog);
    }

    public List<ProductCatalog> getAll() {
        return entityManager.createNamedQuery("getAllProductCatalogs", ProductCatalog.class).getResultList();
    }

    public List<ProductCatalog> getAllAvailable() {
        return entityManager
                .createNamedQuery("getAllProductCatalogs", ProductCatalog.class)
                .getResultList()
                .stream()
                .filter(productCatalog ->
                        productCatalog.getProducts().stream().noneMatch(product ->
                                product.getOrder() != null && product.getOrder().getCode() == 10000
                        ) && productCatalog.getProducts().stream().anyMatch(product ->
                                product.getOrder() == null
                        )
                )
                .collect(Collectors.toList());
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

    //TODO get products - product catalogs
    public ProductCatalog getProductCatalogProducts(long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = this.find(code);
        Hibernate.initialize(productCatalog.getProducts());
        return productCatalog;
    }

    public List<Product> getProductsWithoutProductPackage(ProductCatalog productCatalog) throws MyEntityNotFoundException {
        ProductCatalog productCatalogProducts = this.getProductCatalogProducts(productCatalog.getCode());
        List<Product> productsWithoutPackage = new ArrayList<Product>();
        productCatalogProducts.getProducts().forEach(product -> {
            if (product.getProductPackages() == null || product.getProductPackages().isEmpty())
                productsWithoutPackage.add(product);
        });
        return productsWithoutPackage;
    }
}
