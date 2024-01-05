package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;

import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    private SensorDTO toDTONoObservations(Sensor sensor) {
        return new SensorDTO(
                sensor.getCode(),
                sensor.getType(),
                sensor.getType().getSensorType()
        );
    }

    private List<SensorDTO> toDTOsNoObservations(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTONoObservations).collect(Collectors.toList());
    }
    @GET
    @Path("/")
    public List<SensorDTO> getAll() {
        return toDTOsNoObservations(sensorBean.getAll());
    }
}
