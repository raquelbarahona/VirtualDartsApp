package com.example.virtualdarts2;

public class UserIDEntry {
    String user_ID;
    String password;

    // constructors
    public UserIDEntry() {}

    public UserIDEntry(String user_ID,  String password) {
        this.user_ID = user_ID;
        this.password = password;
    }

    // getters
    public String getUser_ID() {
        return this.user_ID;
    }
    public String getPassword() {
        return this.password;
    }

    // setters
    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}