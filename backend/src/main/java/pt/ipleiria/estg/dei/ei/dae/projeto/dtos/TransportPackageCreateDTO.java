package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransportPackageCreateDTO extends TransportPackageDTO implements Serializable {


    long quantity;

    public TransportPackageCreateDTO() {
    }

    public TransportPackageCreateDTO(long code, PackageType type, String material, long volume, String createdAt, long quantity) {
        super(code, type, material, volume, createdAt);
        this.clientOrders = new ArrayList<ClientOrderDTO>();
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
