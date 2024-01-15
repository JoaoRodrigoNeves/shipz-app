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
    private List<ClientOrder> clientOrders;
    String address;
    public FinalCostumer() {
        clientOrders = new ArrayList<>();
    }

    public FinalCostumer(String username, String password, String name, String email, String address) {
        super(username, password, name, email);
        this.address = address;
        clientOrders = new ArrayList<>();
    }

    public void addOrder(ClientOrder clientOrder){
        clientOrders.add(clientOrder);
    }

    public void removeOrder(ClientOrder clientOrder){
        clientOrders.remove(clientOrder);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ClientOrder> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(List<ClientOrder> clientOrders) {
        this.clientOrders = clientOrders;
    }

}
