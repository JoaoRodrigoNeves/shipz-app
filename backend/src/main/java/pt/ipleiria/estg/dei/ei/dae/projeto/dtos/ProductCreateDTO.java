package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;

public class ProductCreateDTO extends ProductDTO implements Serializable {

    long quantity;

    public ProductCreateDTO() {
    }

    public ProductCreateDTO(long code, long productCatalogCode, String productCatalogName, String productManufacterName, long quantity) {
        super(code, productCatalogCode, productCatalogName, productManufacterName);
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
