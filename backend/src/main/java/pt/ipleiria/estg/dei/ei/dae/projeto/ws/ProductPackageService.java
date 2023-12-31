package pt.ipleiria.estg.dei.ei.dae.projeto.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductManufacterDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.dtos.ProductPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductManufacterBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.ejbs.ProductPackageBean;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductManufacter;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.ProductPackage;
import pt.ipleiria.estg.dei.ei.dae.projeto.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projeto.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("product-packages")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"LogisticOperator", "Administrator"})
public class ProductPackageService {
    @Context
    private SecurityContext securityContext;
    @EJB
    private ProductPackageBean productPackageBean;

    private ProductPackageDTO productPackageToDTO(ProductPackage productPackage) {
        ProductPackageDTO productPackageDTO = new ProductPackageDTO(
                productPackage.getCode(),
                productPackage.getType(),
                productPackage.getMaterial(),
                productPackage.getStatus(),
                productPackage.getManufacturingDate()
        );
        productPackageDTO.setProducts(productToDTOs(productPackage.getProducts()));

        return productPackageDTO;
    }

    private List<ProductPackageDTO> productPackageToDTOs(List<ProductPackage> productPackages) {
        return productPackages.stream()
                .map(this::productPackageToDTO)
                .collect(Collectors.toList());
    }

    private ProductPackageDTO productPackageToDTONoProducts(ProductPackage productPackage) {
        return new ProductPackageDTO(
                productPackage.getCode(),
                productPackage.getType(),
                productPackage.getMaterial(),
                productPackage.getStatus(),
                productPackage.getManufacturingDate()
        );
    }

    private List<ProductPackageDTO> productPackageToDTOsNoProducts(List<ProductPackage> productPackages) {
        return productPackages.stream()
                .map(this::productPackageToDTONoProducts)
                .collect(Collectors.toList());
    }

    private ProductDTO productToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(
                product.getCode(),
                product.getProductCatalog().getCode(),
                product.getProductCatalog().getName(),
                product.getProductManufacter().getUsername()
        );

        if(product.getClientOrder() != null){
            productDTO.setClientOrderCode(product.getClientOrder().getCode());
        }
        return productDTO;
    }

    private List<ProductDTO> productToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }

    //TODO create a product-package

    //TODO get product-package details

    //TODO update product-package

    //TODO delete product-package

    //TODO get all product-packages
    @GET
    @Path("/")
    public List<ProductPackageDTO> getAll() {
        return productPackageToDTOsNoProducts(productPackageBean.getAll());
    }
    //TODO get products from product-package
    @GET
    @Path("{packageCode}/products")
    public Response getProducts(@PathParam("packageCode") long code) throws MyEntityNotFoundException {
        ProductPackage productPackage = productPackageBean.getProducts(code);
        List<ProductDTO> productDTOS = productToDTOs(productPackage.getProducts());
        return Response.ok(productDTOS).build();
    }
}
