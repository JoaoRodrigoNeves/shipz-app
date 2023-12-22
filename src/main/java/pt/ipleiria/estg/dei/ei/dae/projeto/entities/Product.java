package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT p FROM Product p ORDER BY p.createdAt DESC" //JPQL
        )
})
@Table(name="products")
public class Product extends Versionable implements Serializable {
    @Id
    long code;

    @NotNull
    String name;

    @ManyToOne @JoinColumn(name = "package_code") @NotNull
    Package pack;

    @ManyToOne
    @JoinColumn(name = "product_catalog_code")
    @NotNull
    private ProductCatalog productCatalog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_order_id")
    private ClientOrder clientOrder;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    public Product() {
    }

    public Product(long code, String name, Package pack, ProductCatalog productCatalog) {
        this.code = code;
        this.name = name;
        this.pack = pack;
        this.productCatalog = productCatalog;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ProductCatalog getProductCatalog() {
        return productCatalog;
    }

    public void setProductCatalog(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
    }

    public Package getPackage() {
        return pack;
    }

    public void setPackage(Package pack) {
        this.pack = pack;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }

    public ClientOrder getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(ClientOrder clientOrder) {
        this.clientOrder = clientOrder;
    }
}
