package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.TransportPackageCatalogDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.TransportPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.TransportPackageCatalogBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackageCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.ListNotEmptyException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("transport-package-catalogs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter", "LogisticOperator", "FinalCostumer"})
public class TransportPackageCatalogService {
    @EJB
    private TransportPackageCatalogBean transportPackageCatalogBean;

    private TransportPackageCatalogDTO toDTO(TransportPackageCatalog transportPackageCatalog) {
        TransportPackageCatalogDTO transportPackageCatalogDTO = new TransportPackageCatalogDTO(
                transportPackageCatalog.getCode(),
                transportPackageCatalog.getName(),
                transportPackageCatalog.getMaterial(),
                transportPackageCatalog.getVolume());

        transportPackageCatalogDTO.setTransportPackageDTOList(transportPackagesToDTOs(transportPackageCatalog.getTransportPackages()));

        return transportPackageCatalogDTO;
    }

    private List<TransportPackageCatalogDTO> toDTOs(List<TransportPackageCatalog> transportPackageCatalog) {
        return transportPackageCatalog.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private TransportPackageCatalogDTO toNoPackageDTO(TransportPackageCatalog transportPackageCatalog) {
        return new TransportPackageCatalogDTO(
                transportPackageCatalog.getCode(),
                transportPackageCatalog.getName(),
                transportPackageCatalog.getMaterial(),
                transportPackageCatalog.getVolume());
    }

    private List<TransportPackageCatalogDTO> toNoPackageDTOs(List<TransportPackageCatalog> transportPackageCatalog) {
        return transportPackageCatalog.stream().map(this::toNoPackageDTO).collect(Collectors.toList());
    }

    private TransportPackageDTO transportPackagesToDTO(TransportPackage transportPackage) {
        return new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getType(),
                transportPackage.getMaterial(),
                transportPackage.getVolume(),
                transportPackage.getCreatedAt().toString()
        );

    }

    private List<TransportPackageDTO> transportPackagesToDTOs(List<TransportPackage> transportPackages) {
        return transportPackages.stream().map(this::transportPackagesToDTO).collect(Collectors.toList());
    }

    //TODO create transport Package Catalog
    @POST
    @Path("/")
    @RolesAllowed({"ProductManufacter"})
    public Response create(TransportPackageCatalogDTO transportPackageCatalogDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        TransportPackageCatalog transportPackageCatalog = transportPackageCatalogBean.create(transportPackageCatalogDTO.getName(), transportPackageCatalogDTO.getMaterial(), transportPackageCatalogDTO.getVolume());
        return Response.status(Response.Status.CREATED).entity(toDTO(transportPackageCatalog)).build();
    }

    //TODO get transport Package Catalog details
    @GET
    @Path("{code}")
    @RolesAllowed({"ProductManufacter"})
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackageCatalog transportPackageCatalog = transportPackageCatalogBean.find(code);
        return Response.status(Response.Status.OK).entity(toDTO(transportPackageCatalog)).build();
    }

    //TODO delete transport Package Catalog
    @DELETE
    @Path("{code}")
    @RolesAllowed({"ProductManufacter"})
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException, ListNotEmptyException {
        transportPackageCatalogBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    //TODO get all transport Package Catalog
    @GET
    @Path("/")
    @RolesAllowed({"ProductManufacter", "LogisticOperator", "FinalCostumer"})
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(toNoPackageDTOs(transportPackageCatalogBean.getAll())).build();
    }
}
