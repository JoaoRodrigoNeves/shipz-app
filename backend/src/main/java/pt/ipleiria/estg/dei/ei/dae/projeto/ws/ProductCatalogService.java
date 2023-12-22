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

import java.util.List;
import java.util.stream.Collectors;

public class ProductCatalogService {

    @EJB
    private ProductCatalogBean productCatalogBean;

    private ProductCatalogDTO productCatalogToDTONoProducts(ProductCatalog productCatalog) {
        return new ProductCatalogDTO(
                productCatalog.getCode(),
                productCatalog.getName(),
                productCatalog.getProductManufacter()
        );
    }

    private List<ProductCatalogDTO> productCatalogsToDTONoProducts(List<ProductCatalog> productCatalog) {
        return productCatalog.stream()
                .map(this::productCatalogToDTONoProducts)
                .collect(Collectors.toList());
    }

    private ProductDTO productDTO(Product product) {
        return new ProductDTO(
                product.getCode(),
                product.getName(),
                product.getProductCatalog().getCode()
        );
    }

    private List<ProductDTO> productsDTO(List<Product> products) {
        return products.stream().map(this::productDTO).collect(Collectors.toList());
    }

    private ProductCatalogDTO productCatalogToDTOWithProducts(ProductCatalog productCatalog) {
        ProductCatalogDTO productCatalogDTO = new ProductCatalogDTO(
                productCatalog.getCode(),
                productCatalog.getName(),
                productCatalog.getProductManufacter()
        );

        productCatalogDTO.setProductsDTO(productsDTO(productCatalog.getProducts()));
        return  productCatalogDTO;
    }


    @GET
    @Path("/")
    public List<ProductCatalogDTO> getAllProductCatalog() {
        return productCatalogsToDTONoProducts(productCatalogBean.getAll());
    }

    @POST
    @Path("/")
    public Response create(ProductCatalogDTO productCatalogDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        productCatalogBean.create(
                productCatalogDTO.getCode(),
                productCatalogDTO.getName(),
                productCatalogDTO.getProductManufacter().getUsername()
        );
        ProductCatalog productCatalog = productCatalogBean.find(productCatalogDTO.getCode());
        return Response.status(Response.Status.CREATED).entity(productCatalogToDTONoProducts(productCatalog)).build();
    }

    @GET
    @Path("{productCatalogCode}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"ProductsManufacters"})
    public Response getProductCatalogDetails(@PathParam("productCatalogCode") long productCatalogCode) {
        ProductCatalog productCatalog = productCatalogBean.find(productCatalogCode);
        if (productCatalog != null) {
            return Response.ok(productCatalogToDTOWithProducts(productCatalog)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUCT_CATALOG")
                .build();
    }
    @POST
    @Path("/add/{productCatalogCode}/{productCode}")
    public Response addProduct (@PathParam("productCatalogCode") long productCatalogCode, @PathParam("productCode") long productCode){

        if(!productCatalogBean.addProductToProductCatalog(productCatalogCode, productCode))
            return Response.status(Response.Status.BAD_REQUEST).build();
        return
                Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/remove/{productCatalogCode}/{productCode}")
    public Response removeSubjectFromStudent(@PathParam("productCatalogCode") long productCatalogCode, @PathParam("productCode") long productCode) {

        if(productCatalogBean.removeProductToProductCatalog(productCatalogCode, productCode)){
            return Response.ok().build();
        }else {
            return Response.serverError().build();

        }

    }
}
