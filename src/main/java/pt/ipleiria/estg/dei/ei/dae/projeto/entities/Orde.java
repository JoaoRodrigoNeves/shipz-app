package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM Orde o ORDER BY o.id" // JPQL
        )
})
public class Orde implements Serializable {

    @Id
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "logisticOperator_username")
    private LogisticOperator logisticOperator;

    public Orde(long id, LogisticOperator logisticOperator) {
        this.id = id;
        setLogisticOperator(logisticOperator);
    }
    public Orde() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LogisticOperator getLogisticOperator() {
        return logisticOperator;
    }

    public void setLogisticOperator(LogisticOperator logisticOperator) {
        this.logisticOperator = logisticOperator;
    }
}
