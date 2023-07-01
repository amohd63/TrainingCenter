package com.example.trainingcenter.Model;

import java.util.regex.Pattern;

public class Trainee extends User{
    private String email;
    private String firstName;
    private String lastName;
    private String pictureObj;
    private String mobileNumber;
    private String address;

    public Trainee() {
    }

    public Trainee(String email, String firstName, String lastName, String pictureObj) {
        super(email, firstName, lastName, pictureObj);
    }

    public Trainee(String email, String firstName, String lastName, String pictureObj, String mobileNumber, String address) {
        super(email, firstName, lastName, pictureObj);
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        String regex = "^05\\d{8}$";
        return Pattern.matches(regex, mobileNumber);
    }

    public String getFullName(){
        return this.getFirstName() + " " + this.getLastName();
    }
}
