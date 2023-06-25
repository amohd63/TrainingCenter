package com.example.trainingcenter.Model;

public class Course {
    private String courseID;
    private String courseTitle;
    private String courseTopics[];
    private PictureObject coursePhoto;
    private boolean isAvailableForRegistration;

    public Course() {
    }

    public Course(String courseID, String courseTitle, String[] courseTopics, PictureObject coursePhoto, boolean isAvailableForRegistration) {
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

    public String[] getCourseTopics() {
        return courseTopics;
    }

    public void setCourseTopics(String[] courseTopics) {
        this.courseTopics = courseTopics;
    }

    public PictureObject getCoursePhoto() {
        return coursePhoto;
    }

    public void setCoursePhoto(PictureObject coursePhoto) {
        this.coursePhoto = coursePhoto;
    }

    public boolean isAvailableForRegistration() {
        return isAvailableForRegistration;
    }

    public void setAvailableForRegistration(boolean availableForRegistration) {
        isAvailableForRegistration = availableForRegistration;
    }


}
