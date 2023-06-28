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

public class show_table_for_course_and_student extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_show_table_for_course_and_student, null);
        builder.setView(view);
        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.data_to_be_displayed);
        ImageButton cloarReport = view.findViewById(R.id.close_reportNumOne_dialog_now);

        Bundle args = getArguments();
        String courseTitle = args.getString("pointer");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // 1. Retrieve courseID by courseTitle
        CollectionReference courseRef = db.collection("Course");
        Query courseQuery = courseRef.whereEqualTo("courseTitle", courseTitle);
        courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String courseId = document.getId();
                        // 2. Retrieve offeringIDs by courseID
                        CollectionReference offeringRef = db.collection("CourseOffering");
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId);
                        offeringQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String offeringId = document.getId();
                                        // 3. Retrieve emails by offeringID
                                        CollectionReference registrationRef = db.collection("Registration");
                                        Query registrationQuery = registrationRef.whereEqualTo("offeringID", offeringId);
                                        registrationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String email = document.getString("traineeID");
                                                        String status = document.getString("status");
                                                        // 4. Retrieve user details by email
                                                        CollectionReference userRef = db.collection("User");
                                                        Query userQuery = userRef.whereEqualTo("email", email);
                                                        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        if(status.equals("Accepted")) {
                                                                            TableRow row = new TableRow(getContext());
                                                                            String hexColor = "#000000"; // Example hex color value
                                                                            int color = Color.parseColor(hexColor);

                                                                            String email = document.getString("email");
                                                                            String firstName = document.getString("firstName");
                                                                            String lastName = document.getString("lastName");

                                                                            TextView IDTextView = new TextView(getContext());
                                                                            IDTextView.setText(email);
                                                                            IDTextView.setTextSize(12);
                                                                            IDTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                            IDTextView.setTextColor(color);
                                                                            IDTextView.setPadding(5, 0, 5, 0);
                                                                            IDTextView.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                                                                            TextView FnameTextView = new TextView(getContext());
                                                                            FnameTextView.setText(firstName);
                                                                            FnameTextView.setTextSize(12);
                                                                            FnameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                            FnameTextView.setTextColor(color);
                                                                            FnameTextView.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                                                            FnameTextView.setPadding(170,0,0,0);

                                                                            TextView LnameTextView = new TextView(getContext());
                                                                            LnameTextView.setText(lastName);
                                                                            LnameTextView.setTextSize(12);
                                                                            LnameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                            LnameTextView.setTextColor(color);
                                                                            LnameTextView.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                                                            LnameTextView.setPadding(210,0,0,0);


                                                                            row.addView(IDTextView);
                                                                            row.addView(FnameTextView);
                                                                            row.addView(LnameTextView);
                                                                            tableLayout.addView(row);
                                                                        }
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

        cloarReport.setOnClickListener(new View.OnClickListener() {
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