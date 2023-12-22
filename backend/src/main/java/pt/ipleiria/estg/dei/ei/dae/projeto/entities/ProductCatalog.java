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
                name = "getAllProductCatalogs",
                query = "SELECT p FROM ProductCatalog p" // JPQL
        )
})
@Table(name = "products_catalogs")
public class ProductCatalog extends Versionable implements Serializable {
    @Id
    long code;

    @NotNull
    String name;
    @OneToMany(mappedBy = "productCatalog", cascade = CascadeType.REMOVE)
    @NotNull
    List<Product> products;
    @ManyToOne
    @JoinColumn(name = "product_manufacter_code")
    @NotNull
    private ProductManufacter productManufacter;

    public ProductCatalog() {

    }

    public ProductCatalog(long code, String name, ProductManufacter productManufacter) {
        this.code = code;
        this.name = name;
        this.productManufacter = productManufacter;
        this.products = new ArrayList<Product>();
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductManufacter getProductManufacter() {
        return productManufacter;
    }

    public void setProductManufacter(ProductManufacter productManufacter) {
        this.productManufacter = productManufacter;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}
