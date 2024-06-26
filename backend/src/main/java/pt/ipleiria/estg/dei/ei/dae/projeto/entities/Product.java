package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", initialValue = 100000)
    long code;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    List<ProductPackage> productPackages;

    @ManyToOne
    @JoinColumn(name = "product_catalog_code")
    ProductCatalog productCatalog;

    @ManyToOne
    @JoinColumn(name = "product_manufacter_code")
    ProductManufacter productManufacter;

    @ManyToOne
    @JoinColumn(name = "order_code")
    Order order;

    @Column(name = "created_at")
    LocalDateTime createdAt;


    public Product() {
    }

    public Product(ProductCatalog productCatalog, ProductManufacter productManufacter) {
        this.productCatalog = productCatalog;
        this.productManufacter = productManufacter;
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

    public ProductManufacter getProductManufacter() {
        return productManufacter;
    }

    public void setProductManufacter(ProductManufacter productManufacter) {
        this.productManufacter = productManufacter;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order clientOrder) {
        this.order = clientOrder;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
