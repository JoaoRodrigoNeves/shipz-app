package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductPackageDTO extends PackageDTO implements Serializable {

    List<ProductDTO> products;

    public ProductPackageDTO() {
    }

    public ProductPackageDTO(long code, PackageType type, String material, LocalDateTime manufacturingDate) {
        super(code, type, material, manufacturingDate);
        this.products = new ArrayList<ProductDTO>();
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
