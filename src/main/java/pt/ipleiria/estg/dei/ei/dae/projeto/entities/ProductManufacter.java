package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.UserType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductManufacters",
                query = "SELECT p FROM ProductManufacter p" // JPQL
        )
})
@Table(name="productManufacters")
public class ProductManufacter extends User implements Serializable {

    public ProductManufacter() {
    }

    public ProductManufacter(String username, String password, String name, UserType userType, String email) {
        super(username, password, name, userType, email);
    }


}
