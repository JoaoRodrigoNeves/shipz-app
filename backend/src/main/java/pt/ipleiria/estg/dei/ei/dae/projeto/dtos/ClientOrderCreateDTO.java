package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;

import java.util.List;

public class ClientOrderCreateDTO {
    String finalCostumer;
    List<ProductOrderDTO> products;
    public ClientOrderCreateDTO(){

    }
    public ClientOrderCreateDTO(String finalCostumer, List<ProductOrderDTO> products) {
        this.finalCostumer = finalCostumer;
        this.products = products;
    }

    public String getFinalCostumer() {
        return finalCostumer;
    }

    public void setFinalCostumer(String finalCostumer) {
        this.finalCostumer = finalCostumer;
    }

    public List<ProductOrderDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOrderDTO> products) {
        this.products = products;
    }
}
