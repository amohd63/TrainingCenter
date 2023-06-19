package com.example.trainingcenter.Users;

import com.example.trainingcenter.Utils.PictureObject;

import java.util.regex.Pattern;

public class Trainee extends User{
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private PictureObject pictureObj;
    private String mobileNumber;
    private String address;

    public Trainee() {
    }

    public Trainee(String email, String firstName, String lastName, String password, PictureObject pictureObj, String mobileNumber, String address) {
        super(email, firstName, lastName, password, pictureObj);
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
        String regex = "^\\+970\\d{9}$";
        return Pattern.matches(regex, mobileNumber);
    }
}
