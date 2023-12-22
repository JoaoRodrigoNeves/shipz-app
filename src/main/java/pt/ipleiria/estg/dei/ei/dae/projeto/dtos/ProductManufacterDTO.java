package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.UserType;

import java.util.Date;

public class ProductManufacterDTO {
    private String username;
    private String password;
    private String name;
    private UserType userType;
    private String email;
    public ProductManufacterDTO() {
    }

    public ProductManufacterDTO(String username, String password, String name, UserType userType, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
