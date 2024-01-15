package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

public class ObservationDetailDTO {
    double value;
    long sensorCode;
    String sensorName;
    public ObservationDetailDTO() {

    }

    public ObservationDetailDTO(double value, long sensorCode, String sensorName) {
        this.value = value;
        this.sensorCode = sensorCode;
        this.sensorName = sensorName;
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

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }
}
