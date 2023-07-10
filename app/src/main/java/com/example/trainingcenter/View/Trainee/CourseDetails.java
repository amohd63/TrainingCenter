package com.example.trainingcenter.View.Trainee;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.Model.CourseOffering;
import com.example.trainingcenter.R;
import com.example.trainingcenter.View.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CourseDetails extends AppCompatDialogFragment {
    private CustomDialogListener listener;
    private Course course;
    private CourseOffering courseOffering;
    private String instructor;
    private String email;
    private FirebaseFirestore db;
    AlertDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_trainee, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        db = FirebaseFirestore.getInstance();
        ImageButton close = view.findViewById(R.id.course_details_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        TextView courseTitle = view.findViewById(R.id.course_title);
        courseTitle.setText(course.getCourseTitle());

        Button enroll = view.findViewById(R.id.enroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        ImageView coursePhoto = view.findViewById(R.id.course_photo);
        Picasso.get().load(course.getCoursePhoto()).into(coursePhoto);

        LinearLayout prerequisite = view.findViewById(R.id.prerequisite);
        LinearLayout mainTopics = view.findViewById(R.id.main_topics);
        LinearLayout generalInformation = view.findViewById(R.id.general_information);
        LinearLayout conflicts = view.findViewById(R.id.conflicts_view);

        mainTopics.removeAllViews();
        prerequisite.removeAllViews();
        generalInformation.removeAllViews();
        conflicts.removeAllViews();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        for (String topic : course.getCourseTopics()) {
            CardView topicCV = createTopicCardView(topic);
            mainTopics.addView(topicCV);
        }

        final boolean[] flag = {false, false};
        final int[] i = {0, 0};
        final int[] size = {1, 0};
        final boolean[] clearFlag = {false};
        CardView emptyPre = createEmptyCardView("No prerequisites");
        prerequisite.addView(emptyPre);
        db.collection("Prerequisite")
                .whereEqualTo("courseID", course.getCourseID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        size[0] = task.getResult().size();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot prerequisiteDoc : task.getResult()) {
                                db.collection("Course")
                                        .whereEqualTo("courseID", prerequisiteDoc.getString("prerequisiteCourseID"))
                                        .get()
                                        .addOnCompleteListener(courseTask -> {
                                            if (courseTask.isSuccessful()) {
                                                if (courseTask.getResult().isEmpty()){
                                                    i[0]++;
                                                }
                                                for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                                    db.collection("CourseOffering")
                                                            .whereEqualTo("courseID", courseDoc.getString("courseID"))
                                                            .get()
                                                            .addOnCompleteListener(courseOfferingTask -> {
                                                                if (courseOfferingTask.isSuccessful()) {
                                                                    size[1] = courseOfferingTask.getResult().size();
                                                                    flag[1] = false;
                                                                    if (courseOfferingTask.getResult().isEmpty()) {
                                                                        CardView prerequisiteCardView = createPrerequisiteCardView(courseDoc.getString("courseTitle"), "Uncompleted", 0);
                                                                        if (!clearFlag[0]){
                                                                            clearFlag[0] = true;
                                                                            prerequisite.removeAllViews();
                                                                        }
                                                                        prerequisite.addView(prerequisiteCardView);
                                                                        flag[0] = false;
                                                                        i[0]++;
                                                                    } else {
                                                                        for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                                            db.collection("Registration")
                                                                                    .whereEqualTo("traineeID", email)
                                                                                    .whereEqualTo("status", "Accepted")
                                                                                    .whereEqualTo("offeringID", courseOfferingDoc.getString("offeringID"))
                                                                                    .get()
                                                                                    .addOnCompleteListener(registrationTask -> {
                                                                                        if (registrationTask.isSuccessful()) {
                                                                                            Log.d(courseOfferingDoc.getString("status"), " " + String.valueOf(i[1]));
                                                                                            i[1]++;
                                                                                            if (i[1] == size[1]){
                                                                                                if (courseOfferingDoc.getString("status").equals("Ended")) {
                                                                                                    flag[1] = true;
                                                                                                    flag[0] = true;
                                                                                                }
                                                                                                if (flag[1]){
                                                                                                    CardView prerequisiteCardView = createPrerequisiteCardView(courseDoc.getString("courseTitle"), "Completed", 1);
                                                                                                    if (!clearFlag[0]){
                                                                                                        clearFlag[0] = true;
                                                                                                        prerequisite.removeAllViews();
                                                                                                    }
                                                                                                    prerequisite.addView(prerequisiteCardView);
                                                                                                }else{
                                                                                                    Log.d("Joined course details ", "1");
                                                                                                    CardView prerequisiteCardView = createPrerequisiteCardView(courseDoc.getString("courseTitle"), "Uncompleted", 0);
                                                                                                    if (!clearFlag[0]){
                                                                                                        clearFlag[0] = true;
                                                                                                        prerequisite.removeAllViews();
                                                                                                    }
                                                                                                    prerequisite.addView(prerequisiteCardView);
                                                                                                }
                                                                                            }else{
                                                                                                if (registrationTask.getResult().isEmpty()){
                                                                                                }else if (courseOfferingDoc.getString("status").equals("Ended")) {
                                                                                                    i[0]++;
                                                                                                    flag[1] = true;
                                                                                                    flag[0] = true;
                                                                                                }
                                                                                            }
                                                                                            i[0]++;
                                                                                        } else {
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                } else {
                                                                }
                                                            });
                                                }
                                            } else {
                                            }
                                        });
                            }
                        } else {
                        }
                    }
                });

        final boolean[] foundConflict = {false};
        final boolean[] courseConflict = {false};
        final int[] j = {0};
        final int[] noOfConflicts = {0};
        final int[] emptyFlag = {0};
        CardView empty = createEmptyCardView("No conflicts");
        conflicts.addView(empty);
        db.collection("Registration")
                .whereEqualTo("traineeID", email)
                .whereIn("status", Arrays.asList("Accepted", "Pending"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                        if (regTask.isSuccessful()) {
                            if (regTask.getResult().isEmpty()) {
                                noOfConflicts[0] = 0;
                                j[0] = 0;
                            } else {
                                noOfConflicts[0] = regTask.getResult().size();
                                for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                    db.collection("CourseOffering")
                                            .whereEqualTo("offeringID", regDoc.getString("offeringID"))
                                            .whereIn("status", Arrays.asList("Ongoing", "Pending"))
                                            .get()
                                            .addOnCompleteListener(courseOfferingTask -> {
                                                if (courseOfferingTask.isSuccessful()) {
                                                    if (courseOfferingTask.getResult().isEmpty()){
                                                        j[0]++;
                                                    }
                                                    for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                        db.collection("Course")
                                                                .whereEqualTo("courseID", courseOfferingDoc.getString("courseID"))
                                                                .get()
                                                                .addOnCompleteListener(courseTask -> {
                                                                    if (courseTask.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                                                            String course1Days = courseOfferingDoc.getString("schedule").split(" ")[0];
                                                                            String[] course2Days = courseOffering.getSchedule().split(" ")[0].split(",");

                                                                            String course1Time = courseOfferingDoc.getString("schedule").split(" ")[1];
                                                                            String course2Time = courseOffering.getSchedule().split(" ")[1];
                                                                            if (course1Days.contains(course2Days[0]) || course1Days.contains(course2Days[1])) {
                                                                                if (course1Time.equals(course2Time)) {
                                                                                    String courseID = courseDoc.getString("courseID").substring(0, 6) + "..";
                                                                                    CardView conflictView = createPrerequisiteCardView(courseDoc.getString("courseTitle"), courseID, 2);
                                                                                    if (emptyFlag[0] == 0){
                                                                                        emptyFlag[0]++;
                                                                                        conflicts.removeAllViews();
                                                                                    }
                                                                                    conflicts.addView(conflictView);
                                                                                    foundConflict[0] = true;
                                                                                }
                                                                            } else if (courseDoc.getString("courseID").equals(course.getCourseID())) {
                                                                                String courseID = courseDoc.getString("courseID").substring(0, 6) + "..";
                                                                                CardView conflictView = createPrerequisiteCardView(courseDoc.getString("courseTitle"), courseID, 2);
                                                                                if (emptyFlag[0] == 0){
                                                                                    emptyFlag[0]++;
                                                                                    conflicts.removeAllViews();
                                                                                }
                                                                                conflicts.addView(conflictView);
                                                                                courseConflict[0] = true;
                                                                            }
                                                                            j[0]++;
                                                                        }
                                                                    } else {
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                }
                                            });
                                }
                            }
                        } else {
                        }
                    }
                });
        //must check deadline, number of students
        Timestamp timestamp = Timestamp.now();
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        String st = String.valueOf(i[0]) + " " + String.valueOf(size[0]);
//                        Toast.makeText(dialog.getContext(), st, Toast.LENGTH_SHORT).show();
                        if (courseOffering.getRegistrationDeadline().compareTo(timestamp) >= 0) {
                            if (foundConflict[0] && courseConflict[0]) {
                                Toast.makeText(dialog.getContext(), "Course is previously taken & time conflict!", Toast.LENGTH_SHORT).show();
                            } else if (courseConflict[0]) {
                                Toast.makeText(dialog.getContext(), "Course is previously taken!", Toast.LENGTH_SHORT).show();
                            } else if (foundConflict[0]) {
                                Toast.makeText(dialog.getContext(), "Time conflict!", Toast.LENGTH_SHORT).show();
                            } else {
                                String key = String.valueOf(System.currentTimeMillis());
                                Map<String, Object> reg = new HashMap<>();
                                reg.put("traineeID", email);
                                reg.put("offeringID", courseOffering.getOfferingID());
                                reg.put("status", "Pending");
                                reg.put("registrationID", key);
                                db.collection("Registration").document(key).set(reg);
                                Toast.makeText(dialog.getContext(), "Enrolled successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(dialog.getContext(), "Registration is closed!", Toast.LENGTH_SHORT).show();
                        }
//                        if (i[0] == size[0] && j[0] == noOfConflicts[0]) {
//
//                        } else {
//                            Toast.makeText(dialog.getContext(), "Couldn't fetch prerequisites!", Toast.LENGTH_SHORT).show();
//                        }
                        dismiss();
                    }
                }, 1500); // Delay time in milliseconds (e.g., 3000ms = 3 second)
            }
        });

        CardView startDate = createGeneralInformationCardView("Start date", dateFormat.format(courseOffering.getRegistrationDeadline().toDate()));
        CardView schedule = createGeneralInformationCardView("Schedule", courseOffering.getSchedule());
        CardView instructorCV = createGeneralInformationCardView("Instructor", instructor);
        CardView venue = createGeneralInformationCardView("Venue", courseOffering.getVenue());

        generalInformation.addView(instructorCV);
        generalInformation.addView(startDate);
        generalInformation.addView(schedule);
        generalInformation.addView(venue);


        return dialog;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }

    private CardView createEmptyCardView(String text) {
        CardView cardView = new CardView(dialog.getContext());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);

        LinearLayout innerLinearLayout = new LinearLayout(dialog.getContext());
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);


        TextView titleTextView = createTextView(dialog.getContext(), text, 16, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);

        // Add the LinearLayout to the CardView
        cardView.addView(innerLinearLayout);
        return cardView;
    }

    private CardView createPrerequisiteCardView(String courseName, String status, int color) {
        CardView cardView = new CardView(dialog.getContext());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);

        LinearLayout innerLinearLayout = new LinearLayout(dialog.getContext());
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);


        TextView titleTextView = createTextView(dialog.getContext(), courseName, 16, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));
        //titleTextView.setPadding(0, 0, 0, 16);

        TextView statusTextView = createTextView(dialog.getContext(), status, 12, Typeface.DEFAULT);
        if (color == 0) {
            statusTextView.setTextColor(Color.parseColor("#fc7884"));
        } else if (color == 1) {
            statusTextView.setTextColor(Color.parseColor("#84fc78"));
        }
        //statusTextView.setCompoundDrawablePadding(16);
        //statusTextView.setPadding(0, 0, 0, 16);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);
        innerLinearLayout.addView(statusTextView);

        // Add the LinearLayout to the CardView
        cardView.addView(innerLinearLayout);
        return cardView;
    }

    private CardView createGeneralInformationCardView(String title, String info) {
        CardView cardView = new CardView(dialog.getContext());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);

        LinearLayout innerLinearLayout = new LinearLayout(dialog.getContext());
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);


        TextView titleTextView = createTextView(dialog.getContext(), title, 16, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));
        //titleTextView.setPadding(0, 0, 0, 16);

        TextView statusTextView = createTextView(dialog.getContext(), info, 12, Typeface.DEFAULT);
        //statusTextView.setCompoundDrawablePadding(16);
        //statusTextView.setPadding(0, 0, 0, 16);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);
        innerLinearLayout.addView(statusTextView);

        // Add the LinearLayout to the CardView
        cardView.addView(innerLinearLayout);
        return cardView;
    }

    private CardView createTopicCardView(String topic) {
        CardView cardView = new CardView(dialog.getContext());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);

        LinearLayout innerLinearLayout = new LinearLayout(dialog.getContext());
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);


        TextView titleTextView = createTextView(dialog.getContext(), topic, 16, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));
        //titleTextView.setPadding(0, 0, 0, 16);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);

        // Add the LinearLayout to the CardView
        cardView.addView(innerLinearLayout);
        return cardView;
    }

    private TextView createTextView(Context context, String text, int textSize, Typeface typeface) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        //textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(textSize);
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.calibri));
        textView.setTypeface(typeface);
        //textView.setFontFamily(getResources().getFont(R.font.calibri));
        return textView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        try {
//            listener = (CustomDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement ExampleDialogListener");
//        }
    }

    public interface CustomDialogListener {
        void enroll(String username, String password);
    }
}
