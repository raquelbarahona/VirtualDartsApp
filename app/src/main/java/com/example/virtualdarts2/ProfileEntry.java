package com.example.virtualdarts2;

public class ProfileEntry {
    String user_ID;
    String first_name;
    String last_name;
    int age;

    public ProfileEntry() {}

    public ProfileEntry(String user_ID,  String first_name, String last_name, int age) {
        this.user_ID = user_ID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age =  age;

    }

    public String getUser_ID() {
        return this.user_ID;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public int getAge() {
        return this.age;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
