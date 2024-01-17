package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.TransportPackageCatalogDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.TransportPackageCatalogBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackageCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.ListNotEmptyException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("transport-package-catalogs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TransportPackageCatalogService {
    @EJB
    private TransportPackageCatalogBean transportPackageCatalogBean;

    private TransportPackageCatalogDTO toDTO(TransportPackageCatalog transportPackageCatalog) {
        return new TransportPackageCatalogDTO(
                transportPackageCatalog.getCode(),
                transportPackageCatalog.getName(),
                transportPackageCatalog.getMaterial(),
                transportPackageCatalog.getVolume());
    }

    private List<TransportPackageCatalogDTO> toDTOs(List<TransportPackageCatalog> transportPackageCatalog) {
        return transportPackageCatalog.stream().map(this::toDTO).collect(Collectors.toList());
    }

    //TODO create transport Package Catalog
    @POST
    @Path("/")
    public Response create(TransportPackageCatalogDTO transportPackageCatalogDTO) throws MyEntityExistsException {
        TransportPackageCatalog transportPackageCatalog = transportPackageCatalogBean.create(transportPackageCatalogDTO.getName(), transportPackageCatalogDTO.getMaterial(), transportPackageCatalogDTO.getVolume());
        return Response.status(Response.Status.CREATED).entity(toDTO(transportPackageCatalog)).build();
    }

    //TODO get transport Package Catalog details
    @GET
    @Path("{code}")
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackageCatalog transportPackageCatalog = transportPackageCatalogBean.find(code);
        return Response.status(Response.Status.OK).entity(toDTO(transportPackageCatalog)).build();
    }

    //TODO delete transport Package Catalog
    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException, ListNotEmptyException {
        transportPackageCatalogBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
    //TODO get all transport Package Catalog
    @GET
    @Path("/")
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(toDTOs(transportPackageCatalogBean.getAll())).build();
    }
}
