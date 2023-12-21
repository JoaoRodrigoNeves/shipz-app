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
    @OneToMany(mappedBy = "logisticOperator", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Orde> ordes;

    public LogisticOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        ordes = new ArrayList<>();
    }

    public LogisticOperator() {
        ordes = new ArrayList<>();
    }

    public void addOrder(Orde orde){
        ordes.add(orde);
    }

    public void removeOrder(Orde orde){
        ordes.remove(orde);
    }

    public List<Orde> getOrdes() {
        return ordes;
    }
}
