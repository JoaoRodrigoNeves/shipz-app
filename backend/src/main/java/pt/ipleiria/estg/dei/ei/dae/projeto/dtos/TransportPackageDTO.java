package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransportPackageDTO extends PackageDTO implements Serializable {

    public TransportPackageDTO() {
    }

    List<ClientOrderDTO> clientOrders;

    public TransportPackageDTO(long code, PackageType type, String material, long volume, String createdAt) {
        super(code, type, material, volume, createdAt);
        this.clientOrders = new ArrayList<ClientOrderDTO>();
    }

    public List<ClientOrderDTO> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(List<ClientOrderDTO> clientOrders) {
        this.clientOrders = clientOrders;
    }
}
