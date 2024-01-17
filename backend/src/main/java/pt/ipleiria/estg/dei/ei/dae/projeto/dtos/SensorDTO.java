package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;

import java.io.Serializable;
import java.util.List;

public class SensorDTO implements Serializable {

    long code;
    String type;

    public SensorDTO() {

    }

    public SensorDTO(long code, String type) {
        this.code = code;
        this.type = type;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
