package pt.ipleiria.estg.dei.ei.dae.projeto.entities.types;

public enum SensorType {
    TEMPERATURE("Temperatura"),
    HUMIDITY("Humidade"),
    PRESSURE("Pressão");
    private final String sensorType;

    SensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorType() {
        return sensorType;
    }
}
