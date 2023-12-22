package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ClientOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ClientOrderBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;

import java.util.List;
import java.util.stream.Collectors;

@Path("/clientOrders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClientOrderService {
    @EJB
    private ClientOrderBean clientOrderBean;

    private ClientOrderDTO toDTO(ClientOrder clientOrder) {
        return new ClientOrderDTO(
                clientOrder.getId()
        );
    }

    private List<ClientOrderDTO> toDTOs(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ClientOrderDTO> getAllClientOrders() {
        var clientOrders = clientOrderBean.getAll();
        return toDTOs(clientOrders);
    }


}
