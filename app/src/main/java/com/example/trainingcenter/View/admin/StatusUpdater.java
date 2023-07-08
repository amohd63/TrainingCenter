package com.example.trainingcenter.View.admin;

import android.util.Log;

import androidx.cardview.widget.CardView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

public class StatusUpdater extends TimerTask {

    @Override
    public void run() {
        // Get a reference to the Firestore collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference offeringRef = firestore.collection("CourseOffering");
        offeringRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Timestamp startDate = document.getTimestamp("startDate");
                        String status;

                        if (startDate != null) {
                            Date date1 = startDate.toDate(); // Your first date object
                            Date date2 = Timestamp.now().toDate(); // Your second date object

                            // Create calendar instances for both dates
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(date1);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(date2);

                            // Clear the time portion of both calendars
                            cal1.set(Calendar.HOUR_OF_DAY, 0);
                            cal1.set(Calendar.MINUTE, 0);
                            cal1.set(Calendar.SECOND, 0);
                            cal1.set(Calendar.MILLISECOND, 0);
                            cal2.set(Calendar.HOUR_OF_DAY, 0);
                            cal2.set(Calendar.MINUTE, 0);
                            cal2.set(Calendar.SECOND, 0);
                            cal2.set(Calendar.MILLISECOND, 0);

                            // Compare the dates without considering the time
                            int comparison = cal1.compareTo(cal2);
                            // Output the result
                            if (comparison == 0) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                                SimpleDateFormat stf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                String date = sdf.format(startDate.toDate());
                                String time = stf.format(startDate.toDate());
                                firestore.collection("Registration")
                                        .whereEqualTo("offeringID", document.getString("offeringID"))
                                        .whereEqualTo("status", "Accepted")
                                        .get()
                                        .addOnCompleteListener(userTask -> {
                                            if (userTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot regDoc : userTask.getResult()) {
                                                    firestore.collection("Course")
                                                            .whereEqualTo("courseID", document.getString("courseID"))
                                                            .get()
                                                            .addOnCompleteListener(courseTask -> {
                                                                if (courseTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {

                                                                        String courseTitle = courseDoc.getString("courseTitle");
                                                                        Log.d("StatusUpdater ", regDoc.getString("traineeID"));
                                                                        String title = "Reminder | " + courseTitle;
                                                                        String body = "Today [" + date + "] at " + time + ", "+courseTitle+" lectures begin. Please be ready!";
                                                                        Map<String, Object> note = new HashMap<>();
                                                                        note.put("body",body);
                                                                        note.put("title",title);
                                                                        note.put("userID", regDoc.getString("traineeID"));
                                                                        Timestamp timestampNote = Timestamp.now();
                                                                        note.put("noteDate",timestampNote);
                                                                        note.put("fetch", false);
                                                                        //firestore.collection("Notification").document().set(note);
                                                                    }

                                                                } else {
                                                                }
                                                            });
                                                }

                                            } else {
                                            }
                                        });
                            }


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
                                status = "Ongoing";
                            } else if (currentDateTime.compareTo(startDateTime) < 0) {
                                // Current date is before the start date
                                // Set status as "pending"
                                status = "Pending";
                            } else {
                                // Current date is after the end date
                                // Set status as "ended"
                                status = "Ended";
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