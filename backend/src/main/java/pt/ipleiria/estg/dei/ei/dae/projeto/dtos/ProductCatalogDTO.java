package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;

import java.util.List;

public class ProductCatalogDTO {
    long code;
    String name;
    String productManufacterUsername;
    public ProductCatalogDTO() {
    }

    public ProductCatalogDTO(long code, String name, String productManufacterUsername) {
        this.code = code;
        this.name = name;
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

    public String getProductManufacterUsername() {
        return productManufacterUsername;
    }

    public void setProductManufacterUsername(String productManufacterUsername) {
        this.productManufacterUsername = productManufacterUsername;
    }
}
