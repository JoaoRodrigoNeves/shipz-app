package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM ClientOrder o ORDER BY o.id" // JPQL
        )
})
public class ClientOrder implements Serializable {

    @Id
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "logisticoperator_username")
    private LogisticOperator logisticOperator;

    public ClientOrder(long id, LogisticOperator logisticOperator) {
        this.id = id;
        setLogisticOperator(logisticOperator);
    }
    public ClientOrder() {
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
