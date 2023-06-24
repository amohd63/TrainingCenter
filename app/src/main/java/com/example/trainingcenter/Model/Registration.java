package com.example.trainingcenter.Model;

public class Registration {
    private String registrationID;
    private String offeringID;
    private String traineeID;
    private enum status {
        PENDING,
        ACCEPTED,
        REJECTED
    };

}
