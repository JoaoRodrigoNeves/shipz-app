package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalogDTO implements Serializable {
    long code;
    String name;
    String catalogArea;
    String category;
    String description;
    String productManufacterUsername;
    Integer maxSecondaryPackage;
    Integer maxTertiaryPackage;
    long primaryPackageVolume;
    String primaryPackageMaterial;
    String secondaryPackageMaterial;
    String tertiaryPackageMaterial;
    List<String> sensors;

    public ProductCatalogDTO() {}

    public ProductCatalogDTO(long code, String name, String catalogArea, String category, String description, String productManufacterUsername, Integer maxSecondaryPackage, Integer maxTertiaryPackage, long primaryPackageVolume, String primaryPackageMaterial, String secondaryPackageMaterial, String tertiaryPackageMaterial) {
        this.code = code;
        this.name = name;
        this.catalogArea = catalogArea;
        this.category = category;
        this.description = description;
        this.productManufacterUsername = productManufacterUsername;
        this.maxSecondaryPackage = maxSecondaryPackage;
        this.maxTertiaryPackage = maxTertiaryPackage;
        this.primaryPackageVolume = primaryPackageVolume;
        this.primaryPackageMaterial = primaryPackageMaterial;
        this.secondaryPackageMaterial = secondaryPackageMaterial;
        this.tertiaryPackageMaterial = tertiaryPackageMaterial;
        this.sensors = new ArrayList<>();
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

    public String getCatalogArea() {
        return catalogArea;
    }

    public void setCatalogArea(String catalogArea) {
        this.catalogArea = catalogArea;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductManufacterUsername() {
        return productManufacterUsername;
    }

    public void setProductManufacterUsername(String productManufacterUsername) {
        this.productManufacterUsername = productManufacterUsername;
    }

    public Integer getMaxSecondaryPackage() {
        return maxSecondaryPackage;
    }

    public void setMaxSecondaryPackage(Integer maxSecondaryPackage) {
        this.maxSecondaryPackage = maxSecondaryPackage;
    }

    public Integer getMaxTertiaryPackage() {
        return maxTertiaryPackage;
    }

    public void setMaxTertiaryPackage(Integer maxTertiaryPackage) {
        this.maxTertiaryPackage = maxTertiaryPackage;
    }

    public long getPrimaryPackageVolume() {
        return primaryPackageVolume;
    }

    public void setPrimaryPackageVolume(long primaryPackageVolume) {
        this.primaryPackageVolume = primaryPackageVolume;
    }

    public String getPrimaryPackageMaterial() {
        return primaryPackageMaterial;
    }

    public void setPrimaryPackageMaterial(String primaryPackageMaterial) {
        this.primaryPackageMaterial = primaryPackageMaterial;
    }

    public String getSecondaryPackageMaterial() {
        return secondaryPackageMaterial;
    }

    public void setSecondaryPackageMaterial(String secondaryPackageMaterial) {
        this.secondaryPackageMaterial = secondaryPackageMaterial;
    }

    public String getTertiaryPackageMaterial() {
        return tertiaryPackageMaterial;
    }

    public void setTertiaryPackageMaterial(String tertiaryPackageMaterial) {
        this.tertiaryPackageMaterial = tertiaryPackageMaterial;
    }

    public List<String> getSensors() {
        return sensors;
    }

    public void setSensors(List<String> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(String sensor) {
        this.sensors.add(sensor);
    }
}
