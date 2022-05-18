package com.ascii274.jobarcelona22.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * UserDto para no ver todos los datos de user.
 */

@Document(collection="usuarios")
public class UserDto {
    @Field(name="username")
    private String username;
    @Field(name="rol")
    private String rol;
    @Field(name="token")
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
