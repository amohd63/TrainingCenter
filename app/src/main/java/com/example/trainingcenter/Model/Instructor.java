package com.example.trainingcenter.Model;

import java.util.regex.Pattern;

public class Instructor extends User {
    private String email;
    private String firstName;
    private String lastName;
    private String pictureObj;
    private String mobileNumber;
    private String address;
    private String specialization;
    private Degree degree;
    public enum Degree{
        BSc,
        MSc,
        PhD
    };

    public Instructor() {
    }

    public Instructor(String email, String firstName, String lastName, String pictureObj, String mobileNumber, String address, String specialization, Degree degree) {
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

    public String getDegree() {
        // Return the degree as a string
        return degree != null ? degree.name() : null;
    }

    public void setDegree(String degree) {

        // Check if the string matches any of the enum values
        for (Degree d : Degree.values()) {
            if (d.name().equals(degree)) {
                this.degree = d;
                return;
            }
        }

        // Handle the case when the string does not match any enum value
        throw new IllegalArgumentException("Invalid degree: " + degree);
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        String regex = "^\\+970\\d{9}$";
        return Pattern.matches(regex, mobileNumber);
    }

    public String getFullName(){
        return this.getFirstName() + " " + this.getLastName();
    }
}
