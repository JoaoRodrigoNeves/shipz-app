package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;

import java.util.List;

public class ProductCatalogDTO {
    long code;
    String name;
    List<ProductDTO> productsDTO;
    ProductManufacter productManufacter;
    public ProductCatalogDTO() {
    }

    public ProductCatalogDTO(long code, String name, ProductManufacter productManufacter) {
        this.code = code;
        this.name = name;
        this.productManufacter = productManufacter;
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

    public List<ProductDTO> getProductsDTO() {
        return productsDTO;
    }

    public void setProductsDTO(List<ProductDTO> productsDTO) {
        this.productsDTO = productsDTO;
    }

    public ProductManufacter getProductManufacter() {
        return productManufacter;
    }

    public void setProductManufacter(ProductManufacter productManufacter) {
        this.productManufacter = productManufacter;
    }
}
