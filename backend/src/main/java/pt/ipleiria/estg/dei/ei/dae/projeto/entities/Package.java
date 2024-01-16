package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.PackageType;

import javax.xml.stream.Location;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "package_id_seq")
    @SequenceGenerator(name = "package_id_seq", sequenceName = "package_id_seq", initialValue = 100000)
    long code;

    @NotNull
    PackageType type;

    @NotNull
    String material;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "packages")
    List<Sensor> sensors;

    long volume;

    @NotNull
    LocalDateTime createdAt;

    public Package() {
    }

    public Package(PackageType type, String material, long volume) {
        this.type = type;
        this.material = material;
        this.volume = volume;
        this.sensors = new ArrayList<Sensor>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return code == aPackage.code && type == aPackage.type && Objects.equals(material, aPackage.material) && Objects.equals(sensors, aPackage.sensors) && Objects.equals(volume, aPackage.volume) && Objects.equals(createdAt, aPackage.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type, material, sensors, volume, createdAt);
    }
}
