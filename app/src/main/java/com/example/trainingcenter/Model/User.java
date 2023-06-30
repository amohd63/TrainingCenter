package com.example.trainingcenter.Model;
import android.util.Patterns;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String pictureObj;

    public User() {
    }

    public User(String email, String firstName, String lastName, String pictureObj) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pictureObj = pictureObj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPictureObj() {
        return pictureObj;
    }

    public void setPictureObj(String pictureObj) {
        this.pictureObj = pictureObj;
    }

    public boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidFirstName_2() {
        return firstName.length() >= 3 && firstName.length() <= 20;
    }

    public static boolean isValidName(String name) {
        return name.length() >= 3 && name.length() <= 20;
    }

    public boolean isValidLastName() {
        return lastName.length() >= 3 && lastName.length() <= 20;
    }
}
