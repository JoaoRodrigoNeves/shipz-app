package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductManufacters",
                query = "SELECT p FROM ProductManufacter p" // JPQL
        )
})
public class ProductManufacter extends User implements Serializable {

    @OneToMany(mappedBy = "productManufacter", cascade = CascadeType.REMOVE) @NotNull
    List<ProductCatalog> productsCatalog;

    public ProductManufacter() {
    }

    public ProductManufacter(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.productsCatalog = new ArrayList<ProductCatalog>();

    }

    public List<ProductCatalog> getProductsCatalog() {
        return productsCatalog;
    }

    public void setProductsCatalog(List<ProductCatalog> productsCatalog) {
        this.productsCatalog = productsCatalog;
    }

    public void addProductCatalog(ProductCatalog productCatalog){
        this.productsCatalog.add(productCatalog);
    }

    public void removeProductCatalog(ProductCatalog productCatalog){
        this.productsCatalog.remove(productCatalog);
    }
}