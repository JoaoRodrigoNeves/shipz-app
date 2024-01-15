package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogisticOperatorDTO implements Serializable {

    private String username;
    private String password;
    private String name;
    private String email;

    private List<ClientOrderDTO> clientOrdersDTO;
    public LogisticOperatorDTO(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


