package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.util.List;

public class TransportPackageAddSensorDTO {

    List<String> sensors;

    public TransportPackageAddSensorDTO(List<String> sensors) {
        this.sensors = sensors;
    }

    public TransportPackageAddSensorDTO() {
    }

    public List<String> getSensors() {
        return sensors;
    }

    public void setSensors(List<String> sensors) {
        this.sensors = sensors;
    }
}
