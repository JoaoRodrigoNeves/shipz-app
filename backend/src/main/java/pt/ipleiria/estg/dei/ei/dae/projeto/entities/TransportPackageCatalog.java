package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTransportPackageCatalogs",
                query = "SELECT t FROM TransportPackageCatalog t" // JPQL
        )
})
@Table(name = "transport_package_catalogs", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class TransportPackageCatalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "package_id_seq")
    @SequenceGenerator(name = "package_id_seq", sequenceName = "package_id_seq", initialValue = 100000)
    long code;
    String name;
    String material;
    long volume;
    @OneToMany(mappedBy = "transportPackageCatalog", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    List<TransportPackage> transportPackages;

    public TransportPackageCatalog() {

    }
    public TransportPackageCatalog(String name, String material, long volume) {
        this.name = name;
        this.material = material;
        this.volume = volume;
        this.transportPackages = new ArrayList<TransportPackage>();
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public List<TransportPackage> getTransportPackages() {
        return transportPackages;
    }

    public void setTransportPackages(List<TransportPackage> transportPackages) {
        this.transportPackages = transportPackages;
    }

    public void addTransportPackage(TransportPackage transportPackage){
        this.transportPackages.add(transportPackage);
    }

    public void removeTransportPackage(TransportPackage transportPackage){
        this.transportPackages.remove(transportPackage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransportPackageCatalog that = (TransportPackageCatalog) o;
        return code == that.code && volume == that.volume && Objects.equals(name, that.name) && Objects.equals(material, that.material) && Objects.equals(transportPackages, that.transportPackages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, material, volume, transportPackages);
    }
}
