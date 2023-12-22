package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllFinalCostumers",
                query = "SELECT fc from FinalCostumer fc ORDER BY fc.name"
        ),
})
public class FinalCostumer extends User implements Serializable {
    String address;
    public FinalCostumer() {
    }

    public FinalCostumer(String username, String password, String name, String email, String address) {
        super(username, password, name, email);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
