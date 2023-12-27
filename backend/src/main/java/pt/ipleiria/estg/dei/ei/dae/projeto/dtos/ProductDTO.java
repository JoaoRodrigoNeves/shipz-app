package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDTO {

    long code;

    long productCatalogCode;

    String productManufacterUsername;

    long clientOrderCode;


    public ProductDTO() {
    }

    public ProductDTO(long code, long productCatalogCode, String productManufacterUsername) {
        this.code = code;
        this.productCatalogCode = productCatalogCode;
        this.productManufacterUsername = productManufacterUsername;
    }

    public ProductDTO(long code, long productCatalogCode, String productManufacterUsername, long clientOrderCode) {
        this.code = code;
        this.productCatalogCode = productCatalogCode;
        this.productManufacterUsername = productManufacterUsername;
        this.clientOrderCode = clientOrderCode;
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

    public String  setProductManufacterUsername() {
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
