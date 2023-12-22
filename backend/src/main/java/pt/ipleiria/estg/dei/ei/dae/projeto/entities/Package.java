package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPackages",
                query = "SELECT p FROM Package p ORDER BY p.code DESC" //JPQL
        )
})
@Table(name = "packages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Package extends Versionable implements Serializable {
    @Id
    long code;

    @NotNull
    String type;

    @NotNull
    String material;

    // conjunto de valores observados -> temperatura, humidade...
    //Set<Sensor> observations;

    // {"em trânsito", "pendente", "entregue" , "em prepação"}...
    @NotNull
    String status;

    // data de fabrico da embalagem
    @NotNull
    Date manufacturingDate;

    // para localizar a embalagem
    //Location location;

    //QualityControl qualityControlData;

    public Package() {
    }

    public Package(long code, String type, String material, String status, Date manufacturingDate) {
        this.code = code;
        this.type = type;
        this.material = material;
        this.status = status;
        this.manufacturingDate = manufacturingDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return code == aPackage.code && Objects.equals(type, aPackage.type) && Objects.equals(material, aPackage.material) && Objects.equals(status, aPackage.status) && Objects.equals(manufacturingDate, aPackage.manufacturingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type, material, status, manufacturingDate);
    }
}
