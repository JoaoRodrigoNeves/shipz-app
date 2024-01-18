package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientOrderDTO implements Serializable {
    private long code;
    private String logisticOperator;
    private String logisticOperatorName;
    private String finalCostumer;
    private String finalCostumerName;
    private String location;
    private String status;
    private String createdAt;
    private String deliveredAt;
    private ArrayList<ProductDTO> products;

    public ClientOrderDTO() {
        this.products = new ArrayList<ProductDTO>();
    }

    public ClientOrderDTO(long code, String logisticOperatorName, String status) {
        this.code = code;
        this.logisticOperatorName = logisticOperatorName;
        this.status = status;
        this.products = new ArrayList<ProductDTO>();
    }

    public ClientOrderDTO(long code, String location, String status, String createdAt) {
        this.code = code;
        this.status = status;
        this.location = location;
        this.createdAt = createdAt;
        this.products = new ArrayList<ProductDTO>();
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

    public String getLogisticOperatorName() {
        return logisticOperatorName;
    }

    public void setLogisticOperatorName(String logisticOperatorName) {
        this.logisticOperatorName = logisticOperatorName;
    }

    public String getFinalCostumer() {
        return finalCostumer;
    }

    public void setFinalCostumer(String finalCostumer) {
        this.finalCostumer = finalCostumer;
    }

    public String getFinalCostumerName() {
        return finalCostumerName;
    }

    public void setFinalCostumerName(String finalCostumerName) {
        this.finalCostumerName = finalCostumerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDTO> products) {
        this.products = products;
    }
}
