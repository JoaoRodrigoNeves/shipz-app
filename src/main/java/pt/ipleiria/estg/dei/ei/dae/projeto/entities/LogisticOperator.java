package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllLogisticOperators",
                query = "SELECT s FROM LogisticOperator s ORDER BY s.name" // JPQL
        )
})
public class LogisticOperator extends User implements Serializable {
    @OneToMany(mappedBy = "logisticOperator", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ClientOrder> clientorders;

    public LogisticOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        clientorders = new ArrayList<>();
    }

    public LogisticOperator() {
        clientorders = new ArrayList<>();
    }

    public void addOrder(ClientOrder clientOrder){
        clientorders.add(clientOrder);
    }

    public void removeOrder(ClientOrder clientOrder){
        clientorders.remove(clientOrder);
    }

    public List<ClientOrder> getClientorders() {
        return clientorders;
    }

    public void setClientorders(List<ClientOrder> clientorders) {
        this.clientorders = clientorders;
    }
}
