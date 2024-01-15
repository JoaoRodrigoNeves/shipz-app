package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;

public class ObservationDTO {
    long id;
    double value;
    long sensorCode;
    public ObservationDTO() {

    }
    public ObservationDTO(long sensorCode, double value) {
        this.sensorCode = sensorCode;
        this.value = value;
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
}
