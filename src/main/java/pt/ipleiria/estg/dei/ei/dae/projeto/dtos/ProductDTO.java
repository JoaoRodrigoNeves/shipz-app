package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;

import java.util.Date;

public class ProductDTO {

    long code;

    String name;

    long packageCode;

    Date createdAt;

    Date updatedAt;

    public ProductDTO() {
    }

    public ProductDTO(long code, String name, long packageCode) {
        this.code = code;
        this.name = name;
        this.packageCode = packageCode;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(long packageCode) {
        this.packageCode = packageCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = new Date();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }
}
