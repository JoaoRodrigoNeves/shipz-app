package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;

import java.io.Serializable;
import java.util.List;

public class ClientOrderCreateDTO implements Serializable {
    String finalCostumer;
    String logisticOperator;
    List<ProductOrderDTO> products;
    public ClientOrderCreateDTO(){

    }
    public ClientOrderCreateDTO(String finalCostumer, String logisticOperator, List<ProductOrderDTO> products) {
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
