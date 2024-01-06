package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductCatalogDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductCatalogBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductCatalog;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("product-catalogs")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"ProductManufacter", "Administrator", "LogisticOperator"})
public class ProductCatalogService {

    @EJB
    private ProductCatalogBean productCatalogBean;

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

        if(product.getClientOrder() != null){
            productDTO.setClientOrderCode(product.getClientOrder().getCode());
        }
        return productDTO;
    }

    private List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }


    //TODO create a new product-catalog
    @POST
    @Path("/")
    public Response create(ProductCatalogDTO productCatalogDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        ProductCatalog productCatalog = productCatalogBean.create(
                productCatalogDTO.getName(),
                productCatalogDTO.getCatalogArea(),
                productCatalogDTO.getCategory(),
                productCatalogDTO.getDescription(),
                productCatalogDTO.getProductManufacterUsername()
        );
        return Response.status(Response.Status.CREATED).entity(productCatalogToDTO(productCatalog)).build();
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
                productCatalogDTO.getProductManufacterUsername()
        );
        return Response.status(Response.Status.OK).entity(productCatalogToDTO(productCatalog)).build();
    }

    //TODO delete a product-catalog
    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        productCatalogBean.remove(code);
        return Response.status(Response.Status.OK).entity("Success").build();
    }

    //TODO get all product-catalogs
    @GET
    @Path("/")
    @RolesAllowed({"LogisticOperator", "FinalCostumer"})
    public List<ProductCatalogDTO> getAll() {
        return productCatalogToDTOs(productCatalogBean.getAll());
    }
    @GET
    @Path("/available")
    @RolesAllowed({"LogisticOperator", "FinalCostumer"})
    public List<ProductCatalogDTO> getAllAvailable() {
        return productCatalogToDTOs(productCatalogBean.getAllAvailable());
    }

    //TODO get products from product-catalog
    @GET
    @Path("{code}/products")
    @RolesAllowed({"ProductsManufacters", "LogisticOperator"})
    public Response getProducts(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.getProductCatalogProducts(code);
        return Response.status(Response.Status.OK).entity(productsToDTOs(productCatalog.getProducts())).build();
    }

    //TODO add product to product-catalog
    @POST
    @Path("{productCatalogCode}")
    @RolesAllowed({"ProductManufacter"})
    public Response addProduct(@PathParam("productCatalogCode") long productCatalogCode, ProductDTO productDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        productCatalogBean.addProduct(productCatalogCode, productDTO.getCode());
        return Response.status(Response.Status.OK).entity("Product added").build();
    }

    //TODO remove product from product-catalog
    @DELETE
    @Path("{productCatalogCode}")
    @RolesAllowed({"ProductManufacter"})
    public Response removeProduct(@PathParam("productCatalogCode") long productCatalogCode, ProductDTO productDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        productCatalogBean.removeProduct(productCatalogCode, productDTO.getCode());
        return Response.status(Response.Status.OK).entity("Product removed").build();
    }

    //TODO get products without product-package
    @GET
    @Path("{code}/products/no-package")
    public Response getProductsWithoutProductPackage(@PathParam("code") long code) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.getProductCatalogProducts(code);
        List<ProductDTO> productsToDTOs = productsToDTOs(productCatalogBean.getProductsWithoutProductPackage(productCatalog));
        return Response.status(Response.Status.OK).entity(productsToDTOs).build();
    }
}
