package org.marehman.rideSharing.model;

import javax.persistence.*;


@Entity
@Table(name="RideShare_User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String ownerId;
    private Double lastLogin;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Double getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Double lastLogin) {
        this.lastLogin = lastLogin;
    }
}