package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("products")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter"})
public class ProductService {

    @EJB
    private ProductBean productBean;
    @EJB
    private ProductPackageBean productPackageBean;
    @EJB
    private ProductCatalogBean productCatalogBean;
    @EJB
    private SensorBean sensorBean;
    /*@EJB
    private PackageBean packageBean;*/

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

    /*private List<ProductDTO> productToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }*/

    private ProductPackageDTO productPackageToDTONoProducts(ProductPackage productPackage) {
        return new ProductPackageDTO(
                productPackage.getCode(),
                productPackage.getType(),
                productPackage.getType().getPackageType(),
                productPackage.getMaterial(),
                productPackage.getVolume(),
                productPackage.getCreatedAt().toString()
        );
    }

    private List<ProductPackageDTO> productPackageToDTOsNoProducts(List<ProductPackage> productPackages) {
        return productPackages.stream()
                .map(this::productPackageToDTONoProducts)
                .collect(Collectors.toList());
    }

    /*private OrderDTO clientOrderToDTONoProducts(Order clientOrder) {
        OrderDTO orderDTO = new OrderDTO(
                clientOrder.getCode(),
                clientOrder.getStatus().getOrderStatus(),
                clientOrder.getCreatedAt().toString()
        );
        if (clientOrder.getLogisticOperator() != null) {
            orderDTO.setLogisticOperator(clientOrder.getLogisticOperator().getUsername());
        }
        if (clientOrder.getDeliveredAt() != null)
            orderDTO.setDeliveredAt(clientOrder.getDeliveredAt().toString());

        return orderDTO;
    }*/

    /*private List<OrderDTO> clientOrderToDTOsNoProducts(List<Order> clientOrders) {
        return clientOrders.stream().map(this::clientOrderToDTONoProducts).collect(Collectors.toList());
    }*/

    private ProductCatalogDTO productCatalogToDTO(ProductCatalog productCatalog) {
        return new ProductCatalogDTO(
                productCatalog.getCode(),
                productCatalog.getName(),
                productCatalog.getCatalogArea(),
                productCatalog.getCategory(),
                productCatalog.getDescription(),
                productCatalog.getProductManufacter().getName(),
                productCatalog.getProductManufacter().getUsername(),
                productCatalog.getMaxSecondaryPackage(),
                productCatalog.getMaxTertiaryPackage(),
                productCatalog.getPrimaryPackageVolume(),
                productCatalog.getPrimaryPackageMaterial(),
                productCatalog.getSecondaryPackageMaterial(),
                productCatalog.getTertiaryPackageMaterial()
        );
    }

    /*private ProductManufacterDTO productManufacterToDTO(ProductManufacter productManufacter) {
        return new ProductManufacterDTO(
                productManufacter.getUsername(),
                productManufacter.getName(),
                productManufacter.getEmail()
        );
    }*/

    //TODO create a new product
    @POST
    @Path("/")
    public Response create(ProductCreateDTO productCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        for (int i = 0; i < productCreateDTO.getQuantity(); i++) {
            Product product = productBean.create(
                    productCreateDTO.getProductCatalogCode()
            );
            ProductCatalog productCatalog = productCatalogBean.find(productCreateDTO.getProductCatalogCode());
            productPackageBean.addProductToAllPackages(product);


            ProductPackage productPackage = product.getProductPackages().stream().
                    filter(productPackage1 -> productPackage1.getType() == PackageType.PRIMARY).findFirst().orElseThrow();

            if (productCatalog.isDamageSensor()) {
                Sensor sensor = sensorBean.create(SensorType.DAMAGE.getSensorType(), false);
                sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
            }
            if (productCatalog.isGpsSensor()) {
                Sensor sensor = sensorBean.create(SensorType.GPS.getSensorType(), false);
                sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
            }
            if (productCatalog.isPressureSensor()) {
                Sensor sensor = sensorBean.create(SensorType.PRESSURE.getSensorType(), false);
                sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
            }
            if (productCatalog.isTemperatureSensor()) {
                Sensor sensor = sensorBean.create(SensorType.TEMPERATURE.getSensorType(), false);
                sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
            }
            if (productCatalog.isHumiditySensor()) {
                Sensor sensor = sensorBean.create(SensorType.HUMIDITY.getSensorType(), false);
                sensorBean.addToPackage(sensor.getCode(), productPackage.getCode());
            }
        }
        return Response.status(Response.Status.CREATED).build();
    }

    //TODO find product by code
    @GET
    @Path("{code}")
    @RolesAllowed({"ProductManufacter"})
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        Product product = productBean.find(code);
        return Response.status(Response.Status.OK).entity(productToDTO(product)).build();
    }

    //TODO update product
    /*@PUT
    @RolesAllowed({"ProductManufacter"})
    public Response update(ProductDTO productDTO) throws MyEntityNotFoundException {
        Product product = productBean.update(
                productDTO.getCode(),
                productDTO.getProductCatalogCode()
        );
        return Response.status(Response.Status.OK).entity(productToDTO(product)).build();
    }*/

    //TODO delete product
    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        Product product = productBean.find(code);
        productBean.delete(code);
        return Response.status(Response.Status.OK).entity(productToDTO(product)).build();
    }

    //TODO get all products
    /*@GET
    @Path("/")
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(productToDTOs(productBean.getAllProducts())).build();
    }*/

    //TODO get product-package
    @GET
    @Path("{code}/product-package")
    public Response getProductPackage(@PathParam("code") long code) throws MyEntityNotFoundException {
        Product product = productBean.getProductPackages(code);
        return Response.status(Response.Status.OK).entity(productPackageToDTOsNoProducts(product.getProductPackages())).build();
    }

    @GET
    @Path("{code}/product-catalog")
    @RolesAllowed({"ProductManufacter"})
    public Response getProductCatalog(@PathParam("code") long code) throws MyEntityNotFoundException {
        Product product = productBean.find(code);
        ProductCatalog productCatalog = productCatalogBean.find(product.getProductCatalog().getCode());
        return Response.status(Response.Status.OK).entity(productCatalogToDTO(productCatalog)).build();
    }

    //TODO get order
    /*@GET
    @Path("{code}/order/")
    public Response getOrder(@PathParam("code") long code) throws MyEntityNotFoundException {
        Order clientOrder = productBean.getOrder(code);
        return Response.status(Response.Status.OK).entity(clientOrderToDTONoProducts(clientOrder)).build();
    }*/

    //TODO add product to order
    /*@POST
    @Path("{code}/order/{orderCode}")
    public Response addToOrder(@PathParam("code") long code, @PathParam("orderCode") long orderCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.addProductToOrder(code, orderCode);
        return Response.status(Response.Status.OK).build();
    }*/

    //TODO remove product from order
    /*@DELETE
    @Path("{code}/order/{orderCode}")
    public Response removeFromOrder(@PathParam("code") long code, @PathParam("orderCode") long orderCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.removeProductFromOrder(code, orderCode);
        return Response.status(Response.Status.OK).build();
    }*/

    //TODO get product-manufacter from product
    /*@GET
    @Path("{code}/product-manufacter")
    public Response getProductManufacter(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = productBean.getProductManufacter(code);
        return Response.status(Response.Status.OK).entity(productManufacterToDTO(productManufacter)).build();
    }*/
}
