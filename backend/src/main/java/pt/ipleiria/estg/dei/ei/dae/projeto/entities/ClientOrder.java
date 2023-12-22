package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM ClientOrder o ORDER BY o.id" // JPQL
        )
})
public class ClientOrder {
    @Id
    @NotNull
    private long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logisticoperator_username")
    private LogisticOperator logisticOperator;

    @OneToMany(mappedBy = "clientOrder", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Product> products;

    public ClientOrder( long code, LogisticOperator logisticOperator) {
        this.code = code;
        this.logisticOperator = logisticOperator;
        products = new ArrayList<>();
    }

    public ClientOrder() {
        products = new ArrayList<>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public LogisticOperator getLogisticOperator() {
        return logisticOperator;
    }

    public void setLogisticOperator(LogisticOperator logisticOperator) {
        this.logisticOperator = logisticOperator;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void removeProduct(Product product){
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
