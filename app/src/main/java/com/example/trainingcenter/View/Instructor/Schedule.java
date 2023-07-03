package com.example.trainingcenter.View.Instructor;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class Schedule extends AppCompatActivity {
    private TextView Sun[] = new TextView[6];
    private TextView Mon[] = new TextView[6];
    private TextView Tue[] = new TextView[6];
    private TextView Wed[] = new TextView[6];
    private TextView Thu[] = new TextView[6];
    private TextView Fri[] = new TextView[6];
    private TextView Sat[] = new TextView[6];
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule_instructor);
        dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        Sun[0] = findViewById(R.id.Sunday1);
        Sun[1] = findViewById(R.id.Sunday2);
        Sun[2] = findViewById(R.id.Sunday3);
        Sun[3] = findViewById(R.id.Sunday4);
        Sun[4] = findViewById(R.id.Sunday5);
        Sun[5] = findViewById(R.id.Sunday6);

        Mon[0] = findViewById(R.id.Monday1);
        Mon[1] = findViewById(R.id.Monday2);
        Mon[2] = findViewById(R.id.Monday3);
        Mon[3] = findViewById(R.id.Monday4);
        Mon[4] = findViewById(R.id.Monday5);
        Mon[5] = findViewById(R.id.Monday6);

        Tue[0] = findViewById(R.id.Tuesday1);
        Tue[1] = findViewById(R.id.Tuesday2);
        Tue[2] = findViewById(R.id.Tuesday3);
        Tue[3] = findViewById(R.id.Tuesday4);
        Tue[4] = findViewById(R.id.Tuesday5);
        Tue[5] = findViewById(R.id.Tuesday6);

        Wed[0] = findViewById(R.id.Wednesday1);
        Wed[1] = findViewById(R.id.Wednesday2);
        Wed[2] = findViewById(R.id.Wednesday3);
        Wed[3] = findViewById(R.id.Wednesday4);
        Wed[4] = findViewById(R.id.Wednesday5);
        Wed[5] = findViewById(R.id.Wednesday6);


        Thu[0] = findViewById(R.id.Thursday1);
        Thu[1] = findViewById(R.id.Thursday2);
        Thu[2] = findViewById(R.id.Thursday3);
        Thu[3] = findViewById(R.id.Thursday4);
        Thu[4] = findViewById(R.id.Thursday5);
        Thu[5] = findViewById(R.id.Thursday6);


        Fri[0] = findViewById(R.id.Friday1);
        Fri[1] = findViewById(R.id.Friday2);
        Fri[2] = findViewById(R.id.Friday3);
        Fri[3] = findViewById(R.id.Friday4);
        Fri[4] = findViewById(R.id.Friday5);
        Fri[5] = findViewById(R.id.Friday6);


        Sat[0] = findViewById(R.id.Saturday1);
        Sat[1] = findViewById(R.id.Saturday2);
        Sat[2] = findViewById(R.id.Saturday3);
        Sat[3] = findViewById(R.id.Saturday4);
        Sat[4] = findViewById(R.id.Saturday5);
        Sat[5] = findViewById(R.id.Saturday6);
        performQuery(email);


//        Sun1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Sun1.setText("NLP\nMASRI204");
//            }
//        });
//        Sun2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });
//        Sun3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        Sun4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        Sun5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        Sun6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        Mon1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Mon2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        Mon3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Mon4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        Mon5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        Mon6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        Tue1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Tue2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Tue3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Tue4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Tue5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        Tue6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//
//        Wed1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Wed2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Wed3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Wed4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Wed5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Wed6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        Thu1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Thu2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Thu3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Thu4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Thu5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Thu6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//
//        Fri1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Fri2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        Fri3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Fri4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        Fri5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Fri6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        Sat1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Sat2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Sat3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Sat4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        Sat5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        Sat6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }
    }

    private void performQuery(String instructorEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference courseRef = db.collection("CourseOffering");
        Query courseQuery = courseRef.whereEqualTo("instructorID", instructorEmail);
        courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentOfferings : task.getResult()) {
                        String courseId = documentOfferings.getString("courseID");
                        // 2. Retrieve offeringIDs by courseID
                        CollectionReference offeringRef = db.collection("Course");
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId);
                        offeringQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String courseTitle = document.getString("courseTitle");
                                                        String courseTime = documentOfferings.getString("schedule").split(" ")[1];
                                                        String date = dateFormat.format(documentOfferings.getTimestamp("startDate").toDate());
                                                        String insName = document.getString("courseTitle");
                                                        String days = documentOfferings.getString("schedule").split(" ")[0];
                                                        String[] day = days.split(",");
                                                        String time= courseTime.split("-")[0];
                                                        String venue = documentOfferings.getString("venue");

                                                        if(courseTitle.equals("Test4")){
                                                            // THIS COURSE HAS INVALID DATE
                                                        }
                                                        else{
                                                            int i = calcPos(time);
                                                            if(day[0].equals("S") || day[1].equals("S")){
                                                                Sat[i].setText(courseTitle+"\n"+venue);
                                                            }
                                                            if(day[0].equals("M") || day[1].equals("M")){
                                                                Mon[i].setText(courseTitle+"\n"+venue);
                                                            }
                                                            if(day[0].equals("T") || day[1].equals("T")){
                                                                Thu[i].setText(courseTitle+"\n"+venue);
                                                            }
                                                            if(day[0].equals("W") || day[1].equals("W")){
                                                                Wed[i].setText(courseTitle+"\n"+venue);
                                                            }
                                                            if(day[0].equals("R") || day[1].equals("R")){
                                                                Thu[i].setText(courseTitle+"\n"+venue);
                                                            }
                                                            if(day[0].equals("F") || day[1].equals("F")){
                                                                Fri[i].setText(courseTitle+"\n"+venue);
                                                            }
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
    private int calcPos(String time){
        switch (time){
            case "8:30":
                return 0;
            case "10:00":
                return 1;
            case "11:30":
                return 2;
            case "13:30":
                return 3;
            case "15:00":
                return 4;
            case "16:30":
                return 5;
            default:
                return 5;
        }

    }
}
