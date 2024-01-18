package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;

import java.time.LocalDateTime;

public class ObservationDTO {
    double value;
    long sensorCode;

    String createdAt;

    public ObservationDTO() {

    }

    public ObservationDTO( double value, long sensorCode , String createdAt) {
        this.value = value;
        this.sensorCode = sensorCode;
        this.createdAt = createdAt;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(long sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
