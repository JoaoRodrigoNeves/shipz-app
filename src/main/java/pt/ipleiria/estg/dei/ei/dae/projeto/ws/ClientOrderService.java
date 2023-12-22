package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ClientOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ClientOrderBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/clientOrders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClientOrderService {
    @EJB
    private ClientOrderBean clientOrderBean;

    private ClientOrderDTO toDTONoProducts(ClientOrder clientOrder) {
        return new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getUsername()
        );
    }
    private ClientOrderDTO toDTO(ClientOrder clientOrder) {
        ClientOrderDTO clientOrderDTO = new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getUsername()
        );
        clientOrderDTO.setProductsDTO(productsToDTOs(clientOrder.getProducts()));
        return clientOrderDTO;
    }
    private List<ClientOrderDTO> toDTOsNoProducts(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::toDTONoProducts).collect(Collectors.toList());
    }
    private ProductDTO productToDTO(Product product) {
        return new ProductDTO(
                product.getCode(),
                product.getName(),
                product.getPackage().getCode()
        );
    }
    private List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/") // means: the relative url path is “/api/clientOrder”
    public List<ClientOrderDTO> getAllClientOrders() {
        var clientOrders = clientOrderBean.getAll();
        return toDTOsNoProducts(clientOrders);
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/{code}") // means: the relative url path is “/api/clientOrder/{code}”
    public Response getLogisticOperatorDetails(@PathParam("code") long code) throws MyEntityExistsException, MyEntityNotFoundException {
        var clientOrder = clientOrderBean.findClientOrderWithProducts(code);
        if (clientOrder == null) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " doesn't exist");
        }
        return Response.ok(toDTO(clientOrder)).build();
    }


}
