package com.chinh.wherefoodapp;

public class User {
    public String name,phone,email,password;

    public User() {
    }

    public User (String name, String phone, String email, String password)
    {
        this.name=name;
        this.phone=phone;
        this.email=email;
        this.password=password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
