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
    List<ClientOrder> clientorders;
    @OneToMany(mappedBy = "logisticOperator", cascade = CascadeType.REMOVE)
    @OrderBy("volume DESC")
    List<TransportPackageCatalog> transportPackageCatalogs;
    public LogisticOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.clientorders = new ArrayList<>();
    }

    public LogisticOperator() {
    }

    public void addTransportPackageCatalog(TransportPackageCatalog transportPackageCatalog){
        transportPackageCatalogs.add(transportPackageCatalog);
    }

    public void removeTransportPackageCatalog(TransportPackageCatalog transportPackageCatalog){
        transportPackageCatalogs.remove(transportPackageCatalog);
    }

    public List<TransportPackageCatalog> getTransportPackageCatalogs() {
        return transportPackageCatalogs;
    }

    public void setTransportPackageCatalogs(List<TransportPackageCatalog> transportPackageCatalogs) {
        this.transportPackageCatalogs = transportPackageCatalogs;
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
