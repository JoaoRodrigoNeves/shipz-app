package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;

import java.io.Serializable;
import java.util.List;

public class SensorDTO implements Serializable {

    long code;
    String type;
    boolean inUse;

    public SensorDTO() {

    }

    public SensorDTO(long code, String type, boolean inUse) {
        this.code = code;
        this.type = type;
        this.inUse = inUse;
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

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
