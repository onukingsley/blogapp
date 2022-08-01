package com.example.nox;

public class UserModels {
    String avatar, email,fullname,username;

    public UserModels() {
    }

    public UserModels(String avatar, String email, String fullname, String username) {
        this.avatar = avatar;
        this.email = email;
        this.fullname = fullname;
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }
}
