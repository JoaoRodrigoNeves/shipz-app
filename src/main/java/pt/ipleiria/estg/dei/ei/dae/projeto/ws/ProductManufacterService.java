package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductManufacterDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductManufacterBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.UserType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("productsManufacters")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductsManufacters", "Administrator"})
public class ProductManufacterService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private ProductManufacterBean productManufacterBean;

    private ProductManufacterDTO productManufacterToDTONoPackages(ProductManufacter productManufacter) {
        return new ProductManufacterDTO(
                productManufacter.getUsername(),
                productManufacter.getName(),
                productManufacter.getUserType(),
                productManufacter.getEmail()
        );
    }

    private List<ProductManufacterDTO> productManufacterToDTOsNoPackages(List<ProductManufacter> productManufacters) {
        return productManufacters.stream()
                .map(this::productManufacterToDTONoPackages)
                .collect(Collectors.toList());
    }




    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<ProductManufacterDTO> getAllStudents() {
        return productManufacterToDTOsNoPackages(productManufacterBean.getAll());
    }

    @POST
    @Path("/")
    public Response create(ProductManufacterDTO productManufacterDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        productManufacterBean.create(
                productManufacterDTO.getUsername(),
                productManufacterDTO.getPassword(),
                productManufacterDTO.getName(),
                productManufacterDTO.getEmail(),
                UserType.PRODUCT_MANUFACTER
        );
        ProductManufacter productManufacter = productManufacterBean.find(productManufacterDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(productManufacterToDTONoPackages(productManufacter)).build();
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"Student"})
    public Response getStudentDetails(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        ProductManufacter productManufacter = productManufacterBean.find(username);
        if (productManufacter != null) {
            return Response.ok(productManufacterToDTONoPackages(productManufacter)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUCT_MANUFACTER")
                .build();
    }
}