package com.omegaauto.shurik.mobilesklad.user;

public class MobileSkladUser {
    String email;
    String passwordHash;
    String token;
    String name;

    public MobileSkladUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefault(){
        setName("не авторизован");
        setEmail("email@mail.com");
        setPasswordHash("");
        setToken("");
    }
}
