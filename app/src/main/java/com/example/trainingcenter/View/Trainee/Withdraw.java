package com.example.trainingcenter.View.Trainee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.trainingcenter.R;
import com.example.trainingcenter.View.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class Withdraw extends AppCompatActivity implements WithdrawDialog.WithdrawListener {
    LinearLayout mainView;
    private String email;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        db = FirebaseFirestore.getInstance();
        mainView = findViewById(R.id.withdraw_main_view);
        mainView.setPadding(16, 16, 16, 16);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        db.collection("Registration")
                .whereEqualTo("traineeID", email)
                .whereIn("status", Arrays.asList("Accepted", "Pending"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                        if (regTask.isSuccessful()) {
                            for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("offeringID", regDoc.getString("offeringID"))
                                        .whereIn("status", Arrays.asList("Pending", "Ongoing"))
                                        .get()
                                        .addOnCompleteListener(courseOfferingTask -> {
                                            if (courseOfferingTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                    db.collection("Course")
                                                            .whereEqualTo("courseID", courseOfferingDoc.getString("courseID"))
                                                            .get()
                                                            .addOnCompleteListener(courseTask -> {
                                                                if (courseTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                                                        db.collection("User")
                                                                                .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                                                .get()
                                                                                .addOnCompleteListener(userTask -> {
                                                                                    if (userTask.isSuccessful()) {
                                                                                        for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                                            CardView cardView = createCourseCardView2(
                                                                                                    regDoc.getString("status"),
                                                                                                    courseDoc.getString("courseID"),
                                                                                                    userDoc.getString("firstName") + " " + userDoc.getString("lastName"),
                                                                                                    courseDoc.getString("courseTitle"),
                                                                                                    courseOfferingDoc.getString("schedule").split(" ")[0],
                                                                                                    dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                                    courseOfferingDoc.getString("venue"),
                                                                                                    courseOfferingDoc.getString("schedule").split(" ")[1],
                                                                                                    courseDoc.getString("photo"),
                                                                                                    courseOfferingDoc.getString("status")
                                                                                            );
                                                                                            String regDocID = regDoc.getString("registrationID");
                                                                                            cardView.setOnClickListener(new View.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(View view) {
                                                                                                    openDialog(regDocID);
                                                                                                }
                                                                                            });
                                                                                            mainView.addView(cardView);
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
                                        });
                            }
                        } else {
                        }
                    }
                });

        Button withdrawAll = findViewById(R.id.withdraw_all);
        withdrawAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withdraw();
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private CardView createCourseCardView2(String status, String courseID, String instructor, String courseName, String days, String date, String venue, String time, String imgURL, String courseStatus) {
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
        Picasso.get().load(imgURL).into(imageView);


        LinearLayout innerLayout = new LinearLayout(this);

// Set layout parameters
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 75));
        innerLayout.setPadding(8, 0, 0, 0);
        innerLayout.setWeightSum(75);
        innerLayout.setOrientation(LinearLayout.VERTICAL);


        // Create an instance of TextView
        TextView statusTV = new TextView(this);

// Set layout parameters
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(0, 0, 0, 4);
        statusTV.setLayoutParams(params1);
        statusTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        statusTV.setText(status);
        statusTV.setTextColor(ContextCompat.getColor(this, R.color.lavender));
        statusTV.setTextSize(16);

        LinearLayout view1 = new LinearLayout(this);

// Set layout_width and layout_height to match_parent
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (1 * scale + 0.5f)
        );
        int marginTop1 = (int) (8 * scale + 0.5f);
        int marginBottom1 = (int) (8 * scale + 0.5f);
        layoutParams1.setMargins(0, marginTop1, 0, marginBottom1);
        view1.setLayoutParams(layoutParams1);

// Set background color
        view1.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        TextView titleTV = new TextView(this);
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

        innerLayout.addView(statusTV);
        innerLayout.addView(view1);
        innerLayout.addView(titleTV);
        innerLayout.addView(instructorTV);
        innerLayout.addView(testV);
        innerLayout.addView(view);
        innerLayout.addView(innerLinearLayout3);

        // Create an instance of TextView
        TextView courseStatusTV = new TextView(this);
// Set layout parameters
        courseStatusTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        courseStatusTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        String statusOut;
        if (courseStatus.equals("Pending")){
            statusOut = "Course has not started yet!";
        }else if (courseStatus.equals("Ongoing")){
            statusOut = "Course already started!";
        }else{
            statusOut = "Course ended!";
        }
        courseStatusTV.setText(statusOut);
        courseStatusTV.setTextSize(14);

        view = new LinearLayout(this);
// Set layout_width and layout_height to match_parent
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (1 * scale + 0.5f)
        );
        layoutParams.setMargins(0, marginTop, 0, marginBottom);
        view.setLayoutParams(layoutParams);

// Set background color
        view.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        innerLayout.addView(view);
        innerLayout.addView(courseStatusTV);

        LinearLayout view2 = new LinearLayout(this);

// Set layout_width and layout_height to match_parent
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                (int) (1 * scale + 0.5f),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        marginTop = (int) (8 * scale + 0.5f);
        marginBottom = (int) (8 * scale + 0.5f);
        layoutParams2.setMargins(marginTop, 0, marginBottom, 0);
        view2.setLayoutParams(layoutParams2);

// Set background color
        view2.setBackgroundColor(Color.parseColor("#80D1D1D1"));

        mainLinearLayout.addView(imageView);
        mainLinearLayout.addView(view2);
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

    public void openDialog(String regID) {
        WithdrawDialog exampleDialog = new WithdrawDialog();
        exampleDialog.setRegID(regID);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void withdraw() {
        WithdrawAllCourses exampleDialog = new WithdrawAllCourses();
        exampleDialog.setEmail(email);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    @Override
    public void refresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                mainView.removeAllViews();
                db.collection("Registration")
                        .whereEqualTo("traineeID", email)
                        .whereIn("status", Arrays.asList("Accepted", "Pending"))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                                if (regTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                        db.collection("CourseOffering")
                                                .whereEqualTo("offeringID", regDoc.getString("offeringID"))
                                                .whereIn("status", Arrays.asList("Pending", "Ongoing"))
                                                .get()
                                                .addOnCompleteListener(courseOfferingTask -> {
                                                    if (courseOfferingTask.isSuccessful()) {
                                                        for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                            db.collection("Course")
                                                                    .whereEqualTo("courseID", courseOfferingDoc.getString("courseID"))
                                                                    .get()
                                                                    .addOnCompleteListener(courseTask -> {
                                                                        if (courseTask.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                                                                db.collection("User")
                                                                                        .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                                                        .get()
                                                                                        .addOnCompleteListener(userTask -> {
                                                                                            if (userTask.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                                                    CardView cardView = createCourseCardView2(
                                                                                                            regDoc.getString("status"),
                                                                                                            courseDoc.getString("courseID"),
                                                                                                            userDoc.getString("firstName") + " " + userDoc.getString("lastName"),
                                                                                                            courseDoc.getString("courseTitle"),
                                                                                                            courseOfferingDoc.getString("schedule").split(" ")[0],
                                                                                                            dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                                            courseOfferingDoc.getString("venue"),
                                                                                                            courseOfferingDoc.getString("schedule").split(" ")[1],
                                                                                                            courseDoc.getString("photo"),
                                                                                                            courseOfferingDoc.getString("status")
                                                                                                    );
                                                                                                    String regDocID = regDoc.getString("registrationID");
                                                                                                    cardView.setOnClickListener(new View.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(View view) {
                                                                                                            openDialog(regDocID);
                                                                                                        }
                                                                                                    });
                                                                                                    mainView.addView(cardView);
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
                                                });
                                    }
                                } else {
                                }
                            }
                        });
            }
        }, 1500); // Delay time in milliseconds (e.g., 1000ms = 1 second)


    }
}
