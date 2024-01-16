package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProductCatalogs",
                query = "SELECT p FROM ProductCatalog p" // JPQL
        )
})
@Table(name = "products_catalogs", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ProductCatalog extends Versionable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_catalog_id_seq")
    @SequenceGenerator(name = "product_catalog_id_seq", sequenceName = "product_catalog_id_seq", initialValue = 100000)
    long code;
    @NotNull
    String name;
    String catalogArea;
    String category;
    String description;
    Integer maxSecondaryPackage;
    Integer maxTertiaryPackage;
    long primaryPackageVolume;
    String primaryPackageMaterial;
    String secondaryPackageMaterial;
    String tertiaryPackageMaterial;
    Long activeProductPackageSecondaryCode;
    Long activeProductPackageTertiaryCode;

    @OneToMany(mappedBy = "productCatalog", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc")
    @NotNull
    List<Product> products;
    @ManyToOne
    @JoinColumn(name = "product_manufacter_code")
    @NotNull
    ProductManufacter productManufacter;

    public ProductCatalog() {

    }

    public ProductCatalog(String name, String catalogArea, String category, String description, ProductManufacter productManufacter, Integer maxSecondaryPackage, Integer maxTertiaryPackage, long primaryPackageVolume, String primaryPackageMaterial, String secondaryPackageMaterial, String tertiaryPackageMaterial) {
        this.name = name;
        this.catalogArea = catalogArea;
        this.category = category;
        this.description = description;
        this.productManufacter = productManufacter;
        this.products = new ArrayList<Product>();
        this.maxSecondaryPackage = maxSecondaryPackage;
        this.maxTertiaryPackage = maxTertiaryPackage;
        this.primaryPackageVolume = primaryPackageVolume;
        this.primaryPackageMaterial = primaryPackageMaterial;
        this.secondaryPackageMaterial = secondaryPackageMaterial;
        this.tertiaryPackageMaterial = tertiaryPackageMaterial;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogArea() {
        return catalogArea;
    }

    public void setCatalogArea(String catalogArea) {
        this.catalogArea = catalogArea;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductManufacter getProductManufacter() {
        return productManufacter;
    }

    public void setProductManufacter(ProductManufacter productManufacter) {
        this.productManufacter = productManufacter;
    }

    public Integer getMaxSecondaryPackage() {
        return maxSecondaryPackage;
    }

    public void setMaxSecondaryPackage(Integer maxSecondaryPackage) {
        this.maxSecondaryPackage = maxSecondaryPackage;
    }

    public Integer getMaxTertiaryPackage() {
        return maxTertiaryPackage;
    }

    public void setMaxTertiaryPackage(Integer maxTertiaryPackage) {
        this.maxTertiaryPackage = maxTertiaryPackage;
    }

    public long getPrimaryPackageVolume() {
        return primaryPackageVolume;
    }

    public void setPrimaryPackageVolume(long primaryPackageVolume) {
        this.primaryPackageVolume = primaryPackageVolume;
    }

    public String getPrimaryPackageMaterial() {
        return primaryPackageMaterial;
    }

    public void setPrimaryPackageMaterial(String primaryPackageMaterial) {
        this.primaryPackageMaterial = primaryPackageMaterial;
    }

    public String getSecondaryPackageMaterial() {
        return secondaryPackageMaterial;
    }

    public void setSecondaryPackageMaterial(String secondaryPackageMaterial) {
        this.secondaryPackageMaterial = secondaryPackageMaterial;
    }

    public String getTertiaryPackageMaterial() {
        return tertiaryPackageMaterial;
    }

    public void setTertiaryPackageMaterial(String tertiaryPackageMaterial) {
        this.tertiaryPackageMaterial = tertiaryPackageMaterial;
    }

    public Long getActiveProductPackageSecondaryCode() {
        return activeProductPackageSecondaryCode;
    }

    public void setActiveProductPackageSecondaryCode(Long activeProductPackageSecondaryCode) {
        this.activeProductPackageSecondaryCode = activeProductPackageSecondaryCode;
    }

    public Long getActiveProductPackageTertiaryCode() {
        return activeProductPackageTertiaryCode;
    }

    public void setActiveProductPackageTertiaryCode(Long activeProductPackageTertiaryCode) {
        this.activeProductPackageTertiaryCode = activeProductPackageTertiaryCode;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCatalog that = (ProductCatalog) o;
        return code == that.code && Objects.equals(name, that.name) && Objects.equals(catalogArea, that.catalogArea) && Objects.equals(category, that.category) && Objects.equals(description, that.description) && Objects.equals(maxSecondaryPackage, that.maxSecondaryPackage) && Objects.equals(maxTertiaryPackage, that.maxTertiaryPackage) && Objects.equals(primaryPackageVolume, that.primaryPackageVolume) && Objects.equals(primaryPackageMaterial, that.primaryPackageMaterial) && Objects.equals(secondaryPackageMaterial, that.secondaryPackageMaterial) && Objects.equals(tertiaryPackageMaterial, that.tertiaryPackageMaterial) && Objects.equals(activeProductPackageSecondaryCode, that.activeProductPackageSecondaryCode) && Objects.equals(activeProductPackageTertiaryCode, that.activeProductPackageTertiaryCode) && Objects.equals(products, that.products) && Objects.equals(productManufacter, that.productManufacter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, catalogArea, category, description, maxSecondaryPackage, maxTertiaryPackage, primaryPackageVolume, primaryPackageMaterial, secondaryPackageMaterial, tertiaryPackageMaterial, activeProductPackageSecondaryCode, activeProductPackageTertiaryCode, products, productManufacter);
    }
}
