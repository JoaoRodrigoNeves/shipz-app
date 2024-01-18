package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;

public class OrderListDTO extends OrderDTO implements Serializable {
    private long productQuantity;

    public OrderListDTO() {
    }

    public OrderListDTO(long code, String logisticOperatorName, String status, long productQuantity) {
        super(code, logisticOperatorName, status);
        this.productQuantity = productQuantity;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }
}
