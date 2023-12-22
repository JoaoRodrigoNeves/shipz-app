package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogisticOperatorDTO extends User implements Serializable {

    private List<ClientOrderDTO> clientOrdersDTO;
    public LogisticOperatorDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.clientOrdersDTO = new ArrayList<>();
    }

    public LogisticOperatorDTO() {
    }

    public List<ClientOrderDTO> getClientOrdersDTO() {
        return clientOrdersDTO;
    }

    public void setClientOrdersDTO(List<ClientOrderDTO> clientOrdersDTO) {
        this.clientOrdersDTO = clientOrdersDTO;
    }
}
