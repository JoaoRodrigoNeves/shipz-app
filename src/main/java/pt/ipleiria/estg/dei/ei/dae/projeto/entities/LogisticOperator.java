package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllLogisticOperators",
                query = "SELECT s FROM LogisticOperator s ORDER BY s.name" // JPQL
        )
})
public class LogisticOperator extends User implements Serializable {
    double latitude;
    double longitude;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    int temperature;
    int humidity;
    int acceleration;
    int otherAmbientalData;
    boolean isOpened;
    boolean isAutorized;

    public LogisticOperator(String username, String password, String name, String email, double latitude, double longitude, int temperature, int humidity, int acceleration, int otherAmbientalData, boolean isOpened, boolean isAutorized) {
        super(username, password, name, email);
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.humidity = humidity;
        this.acceleration = acceleration;
        this.otherAmbientalData = otherAmbientalData;
        this.isOpened = isOpened;
        this.isAutorized = isAutorized;
    }

    public LogisticOperator() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getOtherAmbientalData() {
        return otherAmbientalData;
    }

    public void setOtherAmbientalData(int otherAmbientalData) {
        this.otherAmbientalData = otherAmbientalData;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isAutorized() {
        return isAutorized;
    }

    public void setAutorized(boolean autorized) {
        isAutorized = autorized;
    }
}
