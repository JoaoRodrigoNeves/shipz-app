package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;

public class ProductDTO implements Serializable {

    long code;

    long productCatalogCode;

    String productCatalogName;

    String productManufacterName;

    long clientOrderCode;

    public ProductDTO() {
    }

    public ProductDTO(long code, long productCatalogCode, String productCatalogName, String productManufacterName) {
        this.code = code;
        this.productCatalogCode = productCatalogCode;
        this.productCatalogName = productCatalogName;
        this.productManufacterName = productManufacterName;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getProductCatalogCode() {
        return productCatalogCode;
    }

    public void setProductCatalogCode(long productCatalogCode) {
        this.productCatalogCode = productCatalogCode;
    }

    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
    }

    public String getProductManufacterName() {
        return productManufacterName;
    }

    public void setProductManufacterName(String productManufacterName) {
        this.productManufacterName = productManufacterName;
    }

    public long getClientOrderCode() {
        return clientOrderCode;
    }

    public void setClientOrderCode(long clientOrderCode) {
        this.clientOrderCode = clientOrderCode;
    }
}
