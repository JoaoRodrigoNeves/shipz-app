package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.FinalCostumerDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.FinalCostumerBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.FinalCostumer;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("finalCostumers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"FinalCostumers", "Administrator"})
public class FinalCostumerService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private FinalCostumerBean finalCostumerBean;

    private FinalCostumerDTO toDTO(FinalCostumer finalCostumer) {
        return new FinalCostumerDTO(
                finalCostumer.getUsername(),
                finalCostumer.getName(),
                finalCostumer.getEmail()
        );
    }

    private List<FinalCostumerDTO> toDTOs(List<FinalCostumer> finalCostumers) {
        return finalCostumers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<FinalCostumerDTO> getAll() {
        return toDTOs(finalCostumerBean.getAll());
    }

    @POST
    @Path("/")
    public Response create(FinalCostumerDTO finalCostumerDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        finalCostumerBean.create(
                finalCostumerDTO.getUsername(),
                finalCostumerDTO.getPassword(),
                finalCostumerDTO.getName(),
                finalCostumerDTO.getEmail(),
                finalCostumerDTO.getAddress());
        FinalCostumer finalCostumer = finalCostumerBean.find(finalCostumerDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(toDTO(finalCostumer)).build();
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"FinalCostumers"})
    public Response getFinalCostumerDetails(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        FinalCostumer finalCostumer = finalCostumerBean.find(username);
        if (finalCostumer != null) {
            return Response.ok(toDTO(finalCostumer)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_FINAL_COSTUMER")
                .build();
    }
}
