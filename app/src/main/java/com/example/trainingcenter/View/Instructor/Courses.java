package com.example.trainingcenter.View.Instructor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Courses extends AppCompatActivity {
    private FirebaseFirestore db;
    LinearLayout mainView;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_instructor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        mainView = (LinearLayout) findViewById(R.id.courses_main_view);
        mainView.setPadding(16, 16, 16, 16);
        dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
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
                    for (QueryDocumentSnapshot documentIns : task.getResult()) {
                        String courseId = documentIns.getString("courseID");
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
                                        CardView cardView = createCourseCardView(courseId, insName, courseTitle, courseImg);
                                        mainView.addView(cardView);
                                        cardView.setOnClickListener(new View.OnClickListener() {
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

    private CardView createCourseCardView(String courseID, String instructor, String courseName, String courseImg) {
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
    public void openDialog() {
        CustomDialog exampleDialog = new CustomDialog();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private CardView createCardView(Context context, String title, String subTitle) {
        // Create the CardView
        CardView cardView = new CardView(context);
        cardView.setLayoutParams(new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setCardElevation(4);

        // Create the LinearLayout as the CardView's child
        LinearLayout containerLayout = new LinearLayout(context);
        containerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setPadding(16, 16, 16, 16);
        // Create the TextView for the title
        TextView titleTextView = new TextView(context);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        titleTextView.setText(title);
        titleTextView.setTextColor(Color.BLACK);
        titleTextView.setTextSize(16);

        // Create the TextView for the subtitle
        TextView subTitleTextView = new TextView(context);
        subTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        subTitleTextView.setText(subTitle);
        subTitleTextView.setTextColor(Color.GRAY);
        subTitleTextView.setTextSize(14);

        // Add the TextViews to the containerLayout
        containerLayout.addView(titleTextView);
        containerLayout.addView(subTitleTextView);

        // Add the containerLayout to the CardView
        cardView.addView(containerLayout);

        return cardView;
    }


}
