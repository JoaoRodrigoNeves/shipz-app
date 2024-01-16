package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.ws.rs.Path;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class PackageDTO implements Serializable {

    long code;
    PackageType type;
    String material;
    //Set<Sensor> observations;
    long volume
    String createdAt;

    public PackageDTO() {
    }

    public PackageDTO(long code, PackageType type, String material, long volume, String createdAt) {
        this.code = code;
        this.type = type;
        this.material = material;
        this.volume = volume;
        this.createdAt = createdAt;
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

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
  
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
