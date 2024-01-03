package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ClientOrderDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ClientOrderBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ClientOrder;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Path("/clientOrders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClientOrderService {
    @EJB
    private ClientOrderBean clientOrderBean;

    @EJB
    private ObservationBean observationBean;

    private ClientOrderDTO toDTO(ClientOrder clientOrder) {
        ClientOrderDTO clientOrderDTO = new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getUsername()
        );
        clientOrderDTO.setProductsDTO(productToDTOs(clientOrder.getProducts()));
        return clientOrderDTO;
    }
    private ClientOrderDTO toDTONoProducts(ClientOrder clientOrder) {
        return new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLogisticOperator().getUsername()
        );
    }
    private List<ClientOrderDTO> toDTOsNoProducts(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::toDTONoProducts).collect(Collectors.toList());
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

    private List<ProductDTO> productToDTOs(List<Product> products) {
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
    public Response getDetails(@PathParam("code") long code) throws MyEntityExistsException, MyEntityNotFoundException {
        var clientOrder = clientOrderBean.findClientOrderWithProducts(code);
        if (clientOrder == null) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " doesn't exist");
        }
        return Response.ok(toDTO(clientOrder)).build();
    }

    @GET
    @Path("/test")
    public Response getCurrentWeather() {
        String apiKey = "04c28712c3a358c49d2733b0c44feae0";
        String units = "metric";
        String location = "Leiria";
        String lang = "pt";

        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather";

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(apiUrl)
                    .queryParam("q", location)
                    .queryParam("appid", apiKey)
                    .queryParam("lang", lang)
                    .queryParam("units", units);

            Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);

            Response response = builder.get();

            if (response.getStatus() == 200) {
                String jsonResponse = response.readEntity(String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                JsonNode mainNode = jsonNode.get("main");

                for (int i = 0; i < 5; i++) {
                    Random random = new Random();

                    double temperature = changeValue(mainNode.get("temp").asDouble(), random);
                    double pressure = changeValue(mainNode.get("pressure").asDouble(), random);
                    double humidity = changeValue(mainNode.get("humidity").asDouble(), random);

                    observationBean.create(1, temperature);
                    observationBean.create(2, humidity);
                    observationBean.create(3, pressure);
                }

                return Response.ok(true).build();
            } else {
                System.out.println("Falha na solicitação. Código de resposta: " + response.getStatus());
            }

            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static double changeValue(double originalValue, Random random) {
        double randomNumber = (random.nextDouble() * 2) - 1;
        double changedValue = originalValue + randomNumber;

        changedValue = Math.round(changedValue * 100.0) / 100.0;

        return changedValue;
    }
}
