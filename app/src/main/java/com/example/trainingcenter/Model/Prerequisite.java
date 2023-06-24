package com.example.trainingcenter.Model;

public class Prerequisite {
    private String prerequisiteID;
    private String courseID;
    private String prerequisiteCourseID;

    public Prerequisite() {
    }

    public Prerequisite(String prerequisiteID, String courseID, String prerequisiteCourseID) {
        this.prerequisiteID = prerequisiteID;
        this.courseID = courseID;
        this.prerequisiteCourseID = prerequisiteCourseID;
    }

    public String getPrerequisiteID() {
        return prerequisiteID;
    }

    public void setPrerequisiteID(String prerequisiteID) {
        this.prerequisiteID = prerequisiteID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getPrerequisiteCourseID() {
        return prerequisiteCourseID;
    }

    public void setPrerequisiteCourseID(String prerequisiteCourseID) {
        this.prerequisiteCourseID = prerequisiteCourseID;
    }
}
