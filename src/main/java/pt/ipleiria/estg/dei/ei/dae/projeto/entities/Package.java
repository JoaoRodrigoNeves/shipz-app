package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPackages",
                query = "SELECT p FROM Package p ORDER BY p.createdAt DESC" //JPQL
        )
})
@Table(name = "packages")
public class Package extends Versionable implements Serializable {
    @Id
    long code;

    @NotNull
    String type;

    @NotNull
    String material;

    @NotNull
    @OneToOne
    Product product;

    // conjunto de valores observados -> temperatura, humidade...
    //Set<SensorData> observations;

    // {"em trânsito", "pendente", "entregue" , "em prepação"}...
    @NotNull
    String status;

    // data de fabrico
    @NotNull
    Date manufactoringDate;

    // para localizar a embalagem
    //Location location;

    //QualityControl qualityControlData;


    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    public Package() {
    }

    public Package(long code, String type, String material, Product product, String status, Date manufactoringDate) {
        this.code = code;
        this.type = type;
        this.material = material;
        this.product = product;
        this.status = status;
        this.manufactoringDate = manufactoringDate;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getManufactoringDate() {
        return manufactoringDate;
    }

    public void setManufactoringDate(Date manufactoringDate) {
        this.manufactoringDate = manufactoringDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
