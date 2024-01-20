package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.LogisticOperatorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("logistic-operators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"LogisticOperator", "FinalCostumer"})
public class LogisticOperatorService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private LogisticOperatorBean logisticOperatorBean;

    private LogisticOperatorDTO toDTONoClientOrders(LogisticOperator logisticOperator) {
        return new LogisticOperatorDTO(
                logisticOperator.getUsername(),
                logisticOperator.getName(),
                logisticOperator.getEmail()
        );
    }

    private List<LogisticOperatorDTO> toDTOsNoClientOrders(List<LogisticOperator> logisticOperators) {
        return logisticOperators.stream().map(this::toDTONoClientOrders).collect(Collectors.toList());
    }

    /*private LogisticOperatorDTO toDTO(LogisticOperator logisticOperator) {
        return new LogisticOperatorDTO(
                logisticOperator.getUsername(),
                logisticOperator.getName(),
                logisticOperator.getEmail()
        );
    }*/

    /*private ProductDTO productToDTO(Product product) {
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
    }*/

    /*private List<ProductDTO> productToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }*/

    private OrderDTO clientOrderDTO(Order clientOrder) {
        return new OrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getName(),
                clientOrder.getStatus().getOrderStatus(),
                clientOrder.getCreatedAt().toString()
        );
    }

    private List<OrderDTO> clientOrderDTOs(List<Order> clientOrders) {
        return clientOrders.stream().map(this::clientOrderDTO).collect(Collectors.toList());
    }

    /*@POST
    @Path("/")
    public Response create(LogisticOperatorDTO logisticOperatorDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        logisticOperatorBean.create(
                logisticOperatorDTO.getUsername(),
                logisticOperatorDTO.getPassword(),
                logisticOperatorDTO.getName(),
                logisticOperatorDTO.getEmail()
        );

        LogisticOperator newLogisticOperator = logisticOperatorBean.find(logisticOperatorDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(toDTO(newLogisticOperator)).build();
    }*/

    /*@GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/{username}") // means: the relative url path is “/api/logistic-operators/{username}”
    public Response getDetails(@PathParam("username") String username) throws MyEntityNotFoundException {
        var logisticOperator = logisticOperatorBean.find(username);
        return Response.ok(toDTONoClientOrders(logisticOperator)).build();
    }*/

    // TODO get logistic-operator orders
    @GET
    @Path("/{username}/orders")
    @RolesAllowed({"LogisticOperator"})
    public Response getOrders(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        LogisticOperator logisticOperator = logisticOperatorBean.getOrders(username);
        return Response.ok(clientOrderDTOs(logisticOperator.getOrders())).build();
    }

    @GET
    @Path("/")
    @RolesAllowed({"FinalCostumer"})
    public List<LogisticOperatorDTO> getAll() {
        var logisticOperators = logisticOperatorBean.getAll();
        return toDTOsNoClientOrders(logisticOperators);
    }

    //TODO update a logistic-operator
    @PUT
    @Path("/")
    @RolesAllowed({"LogisticOperator"})
    public Response update(LogisticOperatorDTO logisticOperatorDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        logisticOperatorBean.update(
                logisticOperatorDTO.getUsername(),
                logisticOperatorDTO.getPassword(),
                logisticOperatorDTO.getName(),
                logisticOperatorDTO.getEmail()
        );
        LogisticOperator logisticOperator = logisticOperatorBean.find(logisticOperatorDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(toDTONoClientOrders(logisticOperator)).build();
    }
}
