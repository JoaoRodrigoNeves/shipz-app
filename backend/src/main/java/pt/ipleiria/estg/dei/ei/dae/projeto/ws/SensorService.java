package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    private SensorDTO toDTONoObservations(Sensor sensor) {
        return new SensorDTO(
                sensor.getCode(),
                sensor.getType().getSensorType()
        );
    }

    private List<SensorDTO> toDTOsNoObservations(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTONoObservations).collect(Collectors.toList());
    }

    public PackageDTO packageDTO(Package p) {
        return new PackageDTO(
                p.getCode(),
                p.getType(),
                p.getMaterial(),
                p.getCreatedAt().toString()
        );
    }

    public List<PackageDTO> packageDTOs(List<Package> packages) {
        return packages.stream().map(this::packageDTO).collect(Collectors.toList());
    }

    //TODO create sensor
    @POST
    @Path("/")
    public Response create(SensorDTO sensorDTO) throws MyEntityExistsException {
        Sensor sensor = sensorBean.create(sensorDTO.getType());
        return Response.status(Response.Status.CREATED).entity(toDTONoObservations(sensor)).build();
    }

    //TODO get sensor details
    @GET
    @Path("{code}")
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.find(code);
        return Response.status(Response.Status.OK).entity(toDTONoObservations(sensor)).build();
    }

    //TODO update sensor
    @PUT
    @Path("/")
    public Response update(SensorDTO sensorDTO) throws MyConstraintViolationException, MyEntityNotFoundException {
        Sensor sensor = sensorBean.update(
                sensorDTO.getCode(),
                sensorDTO.getType()
        );
        return Response.status(Response.Status.OK).entity(toDTONoObservations(sensor)).build();
    }

    //TODO delete sensor
    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.delete(code);
        return Response.status(Response.Status.OK).entity(toDTONoObservations(sensor)).build();
    }

    //TODO get all sensors
    @GET
    @Path("/")
    public List<SensorDTO> getAll() {
        return toDTOsNoObservations(sensorBean.getAll());
    }

    //TODO add sensor to package
    @POST
    @Path("{code}/packages")
    public Response addPackage(@PathParam("code") long code, PackageDTO packageDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        sensorBean.addToPackage(code, packageDTO.getCode());
        return Response.status(Response.Status.OK).build();
    }

    //TODO remove sensor from package
    @DELETE
    @Path("{code}/packages")
    public Response removePackage(@PathParam("code") long code, PackageDTO packageDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        sensorBean.removeFromPackage(code, packageDTO.getCode());
        return Response.status(Response.Status.OK).build();
    }

    //TODO get packages
    @GET
    @Path("{code}/packages")
    public Response getPackages(@PathParam("code") long code) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.getPackages(code);
        List<PackageDTO> packageDTOs = packageDTOs(sensor.getPackages());
        return Response.status(Response.Status.OK).entity(packageDTOs).build();
    }
}
