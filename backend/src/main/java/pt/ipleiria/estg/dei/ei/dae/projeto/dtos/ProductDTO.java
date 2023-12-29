package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;

public class ProductDTO implements Serializable {

    long code;

    long productCatalogCode;

    String productCatalogName;

    String productManufacterUsername;

    long clientOrderCode;

    public ProductDTO() {
    }

    public ProductDTO(long code, long productCatalogCode, String productCatalogName, String productManufacterUsername) {
        this.code = code;
        this.productCatalogCode = productCatalogCode;
        this.productCatalogName = productCatalogName;
        this.productManufacterUsername = productManufacterUsername;
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

    public String getProductManufacterUsername() {
        return productManufacterUsername;
    }

    public void setProductManufacterUsername(String productManufacterUsername) {
        this.productManufacterUsername = productManufacterUsername;
    }

    public long getClientOrderCode() {
        return clientOrderCode;
    }

    public void setClientOrderCode(long clientOrderCode) {
        this.clientOrderCode = clientOrderCode;
    }
}
