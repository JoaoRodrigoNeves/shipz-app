package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

public class ObservationDetailDTO {
    String value;
    long sensorCode;
    String sensorName;
    public ObservationDetailDTO() {

    }

    public ObservationDetailDTO(String value, long sensorCode, String sensorName) {
        this.value = value;
        this.sensorCode = sensorCode;
        this.sensorName = sensorName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(long sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }
}
