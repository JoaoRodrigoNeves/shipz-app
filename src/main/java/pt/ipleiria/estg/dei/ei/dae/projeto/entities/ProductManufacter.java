package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="productManufacters")
public class ProductManufacter extends User implements Serializable {

}
