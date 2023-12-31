package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductPackageDTO extends PackageDTO implements Serializable {

    List<ProductDTO> products;

    public ProductPackageDTO() {
    }

    public ProductPackageDTO(long code, String type, String material, String status, String manufacturingDate) {
        super(code, type, material, status, manufacturingDate);
        this.products = new ArrayList<ProductDTO>();
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
