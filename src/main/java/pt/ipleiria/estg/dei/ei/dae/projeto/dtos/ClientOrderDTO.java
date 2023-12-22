package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;

public class ClientOrderDTO implements Serializable {

    private long id;

    public ClientOrderDTO(long id) {
        this.id = id;
    }
    public ClientOrderDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
