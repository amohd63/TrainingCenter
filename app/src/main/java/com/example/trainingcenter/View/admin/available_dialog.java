package com.example.trainingcenter.View.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.R;
import com.example.trainingcenter.View.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class available_dialog extends AppCompatDialogFragment {
    private FirebaseFirestore db;
    String[] dayArray = {"T,R", "M,W", "S,M", "S,W"};
    String[] timeArray = {"08:30-10:00", "10:00-11:30", "11:30-13:00", "13:30-15:00", "15:00-16:30", "16:30-18:00"};
    private AutoCompleteTextView address;
    AlertDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_available_dialog, null);
        builder.setView(view);
        Bundle args = getArguments();
        String documentPath = args.getString("pointer");
        String emailForAdmin = args.getString("emailId");
        String titleAdmin = args.getString("title");
        dialog = builder.create();
        address = view.findViewById(R.id.instroctor_add_spinner);
        DocumentReference docRef = FirebaseFirestore.getInstance().document(documentPath);
        ArrayList<String> myList = new ArrayList<>();
        TextView registration_deadline = view.findViewById(R.id.new_registration_in_make_available);
        TextView course_start_date = view.findViewById(R.id.new_start_date_in_make_available);
        TextView course_schedule = view.findViewById(R.id.new_course_schedule_in_make_available);
        EditText venue = view.findViewById(R.id.new_venue_in_make_available);
        Button mKa = view.findViewById(R.id.make_available_in_make_available_daialog);
        ImageButton mma = view.findViewById(R.id.close_make_availbal_dialog_now);

        registration_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Registration deadline")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date(selection));
                        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder().setTitleText("Select a time")
                                .setTimeFormat(TimeFormat.CLOCK_24H)
                                .build();
                        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int hour = materialTimePicker.getHour();
                                int minute = materialTimePicker.getMinute();
                                registration_deadline.setText(date + " " + hour + ":" + minute + ":" + "00");
                            }
                        });
                        materialTimePicker.show(getParentFragmentManager(), "MATERIAL_TIME_PICKER");
                    }
                });
                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        course_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SingleChoiceDialogFragment(dayArray, timeArray, course_schedule).show(getParentFragmentManager(), "single_choice_dialog");
            }
        });

        course_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Course start date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date(selection));
                        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder().setTitleText("Select a time")
                                .setTimeFormat(TimeFormat.CLOCK_24H)
                                .build();
                        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int hour = materialTimePicker.getHour();
                                int minute = materialTimePicker.getMinute();
                                course_start_date.setText(date + " " + hour + ":" + minute + ":" + "00");
                            }
                        });
                        materialTimePicker.show(getParentFragmentManager(), "MATERIAL_TIME_PICKER");
                    }
                });
                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });


        db = FirebaseFirestore.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("User");
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String email = document.getId();
                        CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Instructor");
                        collectionRef2.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                QuerySnapshot querySnapshot2 = task1.getResult();
                                if (querySnapshot2 != null) {
                                    for (QueryDocumentSnapshot document2 : querySnapshot2) {
                                        myList.add(email);
                                    }
                                    String[] options = myList.toArray(new String[myList.size()]);
                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    address.setAdapter(spinnerAdapter);
                                }
                            }
                        });
                    }
                }
            }
        });
        final boolean[] flag2 = {false};
        mKa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registration_deadline.getText().toString().isEmpty()){
                    Toast.makeText(dialog.getContext(), "Please select a registration deadline for the section", Toast.LENGTH_SHORT).show();
                } else if (course_start_date.getText().toString().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Please select a course start date for the section", Toast.LENGTH_SHORT).show();
                } else if (course_schedule.getText().toString().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Please select a course schedule for the section", Toast.LENGTH_SHORT).show();
                } else if (venue.getText().toString().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Please select a venue for the section", Toast.LENGTH_SHORT).show();
                } else if (address.getText().toString().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Please select a instructor for the section", Toast.LENGTH_SHORT).show();
                } else {
                    Timestamp timestamp1 = Timestamp.valueOf(registration_deadline.getText().toString());
                    Timestamp timestamp2 = Timestamp.valueOf(course_start_date.getText().toString());
                    String venuePlace = venue.getText().toString();
                    String schedule = course_schedule.getText().toString();
                    String instroctorw = address.getText().toString();
                    UUID uuid = UUID.randomUUID();
                    String offeringID = uuid.toString().replace("-", "").substring(0, 20);
                    String cId = docRef.getId();
                    DocumentReference docRef2 = db.collection("User").document(instroctorw).collection("Instructor").document(instroctorw);
                    docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    List<String> coursesList1 = ((List<String>) document.get("courses"));
                                    String[] coursesList = coursesList1.toArray(new String[coursesList1.size()]);
                                    boolean flag = false;
                                    for (int x = 0; x < coursesList.length; x++) {
                                        if (coursesList[x].equals(titleAdmin)) {

                                            Map<String, Object> offer = new HashMap<>();
                                            offer.put("registrationDeadline", timestamp1);
                                            offer.put("startDate", timestamp2);
                                            offer.put("venue", venuePlace);
                                            offer.put("schedule", schedule);
                                            offer.put("offeringID", offeringID);
                                            offer.put("courseID", cId);
                                            offer.put("instructorID", instroctorw);
                                            offer.put("status", "pending");

                                            //notification
                                            LocalDateTime currentDateTime = LocalDateTime.now();
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                            String formattedDateTime = currentDateTime.format(formatter);
                                            Timestamp timestampNote = Timestamp.valueOf(formattedDateTime);

                                            String title = "New course";
                                            String body = "The course " + titleAdmin + " is now available for registration";
                                            UUID uuid2 = UUID.randomUUID();
                                            String noteID = uuid.toString().replace("-", "").substring(0, 20);
                                            Map<String, Object> note = new HashMap<>();
                                            note.put("body", body);
                                            note.put("title", title);
                                            note.put("userID", "all");
                                            note.put("noteDate", timestampNote);
                                            note.put("fetch", false);

                                            db.collection("CourseOffering").document(offeringID).set(offer);
                                            db.collection("Notification").document(noteID).set(note);

                                            CollectionReference collectionRef = firestore.collection("Course");
                                            DocumentReference docRef = collectionRef.document(cId);
                                            docRef.update("isAvailableForRegistration", true)
                                                    .addOnSuccessListener(aVoid -> System.out.println("Document updated successfully"))
                                                    .addOnFailureListener(e -> System.out.println("Error updating document: " + e.getMessage()));

                                            flag = true;
                                            dismiss();
                                            getActivity().recreate();
                                        } else {
                                        }
                                    }
                                    if (!flag) {
                                        Toast.makeText(dialog.getContext(), "instructor doesn't teach this course", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
        mma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}