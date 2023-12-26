package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT p FROM Product p ORDER BY p.code DESC" //JPQL
        )
})
@Table(name = "products")
public class Product extends Versionable implements Serializable {
    @Id
    long code;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    List<ProductPackage> productPackages;

    @ManyToOne
    @JoinColumn(name = "product_catalog_code")
    ProductCatalog productCatalog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_order_code")
    ClientOrder clientOrder;

    public Product() {
    }

    public Product(long code, ProductCatalog productCatalog) {
        this.code = code;
        this.productCatalog = productCatalog;
        this.productPackages = new ArrayList<ProductPackage>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public List<ProductPackage> getProductPackages() {
        return productPackages;
    }

    public void setProductPackages(List<ProductPackage> productPackages) {
        this.productPackages = productPackages;
    }

    public void addProductPackage(ProductPackage productPackage) {
        this.productPackages.add(productPackage);
    }

    public void removeProductPackage(ProductPackage productPackage) {
        this.productPackages.remove(productPackage);
    }

    public ProductCatalog getProductCatalog() {
        return productCatalog;
    }

    public void setProductCatalog(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public ClientOrder getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(ClientOrder clientOrder) {
        this.clientOrder = clientOrder;
    }
}
