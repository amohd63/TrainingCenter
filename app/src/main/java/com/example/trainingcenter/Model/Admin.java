package com.example.trainingcenter.Model;

public class Admin extends User{
    private String email;
    private String firstName;
    private String lastName;
    private String pictureObj;

    public Admin() {
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPictureObj() {
        return pictureObj;
    }

    @Override
    public void setPictureObj(String pictureObj) {
        this.pictureObj = pictureObj;
    }

    public Admin(String email, String firstName, String lastName, String pictureObj) {
        super(email, firstName, lastName, pictureObj);
    }
    public String getFullName(){
        return this.getFirstName() + " " + this.getLastName();
    }
}
