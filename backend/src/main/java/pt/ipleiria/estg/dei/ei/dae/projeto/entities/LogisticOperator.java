package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllLogisticOperators",
                query = "SELECT s FROM LogisticOperator s ORDER BY s.name" // JPQL
        )
})
public class LogisticOperator extends User implements Serializable {
    @OneToMany(mappedBy = "logisticOperator", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<Order> orders;
    @OneToMany(mappedBy = "logisticOperator", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("volume DESC")
    List<TransportPackageCatalog> transportPackageCatalogs;

    public LogisticOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.orders = new ArrayList<>();
    }

    public LogisticOperator() {
    }

    public void addTransportPackageCatalog(TransportPackageCatalog transportPackageCatalog) {
        transportPackageCatalogs.add(transportPackageCatalog);
    }

    public void removeTransportPackageCatalog(TransportPackageCatalog transportPackageCatalog) {
        transportPackageCatalogs.remove(transportPackageCatalog);
    }

    public List<TransportPackageCatalog> getTransportPackageCatalogs() {
        return transportPackageCatalogs;
    }

    public void setTransportPackageCatalogs(List<TransportPackageCatalog> transportPackageCatalogs) {
        this.transportPackageCatalogs = transportPackageCatalogs;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
