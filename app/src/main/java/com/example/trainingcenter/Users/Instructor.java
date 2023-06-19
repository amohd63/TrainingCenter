package com.example.trainingcenter.Users;

import com.example.trainingcenter.Utils.PictureObject;

import java.util.regex.Pattern;

public class Instructor extends User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private PictureObject pictureObj;
    private String mobileNumber;
    private String address;
    private String specialization;
    private String degree;

    public Instructor() {
    }

    public Instructor(String email, String firstName, String lastName, String password, PictureObject pictureObj, String mobileNumber, String address, String specialization, String degree) {
        super(email, firstName, lastName, password, pictureObj);
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.specialization = specialization;
        this.degree = degree;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        String regex = "^\\+970\\d{9}$";
        return Pattern.matches(regex, mobileNumber);
    }

    public static boolean isValidDegree(String degree) {
        String[] validDegrees = {"BSc", "MSc", "PhD"};
        for (String validDegree : validDegrees) {
            if (degree.equalsIgnoreCase(validDegree)) {
                return true;
            }
        }
        return false;
    }
}
