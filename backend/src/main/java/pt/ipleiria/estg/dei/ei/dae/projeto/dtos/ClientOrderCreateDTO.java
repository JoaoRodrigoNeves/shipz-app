package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;

import java.util.List;

public class ClientOrderCreateDTO {
    String finalCostumer;
    List<Long> products;
    public ClientOrderCreateDTO(){

    }
    public ClientOrderCreateDTO(String finalCostumer, List<Long> products) {
        this.finalCostumer = finalCostumer;
        this.products = products;
    }

    public String getFinalCostumer() {
        return finalCostumer;
    }

    public void setFinalCostumer(String finalCostumer) {
        this.finalCostumer = finalCostumer;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }
}
