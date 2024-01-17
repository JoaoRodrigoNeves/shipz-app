package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransportPackageDTO extends PackageDTO implements Serializable {

    String location;
    List<ClientOrderDTO> clientOrders;

    public TransportPackageDTO() {
    }

    public TransportPackageDTO(long code, PackageType type, String material, long volume, String location, String createdAt) {
        super(code, type, material, volume, createdAt);
        this.location = location;
        this.clientOrders = new ArrayList<ClientOrderDTO>();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ClientOrderDTO> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(List<ClientOrderDTO> clientOrders) {
        this.clientOrders = clientOrders;
    }
}
