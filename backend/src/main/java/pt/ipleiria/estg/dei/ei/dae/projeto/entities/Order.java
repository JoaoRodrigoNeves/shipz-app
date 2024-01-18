package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM Order o ORDER BY o.code" // JPQL
        )
})
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", initialValue = 100000)
    long code;
    @ManyToOne
    @JoinColumn(name = "final_costumer_username")
    FinalCostumer finalCostumer;
    @ManyToOne
    @JoinColumn(name = "logistic_operator_username")
    LogisticOperator logisticOperator;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.REMOVE)
    List<Product> products;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "orders")
    List<TransportPackage> transportPackages;
    OrderStatus status;
    SensorType sensors;
    String location;
    @Column(name = "created_at")
    LocalDateTime createdAt;
    @Column(name = "delivered_at")
    LocalDateTime deliveredAt;

    public Order() {
    }

    public Order(FinalCostumer finalCostumer, LogisticOperator logisticOperator) {
        this.finalCostumer = finalCostumer;
        this.logisticOperator = logisticOperator;
        this.products = new ArrayList<Product>();
        this.transportPackages = new ArrayList<TransportPackage>();
        this.status = OrderStatus.STATUS_0;
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

    public List<TransportPackage> getTransportPackages() {
        return transportPackages;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Sensor> getSensors() {
        List<Sensor> sensors = new ArrayList<>();
        for (TransportPackage transportPackage : transportPackages) {
            sensors.addAll(transportPackage.getSensors());
        }
        return sensors;
    }

    public void setSensors(SensorType sensors) {
        this.sensors = sensors;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        return code == that.code && Objects.equals(finalCostumer, that.finalCostumer) && Objects.equals(logisticOperator, that.logisticOperator) && Objects.equals(products, that.products) && Objects.equals(transportPackages, that.transportPackages) && status == that.status && Objects.equals(location, that.location) && Objects.equals(createdAt, that.createdAt) && Objects.equals(deliveredAt, that.deliveredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, finalCostumer, logisticOperator, products, transportPackages, status, location, createdAt, deliveredAt);
    }
}
