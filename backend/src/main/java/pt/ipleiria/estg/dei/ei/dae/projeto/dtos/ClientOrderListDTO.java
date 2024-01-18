package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientOrderListDTO extends ClientOrderDTO implements Serializable {
    private long productQuantity;

    public ClientOrderListDTO() {
    }

    public ClientOrderListDTO(long code, String logisticOperatorName, String status, long productQuantity) {
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
