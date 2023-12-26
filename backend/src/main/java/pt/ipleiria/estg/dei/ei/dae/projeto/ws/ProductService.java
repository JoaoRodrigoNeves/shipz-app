package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    @EJB
    private ProductBean productBean;

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


    //TODO create a new package
    @POST
    @Path("/") // means: the relative url path is “/api/product/”
    public Response createNewProduct(ProductDTO productDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        productBean.create(
                productDTO.getCode()
        );
        Product newProduct = productBean.find(productDTO.getCode());
        return Response.status(Response.Status.CREATED).entity(productToDTO(newProduct)).build();
    }
}
