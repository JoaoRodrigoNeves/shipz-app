package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ProductPackage extends Package implements Serializable {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_package_association",
            joinColumns = @JoinColumn(name = "package_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "product_code", referencedColumnName = "code")
    )
    List<Product> products;

    public ProductPackage() {
    }

    public ProductPackage(long code, String type, String material, String status, Date manufacturingDate) {
        super(code, type, material, status, manufacturingDate);
        this.products = new ArrayList<Product>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void remoteProduct(Product product) {
        this.products.remove(product);
    }
}
