package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.util.List;

public class LogisticOperatorDTO extends User implements Serializable {
    private List<ClientOrderDTO> orders;
    public LogisticOperatorDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    public LogisticOperatorDTO() {
    }

    public List<ClientOrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<ClientOrderDTO> orders) {
        this.orders = orders;
    }
}