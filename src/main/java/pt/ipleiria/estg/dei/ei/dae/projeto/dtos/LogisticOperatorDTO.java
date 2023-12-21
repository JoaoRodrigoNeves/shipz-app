package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogisticOperatorDTO extends User implements Serializable {
    private List<OrdeDTO> ordes;
    public LogisticOperatorDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    public LogisticOperatorDTO() {
    }

    public List<OrdeDTO> getOrdes() {
        return ordes;
    }

    public void setOrdes(List<OrdeDTO> ordes) {
        this.ordes = ordes;
    }
}
