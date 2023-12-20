package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.ws.rs.Path;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;

import java.util.Date;

public class PackageDTO {

    long code;

    String type;

    String material;

    Product product;

    //Set<SensorData> observations;

    String status;

    // data de fabrico
    Date manufactoringDate;

    //QualityControl qualityControlData;

    Date createdAt;

    Date updatedAt;

    public PackageDTO() {
    }

    public PackageDTO(long code, String type, String material, Product product, String status, Date manufactoringDate) {
        this.code = code;
        this.type = type;
        this.material = material;
        this.product = product;
        this.status = status;
        this.manufactoringDate = manufactoringDate;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getManufactoringDate() {
        return manufactoringDate;
    }

    public void setManufactoringDate(Date manufactoringDate) {
        this.manufactoringDate = manufactoringDate;
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
