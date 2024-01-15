package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Observation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "observation_id_seq")
    long id;
    double value;
    @ManyToOne
    @JoinColumn(name = "sensor_code")
    Sensor sensor;
    @Column(name = "created_at")
    Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
    public Observation(){

    }
    public Observation(double value, Sensor sensor) {
        this.value = value;
        this.sensor = sensor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
