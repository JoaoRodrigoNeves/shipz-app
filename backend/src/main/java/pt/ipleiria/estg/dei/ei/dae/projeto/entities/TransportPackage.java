package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTransportPackages",
                query = "SELECT tp FROM TransportPackage tp ORDER BY tp.code DESC"
        )
})
public class TransportPackage extends Package implements Serializable {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "transport_package_association",
            joinColumns = @JoinColumn(name = "package_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "transport_code", referencedColumnName = "code")
    )
    List<Package> packages;

    public TransportPackage() {
    }

    public TransportPackage(String type, String material, String status, Date manufacturingDate) {
        super(type, material, status, manufacturingDate);
        this.packages = new ArrayList<Package>();
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public void addPackage(Package pack) {
        this.packages.add(pack);
    }

    public void removePackage(Package pack) {
        this.packages.remove(pack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransportPackage)) return false;
        if (!super.equals(o)) return false;
        TransportPackage that = (TransportPackage) o;
        return packages.equals(that.packages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), packages);
    }
}
