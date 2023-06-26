package com.example.trainingcenter.Model;

import java.util.List;

public class Course {
    private String courseID;
    private String courseTitle;
    private List<String> courseTopics;
    private String coursePhoto;
    private boolean isAvailableForRegistration;

    public Course() {
    }

    public Course(String courseID, String courseTitle, List<String> courseTopics, String coursePhoto, boolean isAvailableForRegistration) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseTopics = courseTopics;
        this.coursePhoto = coursePhoto;
        this.isAvailableForRegistration = isAvailableForRegistration;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public List<String> getCourseTopics() {
        return courseTopics;
    }

    public void setCourseTopics(List<String> courseTopics) {
        this.courseTopics = courseTopics;
    }

    public String getCoursePhoto() {
        return coursePhoto;
    }

    public void setCoursePhoto(String coursePhoto) {
        this.coursePhoto = coursePhoto;
    }

    public boolean isAvailableForRegistration() {
        return isAvailableForRegistration;
    }

    public void setAvailableForRegistration(boolean availableForRegistration) {
        isAvailableForRegistration = availableForRegistration;
    }


}
