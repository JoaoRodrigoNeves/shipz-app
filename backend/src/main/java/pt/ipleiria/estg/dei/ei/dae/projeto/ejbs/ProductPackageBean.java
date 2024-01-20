package pt.ipleiria.estg.dei.ei.dae.projeto.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class ProductPackageBean {

    @PersistenceContext
    private EntityManager entityManager;

    //TODO CRUD operations for ProductPackage entity

    public ProductPackage create(PackageType type, String material, Long volume) {
        ProductPackage productPackage = new ProductPackage(type, material, volume);
        entityManager.persist(productPackage);
        return productPackage;
    }

    public ProductPackage find(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = entityManager.find(ProductPackage.class, code);
        if (productPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + code + " not found");
        return productPackage;
    }

    /*public void update(long code, PackageType type, String material) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        productPackage.setType(type);
        productPackage.setMaterial(material);
        entityManager.merge(productPackage);
    }*/

    /*public ProductPackage delete(long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = this.find(code);
        entityManager.remove(productPackage);
        productPackage.getProducts().forEach(product -> product.removeProductPackage(productPackage));
        return productPackage;
    }*/

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

    public void addProductToAllPackages(Product product) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = entityManager.find(ProductCatalog.class, product.getProductCatalog().getCode());

        if (productCatalog.getSecondaryPackageMaterial() != null && productCatalog.getMaxSecondaryPackage() != null) {
            ProductPackage productPackageSecondary = productCatalog.getActiveProductPackageSecondaryCode() != null ? this.find(productCatalog.getActiveProductPackageSecondaryCode()) : null;
            if (productPackageSecondary != null && productPackageSecondary.getProducts().size() < productCatalog.getMaxSecondaryPackage()) {
                product.addProductPackage(productPackageSecondary);
                productPackageSecondary.addProduct(product);
            } else {
                long productPackageSecondaryVolume = productCatalog.getPrimaryPackageVolume() * productCatalog.getMaxSecondaryPackage();
                ProductPackage newProductPackageSecondary = new ProductPackage(PackageType.SECONDARY, productCatalog.getSecondaryPackageMaterial(), productPackageSecondaryVolume);
                entityManager.persist(newProductPackageSecondary);
                product.addProductPackage(newProductPackageSecondary);
                newProductPackageSecondary.addProduct(product);
                productCatalog.setActiveProductPackageSecondaryCode(newProductPackageSecondary.getCode());
            }
        }

        if (productCatalog.getTertiaryPackageMaterial() != null && productCatalog.getMaxTertiaryPackage() != null) {
            ProductPackage productPackageTertiary = productCatalog.getActiveProductPackageTertiaryCode() != null ? this.find(productCatalog.getActiveProductPackageTertiaryCode()) : null;
            if (productPackageTertiary != null && productPackageTertiary.getProducts().size() < (productCatalog.getMaxTertiaryPackage() * productCatalog.getMaxSecondaryPackage())) {
                product.addProductPackage(productPackageTertiary);
                productPackageTertiary.addProduct(product);
            } else {
                Long productPackageTertiaryVolume = productCatalog.getPrimaryPackageVolume() * productCatalog.getMaxSecondaryPackage() * productCatalog.getMaxTertiaryPackage();
                ProductPackage newProductPackageTertiary = new ProductPackage(PackageType.TERTIARY, productCatalog.getTertiaryPackageMaterial(), productPackageTertiaryVolume);
                entityManager.persist(newProductPackageTertiary);
                product.addProductPackage(newProductPackageTertiary);
                newProductPackageTertiary.addProduct(product);
                productCatalog.setActiveProductPackageTertiaryCode(newProductPackageTertiary.getCode());
            }
        }
    }
}
