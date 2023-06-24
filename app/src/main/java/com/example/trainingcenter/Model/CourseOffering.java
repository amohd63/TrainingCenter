package com.example.trainingcenter.Model;

import com.google.firebase.Timestamp;

public class CourseOffering {
    private String offeringID;
    private String courseID;
    private String instructorID;
    private Timestamp registrationDeadline;
    private Timestamp startDate;
    private String schedule;
    private String venue;

    public CourseOffering() {
    }

    public CourseOffering(String offeringID, String courseID, String instructorID, Timestamp registrationDeadline, Timestamp startDate, String schedule, String venue) {
        this.offeringID = offeringID;
        this.courseID = courseID;
        this.instructorID = instructorID;
        this.registrationDeadline = registrationDeadline;
        this.startDate = startDate;
        this.schedule = schedule;
        this.venue = venue;
    }

    public String getOfferingID() {
        return offeringID;
    }

    public void setOfferingID(String offeringID) {
        this.offeringID = offeringID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(String instructorID) {
        this.instructorID = instructorID;
    }

    public Timestamp getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Timestamp registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
