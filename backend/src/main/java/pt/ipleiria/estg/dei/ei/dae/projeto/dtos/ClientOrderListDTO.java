package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientOrderListDTO implements Serializable {
    private long code;
    private String logisticOperator;
    private String finalCostumer;
    private List<ProductDTO> productsDTO;
    private long productQuantity;
    private String orderStatus;

    public ClientOrderListDTO(long code, long productQuantity, String orderStatus) {
        this.code = code;
        this.productsDTO = new ArrayList<>();
        this.productQuantity = productQuantity;
        this.orderStatus = orderStatus;
    }

    public ClientOrderListDTO() {
    }

    public long getCode() {
        return code;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getLogisticOperator() {
        return logisticOperator;
    }

    public void setLogisticOperator(String logisticOperator) {
        this.logisticOperator = logisticOperator;
    }

    public void setFinalCostumer(String finalCostumer) {
        this.finalCostumer = finalCostumer;
    }

    public List<ProductDTO> getProductsDTO() {
        return productsDTO;
    }

    public void setProductsDTO(List<ProductDTO> productsDTO) {
        this.productsDTO = productsDTO;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }
}
