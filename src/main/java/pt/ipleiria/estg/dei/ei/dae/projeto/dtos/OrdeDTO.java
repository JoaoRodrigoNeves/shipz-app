package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.io.Serializable;

public class OrdeDTO implements Serializable {

    private long id;

    public OrdeDTO(long id) {
        this.id = id;
    }
    public OrdeDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
