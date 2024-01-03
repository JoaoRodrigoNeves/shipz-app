package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sensor_id_seq")
    long id;
    String type;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    @NotNull
    List<Observation> observations;

    public Sensor(){

    }
    public Sensor(String type) {
        this.type = type;
        this.observations = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}