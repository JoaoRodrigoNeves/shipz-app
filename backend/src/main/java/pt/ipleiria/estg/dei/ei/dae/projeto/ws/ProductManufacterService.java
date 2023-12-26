package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductCatalogDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductManufacterDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductManufacterBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("product-manufacters")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter", "Administrator"})
public class ProductManufacterService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private ProductManufacterBean productManufacterBean;

    private ProductManufacterDTO productManufacterToDTONoPackages(ProductManufacter productManufacter) {
        return new ProductManufacterDTO(
                productManufacter.getUsername(),
                productManufacter.getPassword(),
                productManufacter.getName(),
                productManufacter.getEmail()
        );
    }

    private List<ProductManufacterDTO> productManufacterToDTOsNoPackages(List<ProductManufacter> productManufacters) {
        return productManufacters.stream()
                .map(this::productManufacterToDTONoPackages)
                .collect(Collectors.toList());
    }

    private ProductCatalogDTO productCatalogToDTO(ProductCatalog productCatalog) {
        return new ProductCatalogDTO(
                productCatalog.getCode(),
                productCatalog.getName(),
                productCatalog.getProductManufacter().getUsername()
        );
    }

    private List<ProductCatalogDTO> productCatalogToDTOs(List<ProductCatalog> productCatalog) {
        return productCatalog.stream()
                .map(this::productCatalogToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/") // means: the relative url path is “/api/product-manufacters/”
    public List<ProductManufacterDTO> getAllProductManufacters() {
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
                productManufacterDTO.getEmail()
        );
        ProductManufacter productManufacter = productManufacterBean.find(productManufacterDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(productManufacterToDTONoPackages(productManufacter)).build();
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"ProductManufacter"})
    public Response getUserDetails(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        ProductManufacter productManufacter = productManufacterBean.find(username);
        if (productManufacter == null)
            throw new MyEntityNotFoundException("Product Manufacter: '" + username + "' not found");

        return Response.ok(productManufacter).build();
    }

    //TODO get product manufacter catalogs
    @GET
    @Path("{username}/product-catalogs")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"ProductManufacter"})
    public Response getCatalogs(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        var productManufacter = productManufacterBean.getManufacterCatalogs(username);

        var dtos = productCatalogToDTOs(productManufacter.getProductsCatalog());
        return Response.ok(dtos).build();
    }
}