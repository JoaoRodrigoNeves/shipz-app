package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductCatalogs",
                query = "SELECT p FROM ProductCatalog p" // JPQL
        )
})
@Table(name = "products_catalogs", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ProductCatalog extends Versionable implements Serializable {
    @Id
    long code;

    @NotNull
    String name;
    String catalogArea;
    String category;
    String description;
    @OneToMany(mappedBy = "productCatalog", cascade = CascadeType.REMOVE)
    @NotNull
    List<Product> products;
    @ManyToOne
    @JoinColumn(name = "product_manufacter_code")
    @NotNull
    ProductManufacter productManufacter;

    public ProductCatalog() {

    }

    public ProductCatalog(long code, String name, String catalogArea, String category, String description, ProductManufacter productManufacter) {
        this.code = code;
        this.name = name;
        this.catalogArea = catalogArea;
        this.category = category;
        this.description = description;
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

    public String getCatalogArea() {
        return catalogArea;
    }

    public void setCatalogArea(String catalogArea) {
        this.catalogArea = catalogArea;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCatalog that = (ProductCatalog) o;
        return code == that.code && Objects.equals(name, that.name) && Objects.equals(products, that.products) && Objects.equals(productManufacter, that.productManufacter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, products, productManufacter);
    }
}
