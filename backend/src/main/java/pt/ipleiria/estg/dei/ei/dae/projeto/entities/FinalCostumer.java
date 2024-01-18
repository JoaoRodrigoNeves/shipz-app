package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllFinalCostumers",
                query = "SELECT fc from FinalCostumer fc ORDER BY fc.name"
        ),
})
public class FinalCostumer extends User implements Serializable {

    @OneToMany(mappedBy = "finalCostumer", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Order> orders;
    String address;
    public FinalCostumer() {
        orders = new ArrayList<>();
    }

    public FinalCostumer(String username, String password, String name, String email, String address) {
        super(username, password, name, email);
        this.address = address;
        orders = new ArrayList<>();
    }

    public void addOrder(Order clientOrder){
        orders.add(clientOrder);
    }

    public void removeOrder(Order clientOrder){
        orders.remove(clientOrder);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> clientOrders) {
        this.orders = clientOrders;
    }

}
