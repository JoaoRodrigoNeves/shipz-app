package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.util.List;

public class OrderCreateDTO implements Serializable {
    String finalCostumer;
    String logisticOperator;
    List<ProductOrderDTO> products;
    public OrderCreateDTO(){

    }
    public OrderCreateDTO(String finalCostumer, String logisticOperator, List<ProductOrderDTO> products) {
        this.finalCostumer = finalCostumer;
        this.logisticOperator = logisticOperator;
        this.products = products;
    }

    public String getFinalCostumer() {
        return finalCostumer;
    }

    public void setFinalCostumer(String finalCostumer) {
        this.finalCostumer = finalCostumer;
    }

    public String getLogisticOperator() {
        return logisticOperator;
    }

    public void setLogisticOperator(String logisticOperator) {
        this.logisticOperator = logisticOperator;
    }

    public List<ProductOrderDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOrderDTO> products) {
        this.products = products;
    }
}
