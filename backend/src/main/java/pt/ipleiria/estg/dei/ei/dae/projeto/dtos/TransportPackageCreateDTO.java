package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.util.ArrayList;

public class TransportPackageCreateDTO implements Serializable {
    long orderCode;
    long transportPackageCatalogCode;

    public TransportPackageCreateDTO() {
    }

    public TransportPackageCreateDTO(long orderCode, long transportPackageCatalogCode) {
        this.orderCode = orderCode;
        this.transportPackageCatalogCode = transportPackageCatalogCode;
    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public long getTransportPackageCatalogCode() {
        return transportPackageCatalogCode;
    }

    public void setTransportPackageCatalogCode(long transportPackageCatalogCode) {
        this.transportPackageCatalogCode = transportPackageCatalogCode;
    }
}
