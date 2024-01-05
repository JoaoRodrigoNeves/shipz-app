package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientOrderDTO implements Serializable {
    private long code;
    private String logisticOperator;
    private String finalCostumer;
    private List<ProductDTO> productsDTO;

    public ClientOrderDTO(long code) {
        this.code = code;
        this.productsDTO = new ArrayList<>();
    }

    public ClientOrderDTO() {
    }

    public long getCode() {
        return code;
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

    public List<ProductDTO> getProductsDTO() {
        return productsDTO;
    }

    public void setProductsDTO(List<ProductDTO> productsDTO) {
        this.productsDTO = productsDTO;
    }
}
