package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

public class ProductFullDetailsDTO {

    long code;

    long productCatalogCode;
    String productCatalogName;
    String productManufacterUsername;

    long clientOrderCode;


    public ProductFullDetailsDTO() {
    }

    public ProductFullDetailsDTO(long code, long productCatalogCode, String productCatalogName, String productManufacterUsername) {
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
