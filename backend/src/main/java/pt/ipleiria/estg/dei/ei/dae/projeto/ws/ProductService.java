package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("products")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter", "Administrator"})
public class ProductService {

    @EJB
    private ProductBean productBean;

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

    private ProductPackageDTO productPackageToDTONoProducts(ProductPackage productPackage) {
        return new ProductPackageDTO(
                productPackage.getCode(),
                productPackage.getType(),
                productPackage.getMaterial(),
                productPackage.getCreatedAt()
        );
    }

    private List<ProductPackageDTO> productPackageToDTOsNoProducts(List<ProductPackage> productPackages) {
        return productPackages.stream()
                .map(this::productPackageToDTONoProducts)
                .collect(Collectors.toList());
    }

    private ClientOrderDTO clientOrderToDTONoProducts(ClientOrder clientOrder) {
        ClientOrderDTO clientOrderDTO = new ClientOrderDTO(
                clientOrder.getCode(),
                clientOrder.getLocation(),
                clientOrder.getStatus().getOrderStatus(),
                clientOrder.getCreatedAt().toString()
        );
        if(clientOrder.getLogisticOperator() != null){
            clientOrderDTO.setLogisticOperator(clientOrder.getLogisticOperator().getUsername());
        }
        if (clientOrder.getDeliveredAt() != null)
            clientOrderDTO.setDeliveredAt(clientOrder.getDeliveredAt().toString());

        return clientOrderDTO;
    }
    private List<ClientOrderDTO> clientOrderToDTOsNoProducts(List<ClientOrder> clientOrders) {
        return clientOrders.stream().map(this::clientOrderToDTONoProducts).collect(Collectors.toList());
    }

    private ProductCatalogDTO productCatalogToDTO(ProductCatalog productCatalog) {
        return new ProductCatalogDTO(
                productCatalog.getCode(),
                productCatalog.getName(),
                productCatalog.getCatalogArea(),
                productCatalog.getCategory(),
                productCatalog.getDescription(),
                productCatalog.getProductManufacter().getUsername()
        );
    }

    private ProductManufacterDTO productManufacterToDTO(ProductManufacter productManufacter) {
        return new ProductManufacterDTO(
                productManufacter.getUsername(),
                productManufacter.getName(),
                productManufacter.getEmail()
        );
    }

    //TODO create a new product
    @POST
    @Path("/")
    public Response create(ProductDTO productDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Product product = productBean.create(
                productDTO.getProductCatalogCode()
        );
        return Response.status(Response.Status.CREATED).entity(productToDTO(product)).build();
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
    @PUT
    @RolesAllowed({"ProductManufacter"})
    public Response update(ProductDTO productDTO) throws MyEntityNotFoundException {
        Product product = productBean.update(
                productDTO.getCode(),
                productDTO.getProductCatalogCode()
        );
        return Response.status(Response.Status.OK).entity(productToDTO(product)).build();
    }

    //TODO delete product
    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        Product product = productBean.find(code);
        productBean.delete(code);
        return Response.status(Response.Status.OK).entity(productToDTO(product)).build();
    }

    //TODO get all products
    @GET
    @Path("/")
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(productToDTOs(productBean.getAllProducts())).build();
    }

    //TODO get product-package
    @GET
    @Path("{code}/product-package/")
    public Response getProductPackage(@PathParam("code") long code) throws MyEntityNotFoundException {
        Product product = productBean.getProductPackages(code);
        return Response.status(Response.Status.OK).entity(productPackageToDTOsNoProducts(product.getProductPackages())).build();
    }

    //TODO add product to product-package
    @POST
    @Path("{code}/product-package/{packageCode}")
    public Response addToProductPackage(@PathParam("code") long code, @PathParam("packageCode") long packageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.addProductToPackage(code, packageCode);
        return Response.status(Response.Status.OK).build();
    }

    //TODO remove product from product-package
    @DELETE
    @Path("{code}/product-package/{packageCode}")
    public Response removeFromProductPackage(@PathParam("code") long code, @PathParam("packageCode") long packageCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.removeProductFromPackage(code, packageCode);
        return Response.status(Response.Status.OK).build();
    }

    //TODO get order
    @GET
    @Path("{code}/order/")
    public Response getOrder(@PathParam("code") long code) throws MyEntityNotFoundException {
        ClientOrder clientOrder = productBean.getOrder(code);
        return Response.status(Response.Status.OK).entity(clientOrderToDTONoProducts(clientOrder)).build();
    }

    //TODO add product to order
    @POST
    @Path("{code}/order/{orderCode}")
    public Response addToOrder(@PathParam("code") long code, @PathParam("orderCode") long orderCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.addProductToOrder(code, orderCode);
        return Response.status(Response.Status.OK).build();
    }

    //TODO remove product from order
    @DELETE
    @Path("{code}/order/{orderCode}")
    public Response removeFromOrder(@PathParam("code") long code, @PathParam("orderCode") long orderCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productBean.removeProductFromOrder(code, orderCode);
        return Response.status(Response.Status.OK).build();
    }

    //TODO get product-catalog from product
    @GET
    @Path("{code}/product-catalog")
    public Response getProductCatalog(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productBean.getProductCatalog(code);
        return Response.status(Response.Status.OK).entity(productCatalogToDTO(productCatalog)).build();
    }

    //TODO get product-manufacter from product
    @GET
    @Path("{code}/product-manufacter")
    public Response getProductManufacter(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductManufacter productManufacter = productBean.getProductManufacter(code);
        return Response.status(Response.Status.OK).entity(productManufacterToDTO(productManufacter)).build();
    }
}
