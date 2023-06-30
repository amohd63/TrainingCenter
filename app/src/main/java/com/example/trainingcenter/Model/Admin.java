package com.example.trainingcenter.Model;

public class Admin extends User{
    private String email;
    private String firstName;
    private String lastName;
    private String pictureObj;

    public Admin() {
    }

    public Admin(String email, String firstName, String lastName, String pictureObj) {
        super(email, firstName, lastName, pictureObj);
    }
}
