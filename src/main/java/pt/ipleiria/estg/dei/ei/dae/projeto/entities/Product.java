package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

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

    @NotNull
    String name;

    @ManyToOne
    @JoinColumn(name = "package_code")
    @NotNull
    Package pack;

    @ManyToOne
    @JoinColumn(name = "product_catalog_code")
    @NotNull
    private ProductCatalog productCatalog;

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

    public ProductCatalog getProductCatalog() {
        return productCatalog;
    }

    public void setProductCatalog(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public Package getPackage() {
        return pack;
    }

    public void setPackage(Package pack) {
        this.pack = pack;
    }
}
