package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDTO {

    long code;

    String name;

    List<ProductPackage> productPackages;

    public ProductDTO() {
    }

    public ProductDTO(long code, String name) {
        this.code = code;
        this.name = name;
        this.productPackages = new ArrayList<>();
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

    public List<ProductPackage> getProductPackages() {
        return productPackages;
    }

    public void setProductPackages(List<ProductPackage> productPackages) {
        this.productPackages = productPackages;
    }
}
