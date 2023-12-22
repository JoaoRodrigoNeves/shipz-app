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
    @OneToMany(mappedBy = "logisticOperator", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<ClientOrder> orders;

    public LogisticOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        orders = new ArrayList<>();
    }

    public LogisticOperator() {
        orders = new ArrayList<>();
    }

    public void addOrder(ClientOrder orde){
        orders.add(orde);
    }

    public void removeOrder(ClientOrder orde){
        orders.remove(orde);
    }

    public List<ClientOrder> getOrders() {
        return orders;
    }
}
