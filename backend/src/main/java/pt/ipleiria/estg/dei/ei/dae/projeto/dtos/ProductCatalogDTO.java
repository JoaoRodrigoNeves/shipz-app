package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;

import java.io.Serializable;
import java.util.List;

public class ProductCatalogDTO implements Serializable {
    long code;
    String name;
    String catalogArea;
    String category;
    String description;
    String productManufacterUsername;
    public ProductCatalogDTO() {
    }

    public ProductCatalogDTO(long code, String name, String catalogArea, String category, String description, String productManufacterUsername) {
        this.code = code;
        this.name = name;
        this.catalogArea = catalogArea;
        this.category = category;
        this.description = description;
        this.productManufacterUsername = productManufacterUsername;
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
}
