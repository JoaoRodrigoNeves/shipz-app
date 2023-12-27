package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductManufacters",
                query = "SELECT p FROM ProductManufacter p" // JPQL
        )
})
public class ProductManufacter extends User implements Serializable {

    @OneToMany(mappedBy = "productManufacter", cascade = CascadeType.REMOVE) @NotNull
    List<ProductCatalog> productCatalogs;

    @OneToMany(mappedBy = "productManufacter", cascade = CascadeType.REMOVE) @NotNull
    List<Product> products;

    public ProductManufacter() {
    }

    public ProductManufacter(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.productCatalogs = new ArrayList<ProductCatalog>();
        this.products = new ArrayList<Product>();
    }

    public List<ProductCatalog> getProductCatalogs() {
        return productCatalogs;
    }

    public void setProductCatalogs(List<ProductCatalog> productsCatalog) {
        this.productCatalogs = productsCatalog;
    }

    public void addProductCatalog(ProductCatalog productCatalog){
        this.productCatalogs.add(productCatalog);
    }

    public void removeProductCatalog(ProductCatalog productCatalog){
        this.productCatalogs.remove(productCatalog);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductManufacter that = (ProductManufacter) o;
        return Objects.equals(productCatalogs, that.productCatalogs) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCatalogs, products);
    }
}
