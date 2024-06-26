package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductPackageBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("product-packages")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter"})
public class ProductPackageService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private ProductPackageBean productPackageBean;

    /*private ProductPackageDTO productPackageToDTO(ProductPackage productPackage) {
        ProductPackageDTO productPackageDTO = new ProductPackageDTO(
                productPackage.getCode(),
                productPackage.getType(),
                productPackage.getType().getPackageType(),
                productPackage.getMaterial(),
                productPackage.getVolume(),
                productPackage.getCreatedAt().toString()
        );
        productPackageDTO.setProducts(productToDTOs(productPackage.getProducts()));

        return productPackageDTO;
    }*/

    /*private List<ProductPackageDTO> productPackageToDTOs(List<ProductPackage> productPackages) {
        return productPackages.stream()
                .map(this::productPackageToDTO)
                .collect(Collectors.toList());
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

    /*private List<ProductPackageDTO> productPackageToDTOsNoProducts(List<ProductPackage> productPackages) {
        return productPackages.stream()
                .map(this::productPackageToDTONoProducts)
                .collect(Collectors.toList());
    }*/

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

    /*private SensorDTO sensorToDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getCode(),
                sensor.getType().getSensorType(),
                sensor.isInUse()
        );
    }*/

    /*private List<SensorDTO> sensorToDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::sensorToDTO).collect(Collectors.toList());
    }*/

    //TODO create a product-package
    /*@POST
    @Path("/")
    public Response create(ProductPackageDTO productPackageDTO) {
        ProductPackage productPackage = productPackageBean.create(
                productPackageDTO.getType(),
                productPackageDTO.getMaterial(),
                productPackageDTO.getVolume()
        );
        return Response.status(Response.Status.OK).entity(productPackage).build();
    }*/

    //TODO get product-package details
    @GET
    @Path("{code}")
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = productPackageBean.find(code);
        return Response.status(Response.Status.OK).entity(productPackageToDTONoProducts(productPackage)).build();
    }

    //TODO update product-package
    /*@PUT
    @Path("/")
    public Response update(ProductPackageDTO productPackageDTO) throws MyEntityNotFoundException {
        productPackageBean.update(productPackageDTO.getCode(), productPackageDTO.getType(), productPackageDTO.getMaterial());
        ProductPackage productPackage = productPackageBean.find(productPackageDTO.getCode());
        return Response.status(Response.Status.OK).entity(productPackageToDTONoProducts(productPackage)).build();
    }*/

    //TODO delete product-package
    /*@DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = productPackageBean.delete(code);
        return Response.status(Response.Status.OK).entity(productPackageToDTONoProducts(productPackage)).build();
    }*/

    //TODO get all product-packages
    /*@GET
    @Path("/")
    public List<ProductPackageDTO> getAll() {
        return productPackageToDTOsNoProducts(productPackageBean.getAll());
    }*/

    //TODO get products from product-package
    @GET
    @Path("{packageCode}/products")
    public Response getProducts(@PathParam("packageCode") long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = productPackageBean.getProducts(code);
        List<ProductDTO> productDTOS = productToDTOs(productPackage.getProducts());
        return Response.ok(productDTOS).build();
    }

    //TODO add product to package
    /*@POST
    @Path("{code}/products/{productCode}")
    public Response addProduct(@PathParam("code") long code, @PathParam("productCode") long productCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.addProductToPackage(productCode, code);
        return Response.status(Response.Status.OK).build();
    }*/

    //TODO remove product from package
    /*@DELETE
    @Path("{code}/products/{productCode}")
    public Response removeProduct(@PathParam("code") long code, @PathParam("productCode") long productCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.removeProductFromPackage(productCode, code);
        return Response.status(Response.Status.OK).build();
    }*/

    //TODO get sensors from package
    /*@GET
    @Path("{code}/sensors")
    public Response getSensors(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = productPackageBean.getSensors(code);
        return Response.status(Response.Status.OK).entity(sensorToDTOs(productPackage.getSensors())).build();
    }*/
}
