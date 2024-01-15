package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.ws.rs.Path;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.util.Date;

public class PackageDTO implements Serializable {

    long code;

    PackageType type;

    String material;

    //Set<Sensor> observations;

    // data de fabrico
    Date manufacturingDate;

    //QualityControl qualityControlData;

    public PackageDTO() {
    }

    public PackageDTO(long code, PackageType type, String material, Date manufacturingDate) {
        this.code = code;
        this.type = type;
        this.material = material;
        this.manufacturingDate = manufacturingDate;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }
}
