package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductOrderDTO implements Serializable {

    long code;
    long quantity;
    String name;
    String catalogArea;
    String category;
    String description;
    String productManufacterName;
    String productManufacterUsername;
    long primaryPackageVolume;
    Integer maxSecondaryPackage;
    Integer maxTertiaryPackage;
    String primaryPackageMaterial;
    String secondaryPackageMaterial;
    String tertiaryPackageMaterial;
    List<String> sensors;

    public ProductOrderDTO() {
        this.sensors = new ArrayList<>();
    }

    public ProductOrderDTO(long code, long quantity, String name, String catalogArea, String category, String description, String productManufacterName, String productManufacterUsername, Integer maxSecondaryPackage, Integer maxTertiaryPackage, String primaryPackageMaterial, String secondaryPackageMaterial, String tertiaryPackageMaterial, long primaryPackageVolume) {
        this.code = code;
        this.quantity = quantity;
        this.name = name;
        this.catalogArea = catalogArea;
        this.category = category;
        this.description = description;
        this.productManufacterName = productManufacterName;
        this.productManufacterUsername = productManufacterUsername;
        this.maxSecondaryPackage = maxSecondaryPackage;
        this.maxTertiaryPackage = maxTertiaryPackage;
        this.primaryPackageMaterial = primaryPackageMaterial;
        this.secondaryPackageMaterial = secondaryPackageMaterial;
        this.tertiaryPackageMaterial = tertiaryPackageMaterial;
        this.primaryPackageVolume = primaryPackageVolume;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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

    public String getProductManufacterName() {
        return productManufacterName;
    }

    public void setProductManufacterName(String productManufacterName) {
        this.productManufacterName = productManufacterName;
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

    public long getPrimaryPackageVolume() {
        return primaryPackageVolume;
    }

    public void setPrimaryPackageVolume(long primaryPackageVolume) {
        this.primaryPackageVolume = primaryPackageVolume;
    }

    public List<String> getSensors() {
        return sensors;
    }

    public void setSensors(List<String> sensors) {
        this.sensors = sensors;
    }
}
