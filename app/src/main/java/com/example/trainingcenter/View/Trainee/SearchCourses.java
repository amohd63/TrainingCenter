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
import androidx.core.content.res.ResourcesCompat;

import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.Model.CourseOffering;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchCourses extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout coursesMainView;
    private LinearLayout row;
    private String email;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        setContentView(R.layout.activity_courses_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        coursesMainView = (LinearLayout) findViewById(R.id.courses_main_view);
        coursesMainView.setPadding(16, 16, 16, 16);
        context = this;

        AtomicInteger i = new AtomicInteger();
        Timestamp timestamp = Timestamp.now();
        search();
    }

    private void search() {
        Timestamp timestamp = Timestamp.now();
        db.collection("CourseOffering")
                .whereGreaterThanOrEqualTo("registrationDeadline", timestamp)
                .get()
                .addOnCompleteListener(courseOfferingTask -> {
                    if (courseOfferingTask.isSuccessful()) {
                        AtomicInteger size = new AtomicInteger(courseOfferingTask.getResult().size());
                        for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                            if (courseOfferingDoc.getString("status").equals("Ended")) {
                                continue;
                            }
                            db.collection("Course")
                                    .whereEqualTo("courseID", courseOfferingDoc.getString("courseID"))
                                    .whereEqualTo("isAvailableForRegistration", true)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    db.collection("User")
                                                            .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                            .get()
                                                            .addOnCompleteListener(instructorTask -> {
                                                                if (instructorTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot instructorDoc : instructorTask.getResult()) {
                                                                        Course courses = new Course(
                                                                                document.getString("courseID"),
                                                                                document.getString("courseTitle"),
                                                                                (List<String>) document.get("mainTopics"),
                                                                                document.getString("photo"),
                                                                                Boolean.TRUE.equals(document.getBoolean("isAvailableForRegistration"))
                                                                        );
                                                                        CourseOffering courseOfferings = new CourseOffering(
                                                                                courseOfferingDoc.getId(),
                                                                                courseOfferingDoc.getString("courseID"),
                                                                                courseOfferingDoc.getString("instructorID"),
                                                                                courseOfferingDoc.getTimestamp("registrationDeadline"),
                                                                                courseOfferingDoc.getTimestamp("startDate"),
                                                                                courseOfferingDoc.getString("schedule"),
                                                                                courseOfferingDoc.getString("venue")
                                                                        );
                                                                        String courseStatus = courseOfferingDoc.getString("status");
                                                                        String instructor = instructorDoc.getString("firstName") + " " + instructorDoc.getString("lastName");
                                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                                                                        CardView cardView = createCourseCardView(courses.getCourseTitle(), courseOfferings.getSchedule(), dateFormat.format(courseOfferings.getRegistrationDeadline().toDate()), courseOfferings.getVenue(), instructor, courses.getCourseID(), courseStatus, courses.isAvailableForRegistration());
                                                                        cardView.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                openDialog(courses, courseOfferings, instructor);
                                                                            }
                                                                        });
                                                                        coursesMainView.addView(cardView);
                                                                    }
                                                                } else {
                                                                    // Handle order query error
                                                                }
                                                            });
                                                }
                                                //Log.d("expectedTasks2", String.valueOf(expectedTasks));
                                            } else {
                                                // Handle error
                                                //Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    } else {
                        // Handle order query error
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private CardView createCourseCardView(String courseName, String time, String date, String venue, String instructor, String courseID, String status, boolean available) {
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
        titleTextView.setPadding(0, 0, 0, 4);

        // Create an instance of TextView
        TextView testV = new TextView(this);
// Set layout parameters
        testV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        testV.setText(courseID);
        testV.setTextSize(11);

        LinearLayout view = new LinearLayout(this);
        float scale = this.getResources().getDisplayMetrics().density;
// Set layout_width and layout_height to match_parent
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (1 * scale + 0.5f)
        );
        int marginTop = (int) (8 * scale + 0.5f);
        int marginBottom = (int) (8 * scale + 0.5f);
        layoutParams.setMargins(0, marginTop, 0, marginBottom);
        view.setLayoutParams(layoutParams);

// Set background color
        view.setBackgroundColor(Color.parseColor("#80D1D1D1"));

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
        //innerLinearLayout1.setPadding(0, 32, 0, 32);
//        innerLinearLayout1.setWeightSum(50);

        LinearLayout innerLinearLayout2 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams2 = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        innerLinearLayout2.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout2.setLayoutParams(innerLinearLayoutParams2);
        //innerLinearLayout2.setPadding(0, 32, 0, 32);
//        innerLinearLayout2.setWeightSum(50);

//        TextView instructorTextView = createTextView(this, instructor, 16, Typeface.DEFAULT, false);
//        instructorTextView.setGravity(Gravity.END);

        // Add the TextViews to the LinearLayout
        mainLinearLayout.addView(titleTextView);
        mainLinearLayout.addView(testV);
        mainLinearLayout.addView(view);

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

        view = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (1 * scale + 0.5f)
        );
        layoutParams.setMargins(0, marginTop, 0, marginBottom);
        view.setLayoutParams(layoutParams);

// Set background color
        view.setBackgroundColor(Color.parseColor("#80D1D1D1"));
        mainLinearLayout.addView(view);

        // Create an instance of TextView
        TextView statusTV = new TextView(this);
// Set layout parameters
        statusTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        statusTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        String statusOut;
        if (status.equals("Pending")) {
            statusOut = "Course has not started yet!";
        } else if (status.equals("Ongoing")) {
            statusOut = "Course already started!";
        } else {
            statusOut = "Course ended!";
        }
        statusTV.setText(statusOut);
        statusTV.setTextSize(14);

        // Create an instance of TextView
        TextView availableTV = new TextView(this);
// Set layout parameters
        availableTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        availableTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        availableTV.setText(available ? "Course is available for registration" : "Course is not available for registration");
        availableTV.setTextSize(14);


        view = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (1 * scale + 0.5f)
        );
        layoutParams.setMargins(0, marginTop, 0, marginBottom);
        view.setLayoutParams(layoutParams);

// Set background color
        view.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        mainLinearLayout.addView(availableTV);
        mainLinearLayout.addView(view);
        mainLinearLayout.addView(statusTV);


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

    public void openDialog(Course course, CourseOffering courseOffering, String instructor) {
        CourseDetails exampleDialog = new CourseDetails();
        exampleDialog.setCourse(course);
        exampleDialog.setCourseOffering(courseOffering);
        exampleDialog.setInstructor(instructor);
        exampleDialog.setEmail(email);
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
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coursesMainView.removeAllViews();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Pattern pattern = Pattern.compile(".*" + newText + ".*", Pattern.DOTALL);
                coursesMainView.removeAllViews();
                final LinearLayout[] coursesListLayout = {new LinearLayout(context)};
                final LinearLayout.LayoutParams[] coursesListLayoutParams = {new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)};
                coursesListLayout[0].setOrientation(LinearLayout.HORIZONTAL);
                coursesListLayout[0].setWeightSum(100);
                final int[] marginInPixels = {(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics())};
                coursesListLayoutParams[0].setMargins(coursesListLayoutParams[0].leftMargin, coursesListLayoutParams[0].topMargin, coursesListLayoutParams[0].rightMargin, marginInPixels[0]);
                coursesListLayout[0].setLayoutParams(coursesListLayoutParams[0]);
                coursesListLayout[0].setId(View.generateViewId());

                Timestamp timestamp = Timestamp.now();
                db.collection("CourseOffering")
                        .whereGreaterThanOrEqualTo("registrationDeadline", timestamp)
                        .get()
                        .addOnCompleteListener(courseOfferingTask -> {
                            if (courseOfferingTask.isSuccessful()) {
                                AtomicInteger size = new AtomicInteger(courseOfferingTask.getResult().size());
                                for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                    if (courseOfferingDoc.getString("status").equals("Ended")) {
                                        continue;
                                    }
                                    db.collection("Course")
                                            .whereEqualTo("courseID", courseOfferingDoc.getString("courseID"))
                                            .whereEqualTo("isAvailableForRegistration", true)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Matcher matcher = pattern.matcher(document.getString("courseTitle"));
                                                            if (matcher.matches()) {
                                                                db.collection("User")
                                                                        .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                                        .get()
                                                                        .addOnCompleteListener(instructorTask -> {
                                                                            if (instructorTask.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot instructorDoc : instructorTask.getResult()) {
                                                                                    Course courses = new Course(
                                                                                            document.getString("courseID"),
                                                                                            document.getString("courseTitle"),
                                                                                            (List<String>) document.get("mainTopics"),
                                                                                            document.getString("photo"),
                                                                                            Boolean.TRUE.equals(document.getBoolean("isAvailableForRegistration"))
                                                                                    );
                                                                                    CourseOffering courseOfferings = new CourseOffering(
                                                                                            courseOfferingDoc.getId(),
                                                                                            courseOfferingDoc.getString("courseID"),
                                                                                            courseOfferingDoc.getString("instructorID"),
                                                                                            courseOfferingDoc.getTimestamp("registrationDeadline"),
                                                                                            courseOfferingDoc.getTimestamp("startDate"),
                                                                                            courseOfferingDoc.getString("schedule"),
                                                                                            courseOfferingDoc.getString("venue")
                                                                                    );
                                                                                    String courseStatus = courseOfferingDoc.getString("status");
                                                                                    String instructor = instructorDoc.getString("firstName") + " " + instructorDoc.getString("lastName");
                                                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                                                                                    CardView cardView = createCourseCardView(courses.getCourseTitle() /*String.valueOf(size)*/, courseOfferings.getSchedule(), dateFormat.format(courseOfferings.getRegistrationDeadline().toDate()), courseOfferings.getVenue(), instructor, courses.getCourseID(), courseStatus, courses.isAvailableForRegistration());
                                                                                    cardView.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View view) {
                                                                                            openDialog(courses, courseOfferings, instructor);
                                                                                        }
                                                                                    });
                                                                                    coursesMainView.addView(cardView);
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
                                }
                            } else {
                                // Handle order query error
                            }
                        });
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
