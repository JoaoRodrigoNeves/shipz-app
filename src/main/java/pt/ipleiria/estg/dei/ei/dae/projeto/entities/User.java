package pt.ipleiria.estg.dei.ei.dae.projeto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.projeto.entities.types.UserType;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Extra: try the other strategies… what happens to the database?
public class User extends Versionable implements Serializable{
    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private UserType userType;
    @Email
    @NotNull
    private String email;

    @Version
    private int version;
    public User() {

    }

    public User(String username, String password, String name, UserType userType, String email) {
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
