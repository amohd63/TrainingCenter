package com.example.trainingcenter.View.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.widget.TableLayout.LayoutParams;
public class view_previous_offering_course_table extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_view_previous_offering_course_table, null);
        builder.setView(view);

        Bundle args = getArguments();
        String courseTitle = args.getString("pointer");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.data_to_be_displayed_in_offreing_courses_Data);
        ImageButton cloasOffaring = view.findViewById(R.id.close_reportNumTow_dialog_now);

        CollectionReference courseRef = db.collection("Course");
        Query courseQuery = courseRef.whereEqualTo("courseTitle", courseTitle);
        courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot courseDoc : task.getResult()) {
                        String courseId = courseDoc.getId();
                        // 2. Retrieve offering details by courseID
                        CollectionReference offeringRef = db.collection("CourseOffering");
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId);
                        offeringQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot offeringDoc : task.getResult()) {
                                        String offeringId = offeringDoc.getId();
                                        String schedule[] = offeringDoc.getString("schedule").split(" ");
                                        String date = schedule[0];
                                        String time = schedule[1];
                                        String venue = offeringDoc.getString("venue");
                                        String instructorEmail = offeringDoc.getString("instructorID");
                                        // 3. Retrieve number of students by offeringID
                                        CollectionReference registrationRef = db.collection("Registration");
                                        Query registrationQuery = registrationRef.whereEqualTo("offeringID", offeringId);
                                        registrationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int studentCount = task.getResult().size();
                                                    // 4. Retrieve instructor details by instructorEmail
                                                    CollectionReference userRef = db.collection("User");
                                                    Query userQuery = userRef.whereEqualTo("email" , instructorEmail);
                                                    userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot userDoc : task.getResult()) {
                                                                    String instructorName = userDoc.getString("firstName") + " " + userDoc.getString("lastName");
                                                                    TableRow row = new TableRow(getContext());
                                                                    String hexColor = "#000000"; // Example hex color value
                                                                    int color = Color.parseColor(hexColor);
                                                                    String firstFiveChars = courseId.substring(0, Math.min(courseId.length(), 5));

                                                                    TextView IDTextView = new TextView(getContext());
                                                                    IDTextView.setText(firstFiveChars + "...");
                                                                    IDTextView.setTextSize(12);
                                                                    IDTextView.setGravity(Gravity.CENTER);
                                                                    IDTextView.setWidth(85);
                                                                    IDTextView.setTextColor(color);

                                                                    TextView courseTitleTextView = new TextView(getContext());
                                                                    courseTitleTextView.setText(courseTitle);
                                                                    courseTitleTextView.setTextSize(12);
                                                                    courseTitleTextView.setGravity(Gravity.CENTER);
                                                                    courseTitleTextView.setWidth(250);
                                                                    courseTitleTextView.setTextColor(color);

                                                                    TextView dateTextView = new TextView(getContext());
                                                                    dateTextView.setText(date);
                                                                    dateTextView.setTextSize(12);
                                                                    dateTextView.setGravity(Gravity.CENTER);
                                                                    dateTextView.setWidth(120);
                                                                    dateTextView.setTextColor(color);

                                                                    TextView timeTextView = new TextView(getContext());
                                                                    timeTextView.setText(time);
                                                                    timeTextView.setTextSize(12);
                                                                    timeTextView.setGravity(Gravity.CENTER);
                                                                    timeTextView.setWidth(150);
                                                                    timeTextView.setTextColor(color);

                                                                    TextView sTextView = new TextView(getContext());
                                                                    sTextView.setText(String.valueOf(studentCount));
                                                                    sTextView.setTextSize(12);
                                                                    sTextView.setGravity(Gravity.CENTER);
                                                                    sTextView.setWidth(150);
                                                                    sTextView.setTextColor(color);

                                                                    TextView venueTextView = new TextView(getContext());
                                                                    venueTextView.setText(venue);
                                                                    venueTextView.setTextSize(12);
                                                                    venueTextView.setGravity(Gravity.CENTER);
                                                                    venueTextView.setWidth(120);
                                                                    venueTextView.setTextColor(color);

                                                                    TextView instructorNameTextView = new TextView(getContext());
                                                                    instructorNameTextView.setText(instructorName);
                                                                    instructorNameTextView.setTextSize(12);
                                                                    instructorNameTextView.setGravity(Gravity.CENTER);
                                                                    instructorNameTextView.setWidth(200);
                                                                    instructorNameTextView.setTextColor(color);

                                                                    row.addView(IDTextView);
                                                                    row.addView(courseTitleTextView);
                                                                    row.addView(dateTextView);
                                                                    row.addView(timeTextView);
                                                                    row.addView(sTextView);
                                                                    row.addView(venueTextView);
                                                                    row.addView(instructorNameTextView);
                                                                    tableLayout.addView(row);
                                                                }

                                                            } else {
                                                                Log.w("Firestore", "Error getting documents.", task.getException());
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Log.w("Firestore", "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.w("Firestore", "Error getting documents.", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            }
        });
        cloasOffaring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeAllViews();
                dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}