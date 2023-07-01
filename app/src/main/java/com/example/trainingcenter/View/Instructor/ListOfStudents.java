package com.example.trainingcenter.View.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.Model.CourseOffering;
import com.example.trainingcenter.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ListOfStudents extends AppCompatActivity {
    private String email;
    private Context context;
    private LinearLayout mainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liststudent_instructor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainView = (LinearLayout) findViewById(R.id.show_the_student_for_a_course_laty);
        context = this;
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        //performQuery("java programming", email);
        performQuery2(email);
    }
    private void performQuery2(String instructorEmail){
        mainView.removeAllViews();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference courseRef = db.collection("InstructorCourse");
        Query courseQuery = courseRef.whereEqualTo("instructorID", instructorEmail);
        courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String courseId = document.getString("courseID");
                        // 2. Retrieve offeringIDs by courseID
                        CollectionReference offeringRef = db.collection("Course");
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId);
                        offeringQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String courseId = document.getId();
                                        String courseTitle = document.getString("courseTitle");
                                        String insName = document.getString("courseTitle");
                                        String courseImg = document.getString("photo");
                                        CardView c = createCourseCardView2(courseId, insName, courseTitle, courseImg);
                                        mainView.addView(c);
                                        c.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getApplicationContext(), ViewLOS.class);
                                                intent.putExtra("courseTitle", courseTitle);
                                                intent.putExtra("email", instructorEmail);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }
                                else {
                                    Log.w("Firestore", "Error getting documents.", task.getException());
                                }
                            }
                        });
                    }
                }
                else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void performQuery(String courseTitle, String instructorEmail){
        mainView.removeAllViews();
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
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId).whereEqualTo("instructorID", instructorEmail);
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
                                                                            String email = document.getString("email");
                                                                            String firstName = document.getString("firstName");
                                                                            String lastName = document.getString("lastName");
                                                                            String fullName = firstName + " " + lastName;
                                                                            CardView c = createCourseCardView(email, fullName);
                                                                            CardView c2 = createCourseCardView(email, fullName);
                                                                            mainView.addView(c);
                                                                            mainView.addView(c2);
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

    }

    private CardView createCourseCardView2(String courseID, String instructor, String courseName, String courseImg) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        Picasso.get().load(courseImg).into(imageView);


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
                (int) (1 * scale + 0.5f),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        int marginTop = (int) (8 * scale + 0.5f);
        int marginBottom = (int) (8 * scale + 0.5f);
        layoutParams.setMargins(marginTop, 0, marginBottom, 0);
        view.setLayoutParams(layoutParams);

// Set background color
        view.setBackgroundColor(Color.parseColor("#80D1D1D1"));


        innerLayout.addView(titleTV);
        innerLayout.addView(instructorTV);
        innerLayout.addView(testV);
        //innerLayout.addView(view);

        mainLinearLayout.addView(imageView);
        mainLinearLayout.addView(view);
        mainLinearLayout.addView(innerLayout);
        cardView.addView(mainLinearLayout);
        return cardView;
    }

    private CardView createCourseCardView(String id, String F) {
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

        // Create the LinearLayout inside the CardView
        LinearLayout mainLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mainLinearLayout.setLayoutParams(innerLinearLayoutParams);

        // Create the TextViews inside the LinearLayout
        TextView titleTextView = createTextView(this, id, 24, Typeface.DEFAULT, false);
        titleTextView.setTextColor(Color.parseColor("#7884FC"));
        titleTextView.setPadding(0, 0, 0, 32);

        // Create the TextViews inside the LinearLayout
        TextView titleTextView2 = createTextView(this, F, 24, Typeface.DEFAULT, false);
        titleTextView2.setTextColor(Color.parseColor("#7884FC"));
        titleTextView2.setPadding(0, 0, 0, 32);

        // Add the TextViews to the LinearLayout
        mainLinearLayout.addView(titleTextView);
        mainLinearLayout.addView(titleTextView2);

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
                mainView.removeAllViews();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mainView.removeAllViews();
                String courseTitle = newText;
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
                                                                                    String email = document.getString("email");
                                                                                    String firstName = document.getString("firstName");
                                                                                    String lastName = document.getString("lastName");
                                                                                    CardView c = createCourseCardView(email,firstName+" "+lastName);
                                                                                    mainView.addView(c);
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