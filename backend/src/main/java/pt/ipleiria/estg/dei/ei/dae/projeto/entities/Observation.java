package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Observation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "observation_id_seq")
    long id;
    String value;
    @ManyToOne
    @JoinColumn(name = "sensor_code")
    Sensor sensor;
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    public Observation(){

    }
    public Observation(String value, Sensor sensor) {
        this.value = value;
        this.sensor = sensor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
