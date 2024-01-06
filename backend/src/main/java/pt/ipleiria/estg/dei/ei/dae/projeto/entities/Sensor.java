package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.SensorType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllSensors",
                query = "SELECT s FROM Sensor s ORDER BY s.code DESC" //JPQL
        )
})
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sensor_id_seq")
    long code;
    SensorType type;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    List<Observation> observations;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sensor_package_association",
            joinColumns = @JoinColumn(name = "package_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "sensor_code", referencedColumnName = "code")
    )
    List<Package> packages;

    public Sensor(){

    }
    public Sensor(SensorType type) {
        this.type = type;
        this.observations = new ArrayList<Observation>();
        this.packages = new ArrayList<Package>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public void addObservation(Observation observation) {
        this.observations.add(observation);
    }

    public void removeObservation(Observation observation) {
        this.observations.remove(observation);
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public void addPackage(Package p) {
        this.packages.add(p);
    }

    public void removePackage(Package p) {
        this.packages.remove(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return code == sensor.code && type == sensor.type && Objects.equals(observations, sensor.observations) && Objects.equals(packages, sensor.packages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type, observations, packages);
    }
}