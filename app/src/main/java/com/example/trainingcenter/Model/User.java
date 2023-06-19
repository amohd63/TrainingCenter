package com.example.trainingcenter.Model;
import android.util.Patterns;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private PictureObject pictureObj;

    public User() {
    }

    public User(String email, String firstName, String lastName, String password, PictureObject pictureObj) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PictureObject getPictureObj() {
        return pictureObj;
    }

    public void setPictureObj(PictureObject pictureObj) {
        this.pictureObj = pictureObj;
    }

    public boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidFirstName() {
        return firstName.length() >= 3 && firstName.length() <= 20;
    }

    public boolean isValidLastName() {
        return lastName.length() >= 3 && lastName.length() <= 20;
    }

    public boolean isValidPassword() {
        // Minimum 8 characters, at least one uppercase letter, one lowercase letter, and one digit
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$";
        return password.matches(passwordPattern);
    }
}
