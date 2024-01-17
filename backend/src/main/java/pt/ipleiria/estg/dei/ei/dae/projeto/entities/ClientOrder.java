package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.FinalCostumerDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM ClientOrder o ORDER BY o.code" // JPQL
        )
})
public class ClientOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "client_order_id_seq")
    @SequenceGenerator(name = "client_order_id_seq", sequenceName = "client_order_id_seq", initialValue = 100000)
    private long code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalcostumer_username")
    private FinalCostumer finalCostumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logisticoperator_username")
    private LogisticOperator logisticOperator;

    @OneToMany(mappedBy = "clientOrder", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Product> products;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "clientOrders")
    List<TransportPackage> transportPackages;

    private OrderStatus status;
    @Column(name = "created_at")
    Date createdAt;

    public ClientOrder(FinalCostumer finalCostumer) {
        this.finalCostumer = finalCostumer;
        this.products = new ArrayList<>();
        this.status = OrderStatus.STATUS_0;
    }

    public ClientOrder() {
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getProductQuantity() {
        return products.size();
    }

    public LogisticOperator getLogisticOperator() {
        return logisticOperator;
    }
    public FinalCostumer getFinalCostumer() {
        return finalCostumer;
    }

    public void setLogisticOperator(LogisticOperator logisticOperator) {
        this.logisticOperator = logisticOperator;
    }

    public void setFinalCostumer(FinalCostumer finalCostumer) {
        this.finalCostumer = finalCostumer;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setTransportPackages(List<TransportPackage> transportPackages) {
        this.transportPackages = transportPackages;
    }

    public void addTransportPackage(TransportPackage transportPackage) {
        this.transportPackages.add(transportPackage);
    }

    public void removeTransportPackage(TransportPackage transportPackage) {
        this.transportPackages.remove(transportPackage);
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientOrder that = (ClientOrder) o;
        return code == that.code && Objects.equals(logisticOperator, that.logisticOperator) && Objects.equals(products, that.products) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, logisticOperator, products, createdAt);
    }
}
