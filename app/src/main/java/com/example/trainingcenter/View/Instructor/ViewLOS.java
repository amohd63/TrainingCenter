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


public class ViewLOS extends AppCompatActivity {
    private String email, courseTitle;
    private LinearLayout mainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liststudent_instructor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainView = findViewById(R.id.show_the_student_for_a_course_laty);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        courseTitle = intent.getStringExtra("courseTitle");
        performQuery(courseTitle, email);
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
                                                                        assert status != null;
                                                                        if(status.equals("Accepted")) {
                                                                            String email = document.getString("email");
                                                                            String firstName = document.getString("firstName");
                                                                            String lastName = document.getString("lastName");
                                                                            String fullName = firstName + " " + lastName;
                                                                            String personalPhoto = document.getString("personalPhoto");
                                                                            //CardView c = createCourseCardView(email, fullName);
                                                                            CardView c = createCourseCardView2(email, fullName, personalPhoto, "Hebron", "0597544077");
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

    }

    private CardView createCourseCardView2(String stEmail, String stName, String stImg, String address, String mobile) {
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
        Picasso.get().load(stImg).into(imageView);


        LinearLayout innerLayout = new LinearLayout(this);

// Set layout parameters
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 75));
        innerLayout.setPadding(8, 0, 0, 0);
        innerLayout.setWeightSum(75);
        innerLayout.setOrientation(LinearLayout.VERTICAL);


//// Create an instance of TextView
//        TextView titleTV = new TextView(this);
//
//// Set layout parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
//        titleTV.setLayoutParams(params);
//        titleTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
//        titleTV.setText(courseName);
//        titleTV.setTextColor(ContextCompat.getColor(this, R.color.lavender));
//        titleTV.setTextSize(16);


// Create an instance of TextView
        TextView instructorTV = new TextView(this);
// Set layout parameters
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        instructorTV.setLayoutParams(params);
        instructorTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        instructorTV.setText(stName);
        instructorTV.setTextColor(0xFF000000); // Equivalent to #000000 in hexadecimal
        instructorTV.setTextSize(14);


        // Create an instance of TextView
        TextView testV = new TextView(this);
// Set layout parameters
        testV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        testV.setText(stEmail);
        testV.setTextSize(12);


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

        TextView timeTextView = createTextView(this, address, 11, Typeface.DEFAULT, false);
        timeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_16, 0, 0, 0);
        timeTextView.setCompoundDrawablePadding(32);
        timeTextView.setPadding(0, 0, 0, 16);

        TextView dateTextView = createTextView(this, mobile, 11, Typeface.DEFAULT, false);
        dateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_16, 0, 0, 0);
        dateTextView.setCompoundDrawablePadding(32);
        dateTextView.setPadding(0, 0, 0, 16);


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

        LinearLayout view1 = new LinearLayout(this);

// Set layout_width and layout_height to match_parent
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (1 * scale + 0.5f)
        );
        marginTop = (int) (8 * scale + 0.5f);
        marginBottom = (int) (8 * scale + 0.5f);
        layoutParams1.setMargins(0, marginTop, 0, marginBottom);
        view1.setLayoutParams(layoutParams1);

// Set background color
        view1.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        innerLinearLayout1.addView(timeTextView);


        innerLinearLayout2.addView(dateTextView);

        innerLinearLayout3.addView(innerLinearLayout1);
        innerLinearLayout3.addView(innerLinearLayout2);


        innerLayout.addView(instructorTV);
        innerLayout.addView(testV);
        innerLayout.addView(view1);
        innerLayout.addView(innerLinearLayout3);

        mainLinearLayout.addView(imageView);
        mainLinearLayout.addView(view);
        mainLinearLayout.addView(innerLayout);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


}
