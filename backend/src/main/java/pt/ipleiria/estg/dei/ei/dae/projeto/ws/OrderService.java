package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.TransportPackageCatalogBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {
    @EJB
    private OrderBean orderBean;
    @EJB
    private ObservationBean observationBean;
    @EJB
    private TransportPackageCatalogBean transportPackageCatalogBean;
    @EJB
    private SensorBean sensorBean;

    private OrderDTO toDTO(Order clientOrder) {
        OrderDTO orderDTO = new OrderDTO(
                clientOrder.getCode(),
                clientOrder.getLocation(),
                clientOrder.getStatus().getOrderStatus(),
                clientOrder.getCreatedAt().toString()
        );
        if (clientOrder.getDeliveredAt() != null)
            orderDTO.setDeliveredAt(clientOrder.getDeliveredAt().toString());
        if (clientOrder.getLogisticOperator() != null) {
            orderDTO.setLogisticOperator(clientOrder.getLogisticOperator().getUsername());
            orderDTO.setLogisticOperatorName(clientOrder.getLogisticOperator().getName());
        }
        if (clientOrder.getFinalCostumer() != null) {
            orderDTO.setFinalCostumer(clientOrder.getFinalCostumer().getUsername());
            orderDTO.setFinalCostumerName(clientOrder.getFinalCostumer().getName());
        }
        return orderDTO;
    }

    private OrderDTO toDTONoProducts(Order clientOrder) {
        OrderDTO orderDTO = new OrderDTO(
                clientOrder.getCode(),
                clientOrder.getLocation(),
                clientOrder.getStatus().getOrderStatus(),
                clientOrder.getCreatedAt().toString()
        );
        if (clientOrder.getDeliveredAt() != null)
            orderDTO.setDeliveredAt(clientOrder.getDeliveredAt().toString());
        if (clientOrder.getLogisticOperator() != null) {
            orderDTO.setLogisticOperator(clientOrder.getLogisticOperator().getUsername());
        }

        return orderDTO;
    }

    private List<OrderDTO> toDTOsNoProducts(List<Order> clientOrders) {
        return clientOrders.stream().map(this::toDTO).collect(Collectors.toList());
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

    private ObservationPackageDTO observationPackageDTO(long packageCode, String packageType, List<ObservationDetailDTO> observationDetailDTO) {
        return new ObservationPackageDTO(
                packageCode,
                packageType,
                observationDetailDTO
        );
    }

    private ObservationDetailDTO observationDTO(Observation observation) {
        return new ObservationDetailDTO(
                observation.getValue(),
                observation.getSensor().getCode(),
                observation.getSensor().getType().getSensorType()
        );
    }


    private List<ObservationDetailDTO> observationDTOs(List<Observation> observations) {
        return observations.stream().map(this::observationDTO).collect(Collectors.toList());
    }

    private TransportPackageDTO transportPackageToDTO(TransportPackage transportPackage) {
        TransportPackageDTO transportPackageDTO = new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getType(),
                transportPackage.getMaterial(),
                transportPackage.getVolume(),
                transportPackage.getCreatedAt().toString()
        );
        return transportPackageDTO;
    }

    private List<TransportPackageDTO> transportPackageToDTOs(List<TransportPackage> transportPackages) {
        return transportPackages.stream()
                .map(this::transportPackageToDTO)
                .collect(Collectors.toList());
    }

    //TODO create order
    @POST
    @Path("/")
    public Response create(OrderCreateDTO clientOrderDTO) throws MyEntityNotFoundException, MyConstraintViolationException, NoStockException {
        orderBean.create(
                clientOrderDTO.getFinalCostumer(),
                clientOrderDTO.getLogisticOperator(),
                clientOrderDTO.getProducts(),
                transportPackageCatalogBean.getAll()
        );
        return Response.status(Response.Status.CREATED).build();
    }

    //TODO get details
    @GET
    @Path("/{code}")
    public Response getDetails(@PathParam("code") long code) throws MyEntityExistsException, MyEntityNotFoundException {
        var clientOrder = orderBean.getProducts(code);
        if (clientOrder == null) {
            throw new MyEntityNotFoundException("ClientOrder with code: " + code + " doesn't exist");
        }
        return Response.ok(toDTO(clientOrder)).build();
    }

    //TODO get all orders
    @GET
    @Path("/")
    public Response getAllClientOrders() {
        var clientOrders = orderBean.getAll();
        return Response.status(Response.Status.OK).entity(toDTOsNoProducts(clientOrders)).build();
    }

    //TODO get transport-packages
    @GET
    @Path("/{code}/transport-packages")
    public Response getAllTransportPackages(@PathParam("code") long code) throws MyEntityNotFoundException {
        Order clientOrder = orderBean.getTransportPackages(code);
        return Response.status(Response.Status.OK).entity(transportPackageToDTOs(clientOrder.getTransportPackages())).build();
    }

    //TODO change location
    @PATCH
    @Path("/{code}/location/")
    public Response changeLocation(@PathParam("code") long code, OrderDTO orderDTO) throws MyEntityNotFoundException {
        orderBean.changeLocation(code, orderDTO.getLocation());
        return Response.status(Response.Status.OK).build();
    }

    //TODO change status
    @PATCH
    @Path("/{code}/status")
    public Response changeStatus(@PathParam("code") long code, OrderDTO orderDTO) throws MyEntityNotFoundException {
        orderBean.changeStatus(code, orderDTO.getStatus());
        return Response.status(Response.Status.OK).build();
    }

    //TODO get observations
    @GET
    @Path("/{code}/observations")
    public Response getAllObservations(@PathParam("code") long code) throws MyEntityNotFoundException {
        var clientOrder = orderBean.getProducts(code);

        List<ObservationPackageDTO> observationPackageDTO = new ArrayList<>();

        for (Product product : clientOrder.getProducts()) {
            for (ProductPackage productPackage : product.getProductPackages()) {
                if (!productPackage.getSensors().isEmpty()) {
                    List<ObservationDetailDTO> observationSensorDetailDTOS = new ArrayList<>();
                    for (Sensor sensor : productPackage.getSensors()) {
                        observationSensorDetailDTOS.addAll(observationDTOs(sensorBean.getFilteredObservations(sensor.getCode(), clientOrder.getCreatedAt(), clientOrder.getDeliveredAt())));
                    }
                    observationPackageDTO.add(observationPackageDTO(productPackage.getCode(), productPackage.getType().getPackageType(), observationSensorDetailDTOS));
                }
            }
        }

        for (TransportPackage transportPackage : clientOrder.getTransportPackages()) {
            if (!transportPackage.getSensors().isEmpty()) {
                List<ObservationDetailDTO> observationSensorDetailDTOS = new ArrayList<>();
                for (Sensor sensor : transportPackage.getSensors()) {
                    observationSensorDetailDTOS.addAll(observationDTOs(sensorBean.getFilteredObservations(sensor.getCode(), clientOrder.getCreatedAt(), clientOrder.getDeliveredAt())));
                }
                observationPackageDTO.add(observationPackageDTO(transportPackage.getCode(), transportPackage.getType().getPackageType(), observationSensorDetailDTOS));
            }
        }

        if (observationPackageDTO.isEmpty()) {
            throw new MyEntityNotFoundException("Sem observações");
        }

        return Response.status(Response.Status.OK).entity(observationPackageDTO).build();
    }

    //TODO get order products
    @GET
    @Path("/{code}/products")
    public Response getProducts(@PathParam("code") long code) throws MyEntityNotFoundException {
        Order clientOrder = orderBean.getProducts(code);
        return Response.status(Response.Status.OK).entity(productToDTOs(clientOrder.getProducts())).build();
    }

    /*@GET
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
    }*/
}