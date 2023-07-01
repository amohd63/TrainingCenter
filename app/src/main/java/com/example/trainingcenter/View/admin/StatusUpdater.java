package com.example.trainingcenter.View.admin;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class StatusUpdater extends TimerTask {

    @Override
    public void run() {
        // Get a reference to the Firestore collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference offeringRef = firestore.collection("CourseOffering");

        // Retrieve the course offerings from Firestore
        offeringRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Timestamp startDate = document.getTimestamp("startDate");
                        String status;

                        if (startDate != null) {
                            // Retrieve the start date value from the Timestamp
                            Date startDateTime = startDate.toDate();
                            Calendar startCalendar = Calendar.getInstance();
                            startCalendar.setTime(startDateTime);

                            // Calculate the end date by adding 4 months to the start date
                            Calendar endCalendar = Calendar.getInstance();
                            endCalendar.setTime(startDateTime);
                            endCalendar.add(Calendar.MONTH, 4);
                            Date endDateTime = endCalendar.getTime();

                            // Get the current date and time
                            Calendar currentCalendar = Calendar.getInstance();
                            Date currentDateTime = currentCalendar.getTime();

                            // Compare the current date with the start and end dates to determine the status
                            if (currentDateTime.compareTo(startDateTime) >= 0 && currentDateTime.compareTo(endDateTime) <= 0) {
                                // Current date is between the start date and end date
                                // Set status as "on going"
                                status = "on going";
                            } else if (currentDateTime.compareTo(startDateTime) < 0) {
                                // Current date is before the start date
                                // Set status as "pending"
                                status = "pending";
                            } else {
                                // Current date is after the end date
                                // Set status as "ended"
                                status = "ended";
                            }

                            // Update the status field in Firestore
                            document.getReference().update("status", status);
                        }
                    }
                }
            } else {
                System.out.println("filed to update status");
            }
        });
    }
}