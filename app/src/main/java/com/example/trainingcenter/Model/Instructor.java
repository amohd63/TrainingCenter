package com.example.trainingcenter.Model;

import java.util.regex.Pattern;

public class Instructor extends User {
    private String email;
    private String firstName;
    private String lastName;
    private PictureObject pictureObj;
    private String mobileNumber;
    private String address;
    private String specialization;
    private Degree degree;
    private enum Degree{
        BSc,
        MSc,
        PhD
    };

    public Instructor() {
    }

    public Instructor(String email, String firstName, String lastName, PictureObject pictureObj, String mobileNumber, String address, String specialization, Degree degree) {
        super(email, firstName, lastName, pictureObj);
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

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        String regex = "^\\+970\\d{9}$";
        return Pattern.matches(regex, mobileNumber);
    }
}
