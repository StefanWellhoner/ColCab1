package com.colcab.helpers;

public class Users {
    public String email;
    public String token;

    public Users(String email, String token) {
        this.email = email;
        this.token = token;
    }
    public Users(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
