package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.LogisticOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ClientOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.LogisticOperatorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.LogisticOperator;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/logisticOperators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LogisticOperatorService {

    @EJB
    private LogisticOperatorBean logisticOperatorBean;

    private LogisticOperatorDTO toDTO(LogisticOperator logisticOperator) {

        LogisticOperatorDTO logisticOperatorDTO = new LogisticOperatorDTO(
                logisticOperator.getUsername(),
                logisticOperator.getPassword(),
                logisticOperator.getName(),
                logisticOperator.getEmail()
        );
        logisticOperatorDTO.setOrders(ClientOrderToDTOs(logisticOperator.getOrders()));
        return logisticOperatorDTO;
    }

    private ClientOrderDTO ClientOrderToDTO(ClientOrder clientOrder) {
        return new ClientOrderDTO(
                clientOrder.getId()
        );
    }
    public List<ClientOrderDTO> ClientOrderToDTOs(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::ClientOrderToDTO).collect(Collectors.toList());
    }

    private List<LogisticOperatorDTO> toDTOs(List<LogisticOperator> logisticOperators) {
        return logisticOperators.stream().map(this::toDTO).collect(Collectors.toList());
    }



    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/logisticOperators/”
    public List<LogisticOperatorDTO> getAllLogisticOperators() {
        var logisticOperators = logisticOperatorBean.getAll();
        //Hibernate.initialize(logisticOperators);
        return toDTOs(logisticOperators);
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/{username}") // means: the relative url path is “/api/logisticOperators/{username}”
    public LogisticOperatorDTO getLogisticOperatorDetails(@PathParam("username") String username) throws MyEntityExistsException {
        var logisticOperator = logisticOperatorBean.find(username);
        if (logisticOperator == null)
            throw new MyEntityExistsException("LogisticOperator with username: " + username + " doesn't exist");
        return toDTO(logisticOperator);
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
