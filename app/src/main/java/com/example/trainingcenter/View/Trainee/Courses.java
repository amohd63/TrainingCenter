package com.example.trainingcenter.View.Trainee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.Model.CourseOffering;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Courses extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout coursesMainView;
    private LinearLayout row;
    private Course courses;
    private CourseOffering courseOfferings;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        setContentView(R.layout.activity_courses_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        coursesMainView = (LinearLayout) findViewById(R.id.courses_main_view);

        // Create the courses_list LinearLayout
        final LinearLayout[] coursesListLayout = {new LinearLayout(this)};
        final LinearLayout.LayoutParams[] coursesListLayoutParams = {new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)};
        coursesListLayout[0].setOrientation(LinearLayout.HORIZONTAL);
        coursesListLayout[0].setWeightSum(100);
        final int[] marginInPixels = {(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics())};
        coursesListLayoutParams[0].setMargins(coursesListLayoutParams[0].leftMargin, coursesListLayoutParams[0].topMargin, coursesListLayoutParams[0].rightMargin, marginInPixels[0]);
        coursesListLayout[0].setLayoutParams(coursesListLayoutParams[0]);
        coursesListLayout[0].setId(View.generateViewId());





//        TextView tv = new TextView(this);
//        CardView cardView = createCourseCardView("test", "08:00 - 10:00", "12 Jul 2023", "Dr. test");
//        coursesListLayout[0].addView(cardView);
//        coursesMainView.addView(coursesListLayout[0]);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDialog();
//            }
//        });
        Context context = this;
        db.collection("Course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AtomicInteger i = new AtomicInteger();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (Boolean.TRUE.equals(document.getBoolean("isAvailableForRegistration"))) {
                                    Timestamp timestamp = Timestamp.now();

                                    db.collection("CourseOffering")
                                            .whereEqualTo("courseID", document.getString("courseID"))
                                            .get()
                                            .addOnCompleteListener(courseOfferingTask -> {
                                                if (courseOfferingTask.isSuccessful()) {
                                                    for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                        if (courseOfferingDoc.getTimestamp("registrationDeadline").compareTo(timestamp) >= 0) {
                                                            courses = new Course(
                                                                    document.getString("courseID"),
                                                                    document.getString("courseTitle"),
                                                                    (List<String>) document.get("mainTopics"),
                                                                    document.getString("photo"),
                                                                    Boolean.TRUE.equals(document.getBoolean("isAvailableForRegistration"))
                                                            );
                                                            courseOfferings = new CourseOffering(
                                                                    courseOfferingDoc.getId(),
                                                                    courseOfferingDoc.getString("courseID"),
                                                                    courseOfferingDoc.getString("instructorID"),
                                                                    courseOfferingDoc.getTimestamp("registrationDeadline"),
                                                                    courseOfferingDoc.getTimestamp("startDate"),
                                                                    courseOfferingDoc.getString("schedule"),
                                                                    courseOfferingDoc.getString("venue")
                                                            );

                                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                                                            CardView cardView = createCourseCardView(courses.getCourseTitle(), courseOfferings.getSchedule(), dateFormat.format(courseOfferings.getRegistrationDeadline().toDate()), "Dr. test");
                                                            cardView.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    openDialog();
                                                                }
                                                            });
                                                            coursesListLayout[0].addView(cardView);
                                                            if ((i.get() + 1) % 2 == 0) {
                                                                coursesMainView.addView(coursesListLayout[0]);
                                                                coursesListLayout[0] = new LinearLayout(context);
                                                                coursesListLayoutParams[0] = new LinearLayout.LayoutParams(
                                                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                coursesListLayout[0].setOrientation(LinearLayout.HORIZONTAL);
                                                                coursesListLayout[0].setWeightSum(100);
                                                                marginInPixels[0] = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
                                                                coursesListLayoutParams[0].setMargins(coursesListLayoutParams[0].leftMargin, coursesListLayoutParams[0].topMargin, coursesListLayoutParams[0].rightMargin, marginInPixels[0]);
                                                                coursesListLayout[0].setLayoutParams(coursesListLayoutParams[0]);
                                                                coursesListLayout[0].setId(View.generateViewId());
                                                            }
                                                            i.getAndIncrement();
                                                        }
                                                    }

                                                } else {
                                                    // Handle order query error
                                                }
                                            });
                                }
                            }
                            //Log.d("expectedTasks2", String.valueOf(expectedTasks));
                        } else {
                            // Handle error
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        //works good ------------
//        for (int i = 0; i < 9; i++){
//            CardView cardView = createCourseCardView("Test", "08:00 - 10:00", "12 Jul 2023", "Dr. test");
//            coursesListLayout.addView(cardView);
//            if ((i + 1) % 2 == 0){
//                coursesMainView.addView(coursesListLayout);
//                coursesListLayout = new LinearLayout(this);
//                coursesListLayoutParams = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                coursesListLayout.setOrientation(LinearLayout.HORIZONTAL);
//                coursesListLayout.setWeightSum(100);
//                marginInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
//                coursesListLayoutParams.setMargins(coursesListLayoutParams.leftMargin, coursesListLayoutParams.topMargin, coursesListLayoutParams.rightMargin, marginInPixels);
//                coursesListLayout.setLayoutParams(coursesListLayoutParams);
//                coursesListLayout.setId(View.generateViewId());
//            }else if (i == 8){
//                coursesMainView.addView(coursesListLayout);
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private CardView createCourseCardView(String courseName, String time, String date, String instructor) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardViewParams.setMargins(4, 0, 0, 0);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(16);
        cardView.setUseCompatPadding(true);
        //cardView.setContentPadding(8, 8, 8, 8);
        cardView.setContentPadding(16, 32, 16, 32);

        // Create the LinearLayout inside the CardView
        LinearLayout innerLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);

        // Create the TextViews inside the LinearLayout
        TextView titleTextView = createTextView(this, courseName, 18, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));
        titleTextView.setPadding(0, 0, 0, 16);

        TextView timeTextView = createTextView(this, time, 16, Typeface.DEFAULT);
        timeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_red_24dp, 0, 0, 0);
        timeTextView.setCompoundDrawablePadding(16);
        timeTextView.setPadding(0, 0, 0, 16);

        TextView dateTextView = createTextView(this, date, 16, Typeface.DEFAULT);
        dateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_available_red_24dp, 0, 0, 0);
        dateTextView.setCompoundDrawablePadding(16);
        dateTextView.setPadding(0, 0, 0, 16);

        TextView instructorTextView = createTextView(this, instructor, 16, Typeface.DEFAULT);
        instructorTextView.setGravity(Gravity.END);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);
        innerLinearLayout.addView(timeTextView);
        innerLinearLayout.addView(dateTextView);
        innerLinearLayout.addView(instructorTextView);

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
        typeface = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        textView.setTypeface(typeface);
        //textView.setFontFamily(getResources().getFont(R.font.calibri));
        return textView;
    }

    public void openDialog() {
        CustomDialog exampleDialog = new CustomDialog();
        exampleDialog.setCourse(courses);
        exampleDialog.setCourseOffering(courseOfferings);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
