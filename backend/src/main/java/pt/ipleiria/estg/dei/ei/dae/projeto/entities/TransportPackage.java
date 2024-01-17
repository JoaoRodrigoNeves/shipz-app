package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTransportPackages",
                query = "SELECT tp FROM TransportPackage tp ORDER BY tp.code DESC"
        )
})
public class TransportPackage extends Package implements Serializable {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "transport_package_association",
            joinColumns = @JoinColumn(name = "package_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "transport_code", referencedColumnName = "code")
    )
    List<ClientOrder> clientOrders;
    String location;

    public TransportPackage() {
    }

    public TransportPackage(PackageType type, String material, long volume) {
        super(type, material, volume);
        this.clientOrders = new ArrayList<ClientOrder>();
    }

    public List<ClientOrder> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(List<ClientOrder> clientOrders) {
        this.clientOrders = clientOrders;
    }

    public void addClientOrder(ClientOrder clientOrder) {
        this.clientOrders.add(clientOrder);
    }

    public void removeClientOrder(ClientOrder clientOrder) {
        this.clientOrders.remove(clientOrder);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransportPackage that = (TransportPackage) o;
        return Objects.equals(clientOrders, that.clientOrders) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientOrders, location);
    }
}
