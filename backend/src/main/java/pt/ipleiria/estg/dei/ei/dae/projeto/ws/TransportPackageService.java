package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.TransportPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.TransportPackageBean;
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

    private TransportPackageDTO transportPackageToDTO(TransportPackage transportPackage) {
        return new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getType(),
                transportPackage.getMaterial(),
                transportPackage.getVolume(),
                transportPackage.getLocation(),
                transportPackage.getCreatedAt().toString()
        );
    }

    private List<TransportPackageDTO> transportPackageToDTOs(List<TransportPackage> transportPackages) {
        return transportPackages.stream()
                .map(this::transportPackageToDTO)
                .collect(Collectors.toList());
    }

    //TODO create a transport-package
    @POST
    @Path("/")
    public Response create(TransportPackageDTO transportPackageDTO) throws MyEntityExistsException {
        TransportPackage transportPackage = transportPackageBean.create(
                transportPackageDTO.getMaterial(),
                transportPackageDTO.getVolume(),
                transportPackageDTO.getLocation()
        );
        return Response.status(Response.Status.OK).entity(transportPackageToDTO(transportPackage)).build();
    }

    //TODO get transport-package details
    @GET
    @Path("{code}")
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = transportPackageBean.find(code);
        return Response.status(Response.Status.OK).entity(transportPackageToDTO(transportPackage)).build();
    }

    //TODO get all transport-packages
    @GET
    @Path("/")
    public List<TransportPackageDTO> getAll() {
        return transportPackageToDTOs(transportPackageBean.getAll());
    }
}
