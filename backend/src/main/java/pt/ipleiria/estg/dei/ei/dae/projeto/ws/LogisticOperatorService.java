package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ClientOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.LogisticOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.LogisticOperatorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/logisticOperators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LogisticOperatorService {

    @EJB
    private LogisticOperatorBean logisticOperatorBean;

    private LogisticOperatorDTO toDTONoClientOrders(LogisticOperator logisticOperator) {
        return new LogisticOperatorDTO(
                logisticOperator.getUsername(),
                logisticOperator.getPassword(),
                logisticOperator.getName(),
                logisticOperator.getEmail()
        );
    }

    private LogisticOperatorDTO toDTO(LogisticOperator logisticOperator) {
         LogisticOperatorDTO logisticOperatorDTO = new LogisticOperatorDTO(
                logisticOperator.getUsername(),
                logisticOperator.getPassword(),
                logisticOperator.getName(),
                logisticOperator.getEmail()
        );
         logisticOperatorDTO.setClientOrdersDTO(clientOrderstoDTOs(logisticOperator.getClientorders()));
         return logisticOperatorDTO;
    }

    private List<LogisticOperatorDTO> toDTOsNoClientOrders(List<LogisticOperator> logisticOperators) {
        return logisticOperators.stream().map(this::toDTONoClientOrders).collect(Collectors.toList());
    }
    private ClientOrderDTO clientOrderToDTO(ClientOrder clientOrder) {
        ClientOrderDTO clientOrderDTO = new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getUsername()
        );
        clientOrderDTO.setProductsDTO(producttoDTOs(clientOrder.getProducts()));
        return clientOrderDTO;
    }

    private ProductDTO productToDTO(Product product) {
        return new ProductDTO(
                product.getCode(),
                product.getName(),
                product.getPackage().getCode()
        );
    }

    private List<ProductDTO> producttoDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }
    private List<ClientOrderDTO> clientOrderstoDTOs(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::clientOrderToDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/logisticOperators/”
    public List<LogisticOperatorDTO> getAllLogisticOperators() {
        var logisticOperators = logisticOperatorBean.getAll();
        return toDTOsNoClientOrders(logisticOperators);
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/{username}") // means: the relative url path is “/api/logisticOperators/{username}”
    public Response getLogisticOperatorDetails(@PathParam("username") String username) throws MyEntityExistsException, MyEntityNotFoundException {
        var logisticOperator = logisticOperatorBean.findLogisticOperatorWithClientOrder(username);
        if (logisticOperator == null)
            throw new MyEntityExistsException("LogisticOperator with username: " + username + " doesn't exist");
        return Response.ok(toDTO(logisticOperator)).build();
    }

    @GET
    @Path("{username}/clientOrders")
    public Response getLogisticOperatorClientOrders(@PathParam("username") String username) throws MyEntityNotFoundException {
        var logisticOperator = logisticOperatorBean.findLogisticOperatorWithClientOrder(username);
        if (logisticOperator != null) {
            var dtos = clientOrderstoDTOs(logisticOperator.getClientorders());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_LOGISTIC_OPERATOR")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewStudent (LogisticOperatorDTO logisticOperatorDTO) throws MyEntityExistsException {
        logisticOperatorBean.create(
                logisticOperatorDTO.getUsername(),
                logisticOperatorDTO.getPassword(),
                logisticOperatorDTO.getName(),
                logisticOperatorDTO.getEmail()
        );

        LogisticOperator newLogisticOperator = logisticOperatorBean.find(logisticOperatorDTO.getUsername());
        if(newLogisticOperator == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(newLogisticOperator)).build();
    }

}
