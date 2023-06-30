package com.example.trainingcenter.View.Trainee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CoursesHistory extends AppCompatActivity {
    LinearLayout mainView;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        db = FirebaseFirestore.getInstance();
        mainView = findViewById(R.id.history_main_view);
        mainView.setPadding(16, 16, 16, 16);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        db.collection("Course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> courseTask) {
                        if (courseTask.isSuccessful()) {
                            for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("courseID", courseDoc.getString("courseID"))
                                        .get()
                                        .addOnCompleteListener(courseOfferingTask -> {
                                            if (courseOfferingTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                    db.collection("User")
                                                            .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                            .get()
                                                            .addOnCompleteListener(userTask -> {
                                                                if (userTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                        CardView test = createCourseCardView(
                                                                                courseDoc.getString("courseTitle"),
                                                                                courseOfferingDoc.getString("schedule"),
                                                                                dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                courseOfferingDoc.getString("venue"),
                                                                                userDoc.getString("firstName") + userDoc.getString("lastName")
                                                                        );
                                                                        mainView.addView(test);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private CardView createCourseCardView(String courseName, String time, String date, String venue, String instructor) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardViewParams.setMargins(4, 0, 0, 0);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        //cardView.setContentPadding(8, 8, 8, 8);
        cardView.setContentPadding(32, 32, 32, 32);
        cardView.setElevation(32);



        // Create the LinearLayout inside the CardView
        LinearLayout mainLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mainLinearLayout.setLayoutParams(innerLinearLayoutParams);

        // Create the TextViews inside the LinearLayout
        TextView titleTextView = createTextView(this, courseName, 16, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#7884FC"));
        titleTextView.setPadding(0, 0, 0, 32);

        LinearLayout linearLayout = new LinearLayout(this);

// Set layout_width and layout_height to match_parent
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(layoutParams);

// Set marginTop and marginBottom
        int marginTop = (int) getResources().getDimension(R.dimen.margin_top);
        int marginBottom = (int) getResources().getDimension(R.dimen.margin_bottom);
        linearLayout.setPadding(0, marginTop, 0, marginBottom);

// Set background color
        linearLayout.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        TextView timeTextView = createTextView(this, time, 16, Typeface.DEFAULT, false);
        timeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_red_24dp, 0, 0, 0);
        timeTextView.setCompoundDrawablePadding(32);
        timeTextView.setPadding(0, 0, 0, 16);

        TextView dateTextView = createTextView(this, date, 16, Typeface.DEFAULT, false);
        dateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_available_red_24dp, 0, 0, 0);
        dateTextView.setCompoundDrawablePadding(32);
        dateTextView.setPadding(0, 0, 0, 16);

        TextView venueTextView = createTextView(this, venue, 16, Typeface.DEFAULT, false);
        venueTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_on_red_24dp, 0, 0, 0);
        venueTextView.setCompoundDrawablePadding(32);
        venueTextView.setPadding(0, 0, 0, 16);

        TextView instructorTextView = createTextView(this, instructor, 16, Typeface.DEFAULT, false);
        instructorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_outline_red_24dp, 0, 0, 0);
        instructorTextView.setCompoundDrawablePadding(32);
        instructorTextView.setPadding(0, 0, 0, 16);


        LinearLayout innerLinearLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams1 = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        innerLinearLayout1.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout1.setLayoutParams(innerLinearLayoutParams1);
        innerLinearLayout1.setPadding(0, 32, 0, 32);
//        innerLinearLayout1.setWeightSum(50);

        LinearLayout innerLinearLayout2 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams2 = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        innerLinearLayout2.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout2.setLayoutParams(innerLinearLayoutParams2);
        innerLinearLayout2.setPadding(0, 32, 0, 32);
//        innerLinearLayout2.setWeightSum(50);

//        TextView instructorTextView = createTextView(this, instructor, 16, Typeface.DEFAULT);
//        instructorTextView.setGravity(Gravity.END);

        // Add the TextViews to the LinearLayout
        mainLinearLayout.addView(titleTextView);
        mainLinearLayout.addView(linearLayout);

        innerLinearLayout1.addView(timeTextView);
        innerLinearLayout1.addView(dateTextView);


        innerLinearLayout2.addView(venueTextView);
        innerLinearLayout2.addView(instructorTextView);

        LinearLayout innerLinearLayout3 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams3 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        innerLinearLayout3.setLayoutParams(innerLinearLayoutParams3);
        //innerLinearLayout3.setPadding(0, 32, 0, 32);

        innerLinearLayout3.addView(innerLinearLayout1);
        innerLinearLayout3.addView(innerLinearLayout2);

        mainLinearLayout.addView(innerLinearLayout3);

        // Add the LinearLayout to the CardView
        cardView.addView(mainLinearLayout);
        return cardView;
    }


    private TextView createTextView(Context context, String text, int textSize, Typeface typeface, boolean setText) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.calibri));
        //textView.setTextColor(Color.parseColor("#000000"));
        if (setText) {
            textView.setTextSize(textSize);
        }
        return textView;
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

}
