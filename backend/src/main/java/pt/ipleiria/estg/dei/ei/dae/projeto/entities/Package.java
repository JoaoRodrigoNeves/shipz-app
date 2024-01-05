package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.xml.stream.Location;
import java.io.Serializable;
import java.util.Date;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "package_id_seq")
    @SequenceGenerator(name = "package_id_seq", sequenceName = "package_id_seq", initialValue = 100000)
    long code;

    @NotNull
    String type;

    @NotNull
    String material;

    // conjunto de valores observados -> temperatura, humidade...
    //Set<Sensor> observations;

    // data de fabrico da embalagem
    @NotNull
    String manufacturingDate;

    //QualityControl qualityControlData;

    public Package() {
    }

    public Package(String type, String material, String manufacturingDate) {
        this.type = type;
        this.material = material;
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

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return code == aPackage.code && Objects.equals(type, aPackage.type) && Objects.equals(material, aPackage.material) && Objects.equals(manufacturingDate, aPackage.manufacturingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type, material, manufacturingDate);
    }
}
