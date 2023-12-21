package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import pt.ipleiria.estg.dei.ei.dae.projeto.entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogisticOperatorDTO extends User implements Serializable {
    private double latitude;
    private double longitude;
    private int temperature;
    private int humidity;
    private int acceleration;
    private int otherAmbientalData;
    private boolean isOpened;
    private boolean isAuthorized;

    public LogisticOperatorDTO(String username, String password, String name, String email, double latitude, double longitude, int temperature, int humidity, int acceleration, int otherAmbientalData, boolean isOpened, boolean isAuthorized) {
        super(username, password, name, email);
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.humidity = humidity;
        this.acceleration = acceleration;
        this.otherAmbientalData = otherAmbientalData;
        this.isOpened = isOpened;
        this.isAuthorized = isAuthorized;
    }

    public LogisticOperatorDTO() {
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

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }
}
