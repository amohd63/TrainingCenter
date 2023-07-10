package com.example.trainingcenter.View.Instructor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingcenter.View.LoginActivity;
import com.example.trainingcenter.View.admin.StatusUpdater;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.example.trainingcenter.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Timer;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LinearLayout profile;
    private String email;
    private FirebaseFirestore db;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fsignup_default.jpg?alt=media&token=83206b02-8fdc-40a1-8259-e39ad0d78d24";
    private static final long UPDATE_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours
    private Timer timer;
    LinearLayout onGoingCourses, todayCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        setContentView(R.layout.activity_home_instructor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profile = header.findViewById(R.id.profile);
        profile.setOnClickListener(this);
        TextView profileEmail = profile.findViewById(R.id.profilemail);
        ImageView profileImg = profile.findViewById(R.id.profileimage);
        TextView profileName = profile.findViewById(R.id.profilename);
        onGoingCourses = findViewById(R.id.ongoing_courses_currently);
        todayCourses = findViewById(R.id.todayClasses);
        String[] documentData = new String[2];
        DocumentReference docRef = db.collection("User").document(email);
        profileEmail.setText(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        documentData[0] = document.getString("personalPhoto");
                        documentData[1] = document.getString("firstName") + " " + document.getString("lastName");
                        profileName.setText(documentData[1]);
                        Picasso.get().load(documentData[0]).into(profileImg);
                    } else {
                        Picasso.get().load(imgUrl).into(profileImg);
                    }
                } else {
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_courses_ins) {
            Intent intent = new Intent(getApplicationContext(), Courses.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if (id == R.id.nav_schedule_ins) {
            Intent intent = new Intent(getApplicationContext(), Schedule.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if (id == R.id.nav_LOS_ins) {
            Intent intent = new Intent(getApplicationContext(), ListOfStudents.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if (id == R.id.nav_myProfile) {
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profile) {
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ImageView profileImg = profile.findViewById(R.id.profileimage);
        TextView profileName = profile.findViewById(R.id.profilename);
        String[] documentData = new String[2];
        timer = new Timer();
        timer.schedule(new StatusUpdater(), 0, UPDATE_INTERVAL);

        todayCourses.removeAllViews();
        onGoingCourses.removeAllViews();
        fillCourses();
        fillOnGoingClass();

        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        documentData[0] = document.getString("personalPhoto");
                        documentData[1] = document.getString("firstName") + " " + document.getString("lastName");
                        profileName.setText(documentData[1]);
                        Picasso.get().load(documentData[0]).into(profileImg);
                    } else {
                        Picasso.get().load(imgUrl).into(profileImg);
                    }
                } else {
                }
            }
        });
    }
    private char getCurrentDay(){
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        String dayOfWeekSTR = dayOfWeek.toString();

        switch (dayOfWeekSTR) {
            case "MONDAY":
                return 'M';
            case "TUESDAY":
                return 'T';
            case "WEDNESDAY":
                return 'W';
            case "THURSDAY":
                return 'R';
            case "FRIDAY":
                return 'F';
            case "SATURDAY":
                return 'S';
        }
        return 'E';
    }
    private void fillOnGoingClass(){
        final int[] flag = {0};
        CardView emptyOnGoing = createEmptyCardView("you don't have any courses this semester!");
        onGoingCourses.addView(emptyOnGoing);
        final SimpleDateFormat[] dateFormat = {new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)};
        db.collection("Course").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> courseTask) {
                        if (courseTask.isSuccessful()) {
                            for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("courseID", courseDoc.getString("courseID"))
                                        .whereEqualTo("instructorID", email)
                                        .get()
                                        .addOnCompleteListener(courseOfferingTask -> {
                                            if (courseOfferingTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                    String status = (String) courseOfferingDoc.get("status");
                                                    db.collection("User")
                                                            .whereEqualTo("email", email)
                                                            .get()
                                                            .addOnCompleteListener(userTask -> {
                                                                if (userTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                        if(status.equals("Ongoing")) {
                                                                            CardView test = createCourseCardView4(
                                                                                    courseDoc.getString("courseID"),
                                                                                    userDoc.getString("firstName") + " " + userDoc.getString("lastName"),
                                                                                    courseDoc.getString("courseTitle"),
                                                                                    courseOfferingDoc.getString("schedule").split(" ")[0],
                                                                                    dateFormat[0].format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                    courseOfferingDoc.getString("venue"),
                                                                                    courseOfferingDoc.getString("schedule").split(" ")[1]
                                                                            );
                                                                            if(flag[0] == 0){
                                                                                onGoingCourses.removeAllViews();
                                                                                flag[0]++;
                                                                            }
                                                                            onGoingCourses.addView(test);
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
    }
    private void fillCourses(){
        final int[] flag = {0};
        CardView emptyOnGoing = createEmptyCardView("There is no Classes Today!");
        todayCourses.addView(emptyOnGoing);
        final SimpleDateFormat[] dateFormat = {new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)};
        db.collection("Course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> courseTask) {
                        if (courseTask.isSuccessful()) {
                            for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("courseID", courseDoc.getString("courseID"))
                                        .get()
                                        .addOnCompleteListener(courseOfferingTask -> {
                                            if (courseOfferingTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                    String status = (String) courseOfferingDoc.get("status");
                                                    db.collection("User")
                                                            .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                            .get()
                                                            .addOnCompleteListener(userTask -> {
                                                                if (userTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                        if(status.equals("Ongoing") && courseOfferingDoc.getString("schedule").split(" ")[0].contains(String.valueOf(getCurrentDay()))) {
                                                                            CardView test = createCourseCardView4(
                                                                                    courseDoc.getString("courseID"),
                                                                                    userDoc.getString("firstName") + " " + userDoc.getString("lastName"),
                                                                                    courseDoc.getString("courseTitle"),
                                                                                    courseOfferingDoc.getString("schedule").split(" ")[0],
                                                                                    dateFormat[0].format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                    courseOfferingDoc.getString("venue"),
                                                                                    courseOfferingDoc.getString("schedule").split(" ")[1]
                                                                            );
                                                                            if(flag[0] == 0){
                                                                                todayCourses.removeAllViews();
                                                                                flag[0]++;
                                                                            }
                                                                            todayCourses.addView(test);
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
    }
    private CardView createCourseCardView4(String courseID, String instructor, String courseName, String days, String date, String venue, String time) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                1100, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins(4, 0, 0, 0);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(32, 32, 32, 32);
        cardView.setElevation(32);


        LinearLayout mainLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        mainLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLinearLayout.setLayoutParams(innerLinearLayoutParams);
        mainLinearLayout.setGravity(Gravity.CENTER);
        innerLinearLayoutParams.setMargins(10, 10, 10, 10);

        int heightInDp = 60;
        float scale = this.getResources().getDisplayMetrics().density;
        int heightInPixels = (int) (heightInDp * scale + 0.5f);
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(0, heightInPixels, 25);
        heightInPixels = (int) (8 * scale + 0.5f);
        imgParams.setMargins(0, 0, heightInPixels, 0);
        imageView.setLayoutParams(imgParams);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.mobile_img);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fcourse_default.png?alt=media&token=68dd1b73-90b6-4cb9-ac91-460e3dfe6768").into(imageView);


        LinearLayout innerLayout = new LinearLayout(this);

// Set layout parameters
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 75));
        innerLayout.setPadding(8, 0, 0, 0);
        innerLayout.setWeightSum(75);
        innerLayout.setOrientation(LinearLayout.VERTICAL);


// Create an instance of TextView
        TextView titleTV = new TextView(this);

// Set layout parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        titleTV.setLayoutParams(params);
        titleTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        titleTV.setText(courseName);
        titleTV.setTextColor(ContextCompat.getColor(this, R.color.lavender));
        titleTV.setTextSize(16);


// Create an instance of TextView
        TextView instructorTV = new TextView(this);
// Set layout parameters
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        instructorTV.setLayoutParams(params);
        instructorTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        instructorTV.setText(instructor);
        instructorTV.setTextColor(0xFF000000); // Equivalent to #000000 in hexadecimal


        // Create an instance of TextView
        TextView testV = new TextView(this);
// Set layout parameters
        testV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        testV.setText(courseID);
        testV.setTextSize(11);


        LinearLayout view = new LinearLayout(this);

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

        TextView instructorTextView = createTextView(this, days, 16, Typeface.DEFAULT, false);
        instructorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_today_purple_24, 0, 0, 0);
        instructorTextView.setCompoundDrawablePadding(32);
        instructorTextView.setPadding(0, 0, 0, 16);

        LinearLayout innerLinearLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams1 = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        innerLinearLayout1.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout1.setLayoutParams(innerLinearLayoutParams1);
        //innerLinearLayout1.setPadding(0, 0, 0, 0);
//        innerLinearLayout1.setWeightSum(50);

        LinearLayout innerLinearLayout2 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams2 = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        innerLinearLayout2.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout2.setLayoutParams(innerLinearLayoutParams2);
        //innerLinearLayout2.setPadding(0, 0, 0, 0);

        LinearLayout innerLinearLayout3 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams3 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        innerLinearLayout3.setLayoutParams(innerLinearLayoutParams3);
        //innerLinearLayout3.setPadding(0, 32, 0, 32);

        innerLinearLayout1.addView(timeTextView);
        innerLinearLayout1.addView(instructorTextView);


        innerLinearLayout2.addView(venueTextView);
        innerLinearLayout2.addView(dateTextView);

        innerLinearLayout3.addView(innerLinearLayout1);
        innerLinearLayout3.addView(innerLinearLayout2);


        innerLayout.addView(titleTV);
        innerLayout.addView(instructorTV);
        innerLayout.addView(testV);
        innerLayout.addView(view);
        innerLayout.addView(innerLinearLayout3);

        LinearLayout view1 = new LinearLayout(this);

// Set layout_width and layout_height to match_parent
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                (int) (1 * scale + 0.5f),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        marginTop = (int) (8 * scale + 0.5f);
        marginBottom = (int) (8 * scale + 0.5f);
        layoutParams1.setMargins(marginTop, 0, marginBottom, 0);
        view1.setLayoutParams(layoutParams1);

// Set background color
        view1.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        mainLinearLayout.addView(imageView);
        mainLinearLayout.addView(view1);
        mainLinearLayout.addView(innerLayout);
        cardView.addView(mainLinearLayout);
        return cardView;
    }
    private CardView createEmptyCardView(String text) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                1030, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins(4, 0, 0, 0);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);
        cardView.setElevation(32);

        LinearLayout mainLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        mainLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLinearLayout.setLayoutParams(innerLinearLayoutParams);
        mainLinearLayout.setGravity(Gravity.CENTER);

        TextView titleTV = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        titleTV.setLayoutParams(params);
        titleTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        titleTV.setText(text);
        titleTV.setTextColor(ContextCompat.getColor(this, R.color.lavender));
        titleTV.setTextSize(16);

        mainLinearLayout.addView(titleTV);
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

}