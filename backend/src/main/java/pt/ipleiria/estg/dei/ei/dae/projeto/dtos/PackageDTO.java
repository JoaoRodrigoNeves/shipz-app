package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.ws.rs.Path;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;

import java.io.Serializable;
import java.util.Date;

public class PackageDTO implements Serializable {

    long code;

    String type;

    String material;

    //Set<Sensor> observations;

    // data de fabrico
    String manufacturingDate;

    //QualityControl qualityControlData;

    public PackageDTO() {
    }

    public PackageDTO(long code, String type, String material, String manufacturingDate) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
