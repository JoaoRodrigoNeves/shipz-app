package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ClientOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.FinalCostumerDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.LogisticOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.FinalCostumerBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.FinalCostumer;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("final-costumers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"FinalCostumer", "Administrator"})
public class FinalCostumerService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private FinalCostumerBean finalCostumerBean;

    private FinalCostumerDTO toDTO(FinalCostumer finalCostumer) {
        FinalCostumerDTO finalCostumerDTO = new FinalCostumerDTO(
                finalCostumer.getUsername(),
                finalCostumer.getName(),
                finalCostumer.getEmail(),
                finalCostumer.getAddress()
        );
        finalCostumerDTO.setClientOrdersDTO(clientOrderToDTOs(finalCostumer.getClientOrders()));
        return finalCostumerDTO;
    }

    private ProductDTO productToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(
                product.getCode(),
                product.getProductCatalog().getCode(),
                product.getProductCatalog().getName(),
                product.getProductManufacter().getName()
        );

        if(product.getClientOrder() != null){
            productDTO.setClientOrderCode(product.getClientOrder().getCode());
        }
        return productDTO;
    }

    private FinalCostumerDTO toDTONoClientOrders(FinalCostumer finalCostumer) {
        return new FinalCostumerDTO(
                finalCostumer.getUsername(),
                finalCostumer.getName(),
                finalCostumer.getEmail(),
                finalCostumer.getAddress()
        );
    }

    private List<FinalCostumerDTO> toDTOsNoClientOrders(List<FinalCostumer> finalCostumers) {
        return finalCostumers.stream().map(this::toDTONoClientOrders).collect(Collectors.toList());
    }

    private List<ProductDTO> productToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }

    private ClientOrderDTO clientOrderToDTO(ClientOrder clientOrder) {
        ClientOrderDTO clientOrderDTO = new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLocation()
        );
        clientOrderDTO.setFinalCostumer(clientOrder.getFinalCostumer().getUsername());
        return clientOrderDTO;
    }

    private List<FinalCostumerDTO> toDTOs(List<FinalCostumer> finalCostumers) {
        return finalCostumers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<ClientOrderDTO> clientOrderToDTOs(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::clientOrderToDTO).collect(Collectors.toList());
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

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/{username}") // means: the relative url path is “/api/final-costumers/{username}”
    public Response getDetails(@PathParam("username") String username) throws MyEntityExistsException, MyEntityNotFoundException {
        FinalCostumer finalCostumer = finalCostumerBean.findFinalCostumerWithClientOrder(username);
        if (finalCostumer == null)
            throw new MyEntityExistsException("Final Costumer with username: " + username + " doesn't exist");
        return Response.ok(toDTO(finalCostumer)).build();
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/final-costumers/”
    public List<FinalCostumerDTO> getAll() {
        var finalCostumers = finalCostumerBean.getAll();
        return toDTOsNoClientOrders(finalCostumers);
    }
}
