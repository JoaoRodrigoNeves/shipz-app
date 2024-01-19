package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.FinalCostumerBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.FinalCostumer;
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
        return new FinalCostumerDTO(
                finalCostumer.getUsername(),
                finalCostumer.getName(),
                finalCostumer.getEmail(),
                finalCostumer.getAddress()
        );
    }

    private List<FinalCostumerDTO> toDTOs(List<FinalCostumer> finalCostumers) {
        return finalCostumers.stream().map(this::toDTO).collect(Collectors.toList());
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

    private ProductDTO productToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(
                product.getCode(),
                product.getProductCatalog().getCode(),
                product.getProductCatalog().getName(),
                product.getProductManufacter().getName()
        );

        if (product.getOrder() != null) {
            productDTO.setClientOrderCode(product.getOrder().getCode());
        }
        return productDTO;
    }

    private List<ProductDTO> productToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }

    private OrderDTO clientOrderToDTO(Order clientOrder) {
        return new OrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getName(),
                clientOrder.getStatus().getOrderStatus(),
                clientOrder.getLocation(),
                clientOrder.getCreatedAt().toString()
        );
    }

    private List<OrderDTO> clientOrderToDTOs(List<Order> clientOrders) {
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

    //TODO update a logistic-operator
    @PUT
    @Path("/")
    public Response update(FinalCostumerDTO finalCostumerDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        finalCostumerBean.update(
                finalCostumerDTO.getUsername(),
                finalCostumerDTO.getPassword(),
                finalCostumerDTO.getName(),
                finalCostumerDTO.getEmail(),
                finalCostumerDTO.getAddress()
        );
        FinalCostumer finalCostumer = finalCostumerBean.find(finalCostumerDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(toDTONoClientOrders(finalCostumer)).build();
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/{username}") // means: the relative url path is “/api/final-costumers/{username}”
    public Response getDetails(@PathParam("username") String username) throws MyEntityExistsException, MyEntityNotFoundException {
        FinalCostumer finalCostumer = finalCostumerBean.find(username);
        return Response.ok(toDTO(finalCostumer)).build();
    }

    @GET
    @Path("/")
    public List<FinalCostumerDTO> getAll() {
        var finalCostumers = finalCostumerBean.getAll();
        return toDTOsNoClientOrders(finalCostumers);
    }

    @GET
    @Path("/{username}/orders")
    public Response getOrders(@PathParam("username") String username) throws MyEntityNotFoundException {
        FinalCostumer finalCostumer = finalCostumerBean.getOrders(username);
        return Response.ok(clientOrderToDTOs(finalCostumer.getOrders())).build();
    }
}
