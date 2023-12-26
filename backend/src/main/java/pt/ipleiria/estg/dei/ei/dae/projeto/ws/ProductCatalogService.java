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
@RolesAllowed({"ProductManufacter", "Administrator"})
public class ProductCatalogService {

    @EJB
    private ProductCatalogBean productCatalogBean;

    private ProductCatalogDTO productCatalogsToDTO(ProductCatalog productCatalog) {
        return new ProductCatalogDTO(
                productCatalog.getCode(),
                productCatalog.getName(),
                productCatalog.getProductManufacter().getUsername()
        );
    }

    private List<ProductCatalogDTO> productCatalogsToDTOs(List<ProductCatalog> productCatalog) {
        return productCatalog.stream()
                .map(this::productCatalogsToDTO)
                .collect(Collectors.toList());
    }


    private ProductDTO productToDTONoClientOrder(Product product) {
        return new ProductDTO(
                product.getCode(),
                product.getProductCatalog().getCode()
        );
    }

    private List<ProductDTO> productDTOsNoClientOrder(List<Product> products) {
        return products.stream().map(this::productToDTONoClientOrder).collect(Collectors.toList());
    }

    private ProductDTO productToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(
                product.getCode(),
                product.getProductCatalog().getCode()
        );
        productDTO.setClientOrderCode(product.getClientOrder().getCode());
        return productDTO;
    }

    private List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }


    @GET
    @Path("/")
    public List<ProductCatalogDTO> getAllProductCatalog() {
        return productCatalogsToDTOs(productCatalogBean.getAll());
    }

    @POST
    @Path("/")
    public Response create(ProductCatalogDTO productCatalogDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        productCatalogBean.create(
                productCatalogDTO.getCode(),
                productCatalogDTO.getName(),
                productCatalogDTO.getProductManufacterUsername()
        );
        ProductCatalog productCatalog = productCatalogBean.find(productCatalogDTO.getCode());
        return Response.status(Response.Status.CREATED).entity(productCatalogsToDTO(productCatalog)).build();
    }

    @GET
    @Path("{productCatalogCode}")
    @RolesAllowed({"ProductsManufacters"})
    public Response getProductCatalogDetails(@PathParam("productCatalogCode") long productCatalogCode) throws MyEntityNotFoundException {
        ProductCatalog productCatalog = productCatalogBean.find(productCatalogCode);
        return Response.ok(productCatalogsToDTO(productCatalog)).build();
    }
    @POST
    @Path("/{productCatalogCode}")
    @RolesAllowed({"ProductsManufacters"})
    public Response addProduct(@PathParam("productCatalogCode") long productCatalogCode, ProductDTO productDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        productCatalogBean.addProduct(productCatalogCode, productDTO.getCode());
        return Response.status(Response.Status.OK).entity("Product added").build();
    }

    @DELETE
    @Path("/{productCatalogCode}/{productCode}")
    public Response removeProduct(@PathParam("productCatalogCode") long productCatalogCode, @PathParam("productCode") long productCode) throws MyEntityNotFoundException, MyEntityExistsException {
        productCatalogBean.removeProduct(productCatalogCode, productCode);
        return Response.status(Response.Status.OK).entity("Product removed").build();
    }
}
