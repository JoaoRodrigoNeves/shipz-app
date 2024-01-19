package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.TransportPackageBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("transport-packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TransportPackageService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private TransportPackageBean transportPackageBean;

    @EJB
    private SensorBean sensorBean;

    private TransportPackageDTO transportPackageToDTO(TransportPackage transportPackage) {
        TransportPackageDTO transportPackageDTO = new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getType(),
                transportPackage.getMaterial(),
                transportPackage.getVolume(),
                transportPackage.getCreatedAt().toString()
        );
        if (transportPackage.getLocation() != null)
            transportPackageDTO.setLocation(transportPackageDTO.getLocation());
        return transportPackageDTO;
    }

    private List<TransportPackageDTO> transportPackageToDTOs(List<TransportPackage> transportPackages) {
        return transportPackages.stream()
                .map(this::transportPackageToDTO)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response create(TransportPackageCreateDTO transportPackageCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        boolean dontNeedAddPackage = transportPackageBean.create(transportPackageCreateDTO);
        return dontNeedAddPackage ? Response.status(Response.Status.ACCEPTED).build() : Response.status(Response.Status.CREATED).build();
    }
    //TODO get transport-package details
    @GET
    @Path("{code}")
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = transportPackageBean.find(code);
        return Response.status(Response.Status.OK).entity(transportPackageToDTO(transportPackage)).build();
    }

    @GET
    @Path("{code}/sensors")
    public Response getSensors(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = transportPackageBean.getSensors(code);
        List<SensorDTO> sensorDTOs = sensorDTOs(transportPackage.getSensors());
        return Response.status(Response.Status.OK).entity(sensorDTOs).build();
    }

    private List<SensorDTO> sensorDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::sensorDTO).collect(Collectors.toList());
    }

    private SensorDTO sensorDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getCode(),
                sensor.getType().getSensorType(),
                sensor.isInUse()
        );
    }

    //TODO get all transport-packages
    @GET
    @Path("/")
    public List<TransportPackageDTO> getAll() {
        return transportPackageToDTOs(transportPackageBean.getAll());
    }
    //TODO delete transport-package
    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        transportPackageBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("{code}/add-sensors")
    public Response addSensor(@PathParam("code") long code, TransportPackageAddSensorDTO transportPackageAddSensorDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        transportPackageAddSensorDTO.getSensors().forEach(s -> {
            try {
                Sensor sensor = sensorBean.getSensor(s);
                if (sensor == null){
                    sensor = sensorBean.create(s, true);
                }
                sensorBean.addToPackage(sensor.getCode(), code);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return Response.status(Response.Status.OK).build();
    }
}
