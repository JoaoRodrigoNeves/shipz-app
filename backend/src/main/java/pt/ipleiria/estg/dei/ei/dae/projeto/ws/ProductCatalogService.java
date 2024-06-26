package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductCatalogDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductCatalogBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.TransportPackageCatalogBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.TransportPackageCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("product-catalogs")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter", "FinalCostumer"})
public class ProductCatalogService {

    @EJB
    private ProductCatalogBean productCatalogBean;
    @EJB
    private TransportPackageCatalogBean transportPackageCatalogBean;

    private ProductCatalogDTO productCatalogToDTO(ProductCatalog productCatalog) {
        ProductCatalogDTO productCatalogDTO = new ProductCatalogDTO(
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

        if (productCatalog.isTemperatureSensor())
            productCatalogDTO.addSensor(SensorType.TEMPERATURE.getSensorType());
        if (productCatalog.isGpsSensor())
            productCatalogDTO.addSensor(SensorType.GPS.getSensorType());
        if (productCatalog.isDamageSensor())
            productCatalogDTO.addSensor(SensorType.DAMAGE.getSensorType());
        if (productCatalog.isPressureSensor())
            productCatalogDTO.addSensor(SensorType.PRESSURE.getSensorType());
        if (productCatalog.isHumiditySensor())
            productCatalogDTO.addSensor(SensorType.HUMIDITY.getSensorType());

        return productCatalogDTO;
    }

    private List<ProductCatalogDTO> productCatalogToDTOs(List<ProductCatalog> productCatalog) {
        return productCatalog.stream()
                .map(this::productCatalogToDTO)
                .collect(Collectors.toList());
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

    private List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }


    //TODO create a new product-catalog
    @POST
    @Path("/")
    @RolesAllowed({"ProductManufacter"})
    public Response create(ProductCatalogDTO productCatalogDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, NoVolumeException {
        List<TransportPackageCatalog> transportPackageCatalogs = transportPackageCatalogBean.getAll();
        if (transportPackageCatalogs.get(transportPackageCatalogs.size() - 1).getVolume() < productCatalogDTO.getPrimaryPackageVolume())
            throw new NoVolumeException("ProductCatalog primary volume not acceptable, max acceptable: " + transportPackageCatalogs.get(transportPackageCatalogs.size() - 1).getVolume());

        productCatalogBean.create(
                productCatalogDTO.getName(),
                productCatalogDTO.getCatalogArea(),
                productCatalogDTO.getCategory(),
                productCatalogDTO.getDescription(),
                productCatalogDTO.getProductManufacterUsername(),
                productCatalogDTO.getMaxSecondaryPackage(),
                productCatalogDTO.getMaxTertiaryPackage(),
                productCatalogDTO.getPrimaryPackageVolume(),
                productCatalogDTO.getPrimaryPackageMaterial(),
                productCatalogDTO.getSecondaryPackageMaterial(),
                productCatalogDTO.getTertiaryPackageMaterial(),
                productCatalogDTO.getSensors()
        );

        return Response.status(Response.Status.CREATED).build();
    }

    //TODO get product catalog by code
    @GET
    @Path("{code}")
    @RolesAllowed({"ProductManufacter"})
    public Response getDetails(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.find(code);
        return Response.status(Response.Status.OK).entity(productCatalogToDTO(productCatalog)).build();
    }

    //TODO update a product-catalog
    @PUT
    @Path("{code}")
    @RolesAllowed({"ProductManufacter"})
    public Response update(@PathParam("code") long code, ProductCatalogDTO productCatalogDTO) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.update(
                code,
                productCatalogDTO.getName(),
                productCatalogDTO.getCatalogArea(),
                productCatalogDTO.getCategory(),
                productCatalogDTO.getDescription(),
                productCatalogDTO.getProductManufacterUsername(),
                productCatalogDTO.getMaxSecondaryPackage(),
                productCatalogDTO.getMaxTertiaryPackage(),
                productCatalogDTO.getPrimaryPackageVolume(),
                productCatalogDTO.getPrimaryPackageMaterial(),
                productCatalogDTO.getSecondaryPackageMaterial(),
                productCatalogDTO.getTertiaryPackageMaterial(),
                productCatalogDTO.getSensors()
        );
        return Response.status(Response.Status.OK).entity(productCatalogToDTO(productCatalog)).build();
    }

    //TODO delete a product-catalog
    @DELETE
    @Path("{code}")
    @RolesAllowed({"ProductManufacter"})
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException, ListNotEmptyException {
        productCatalogBean.remove(code);
        return Response.status(Response.Status.OK).entity("Success").build();
    }

    //TODO get all product-catalogs
    /*@GET
    @Path("/")
    public List<ProductCatalogDTO> getAll() {
        return productCatalogToDTOs(productCatalogBean.getAll());
    }*/

    @GET
    @Path("/available")
    @RolesAllowed({"FinalCostumer"})
    public List<ProductCatalogDTO> getAllAvailable() {
        return productCatalogToDTOs(productCatalogBean.getAllAvailable());
    }

    //TODO get products from product-catalog
    @GET
    @Path("{code}/products")
    @RolesAllowed({"ProductManufacter"})
    public Response getProducts(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.getProductCatalogProducts(code);
        return Response.status(Response.Status.OK).entity(productsToDTOs(productCatalog.getProducts())).build();
    }

    //TODO add product to product-catalog
    /*@POST
    @Path("{productCatalogCode}")
    public Response addProduct(@PathParam("productCatalogCode") long productCatalogCode, ProductDTO productDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        productCatalogBean.addProduct(productCatalogCode, productDTO.getCode());
        return Response.status(Response.Status.OK).entity("Product added").build();
    }*/

    //TODO remove product from product-catalog
    /*@DELETE
    @Path("{productCatalogCode}")
    public Response removeProduct(@PathParam("productCatalogCode") long productCatalogCode, ProductDTO productDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        productCatalogBean.removeProduct(productCatalogCode, productDTO.getCode());
        return Response.status(Response.Status.OK).entity("Product removed").build();
    }*/

    //TODO get products without product-package
    /*@GET
    @Path("{code}/products/no-package")
    public Response getProductsWithoutProductPackage(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.getProductCatalogProducts(code);
        List<ProductDTO> productsToDTOs = productsToDTOs(productCatalogBean.getProductsWithoutProductPackage(productCatalog));
        return Response.status(Response.Status.OK).entity(productsToDTOs).build();
    }*/
}
