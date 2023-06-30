package com.example.trainingcenter.View.Trainee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingcenter.View.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.example.trainingcenter.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LinearLayout profile;
    private String email;
    private FirebaseFirestore db;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fsignup_default.jpg?alt=media&token=83206b02-8fdc-40a1-8259-e39ad0d78d24";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        setContentView(R.layout.activity_home);
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
        String[] documentData = new String[2];
        DocumentReference docRef = db.collection("User").document(email);
        profileEmail.setText(email);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        LinearLayout dailySchedule = findViewById(R.id.daily_schedule);
        LinearLayout rejectedCourses = findViewById(R.id.rejected_courses);
        LinearLayout pendingCourses = findViewById(R.id.pending_courses);

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

        final int[] flag = {0, 0, 0};
        Timestamp timestamp = Timestamp.now();
        db.collection("Registration")
                .whereEqualTo("traineeID", email)
                .whereEqualTo("status", "Accepted")
                .whereLessThanOrEqualTo("startDate", timestamp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                        if (regTask.isSuccessful()) {
                            for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("offeringID", regDoc.getString("offeringID"))
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
                                                                        String schedule = courseDoc.getString("schedule");
                                                                        Calendar calendar = Calendar.getInstance();
                                                                        calendar.setTime(regDoc.getTimestamp("startDate").toDate());
                                                                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                                        String dayName = getDayName(dayOfWeek);
                                                                        if (schedule.contains(dayName)) {
                                                                            db.collection("User")
                                                                                    .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                                                    .get()
                                                                                    .addOnCompleteListener(userTask -> {
                                                                                        if (userTask.isSuccessful()) {
                                                                                            for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                                                CardView test = createCourseCardView2(
                                                                                                        courseDoc.getString("courseTitle"),
                                                                                                        courseOfferingDoc.getString("schedule"),
                                                                                                        dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                                        courseOfferingDoc.getString("venue"),
                                                                                                        userDoc.getString("firstName") + userDoc.getString("lastName")
                                                                                                );
                                                                                                if (flag[2] == 0) {
                                                                                                    dailySchedule.removeAllViews();
                                                                                                    flag[2]++;
                                                                                                }
                                                                                                dailySchedule.addView(test);
                                                                                            }

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


        /*
        rejected + pending courses
         */
        Context context = this;
        db.collection("Registration")
                .whereEqualTo("traineeID", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                        if (regTask.isSuccessful()) {
                            for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("offeringID", regDoc.getString("offeringID"))
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
                                                                                            CardView test = createCourseCardView2(
                                                                                                    courseDoc.getString("courseTitle"),
                                                                                                    courseOfferingDoc.getString("schedule"),
                                                                                                    dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                                    courseOfferingDoc.getString("venue"),
                                                                                                    userDoc.getString("firstName") + userDoc.getString("lastName")
                                                                                            );
                                                                                            if (regDoc.getString("status").equals("Pending")) {
                                                                                                if (flag[0] == 0) {
                                                                                                    pendingCourses.removeAllViews();
                                                                                                    flag[0]++;
                                                                                                }
                                                                                                pendingCourses.addView(test);
                                                                                            } else if (regDoc.getString("status").equals("Rejected")) {
                                                                                                if (flag[1] == 0) {
                                                                                                    rejectedCourses.removeAllViews();
                                                                                                    flag[1]++;
                                                                                                }
                                                                                                rejectedCourses.addView(test);
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
                                        });
                            }
                        } else {
                        }
                    }
                });


        CollectionReference usersRef = db.collection("Notification");
        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle any errors
                    return;
                }
                // Process the query snapshot and handle the updates
                for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
                    String documentId = documentSnapshot.getId();
                    String userID = documentSnapshot.getString("userID");
                    String title = documentSnapshot.getString("title");
                    String body = documentSnapshot.getString("body");
                    Toast.makeText(Home.this, documentId, Toast.LENGTH_SHORT).show();
                    createNotification(documentId, title, body);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            db.collection("Notification").document(documentId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String, Object> notificationBackup = new HashMap<>();
                                            notificationBackup.put("userID", userID);
                                            notificationBackup.put("title", title);
                                            notificationBackup.put("body", body);
                                            db.collection("User").document().set(notificationBackup);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@android.support.annotation.NonNull Exception e) {
                                        }
                                    });
                        }
                    }, 500); // Delay time in milliseconds (e.g., 1000ms = 1 second)
                }
            }
        });

    }

    //private int NOTIFICATION_ID = 123;
    private static final String NOTIFICATION_TITLE = "Notification Title";
    private static final String NOTIFICATION_BODY = "This is the body of my notification";

    public void createNotification(String NOTIFICATION_ID, String title, String body) {
        Intent intent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                MY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID.hashCode(), builder.build());
    }

    private static final String MY_CHANNEL_ID = "my_chanel_1";
    private static final String MY_CHANNEL_NAME = "My channel";

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(MY_CHANNEL_ID,
                MY_CHANNEL_NAME, importance);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "U";
            case Calendar.MONDAY:
                return "M";
            case Calendar.TUESDAY:
                return "T";
            case Calendar.WEDNESDAY:
                return "W";
            case Calendar.THURSDAY:
                return "R";
            case Calendar.FRIDAY:
                return "F";
            case Calendar.SATURDAY:
                return "S";
            default:
                return "Unknown";
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            moveTaskToBack(true);

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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_courses) {
            Intent intent = new Intent(getApplicationContext(), SearchCourses.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(getApplicationContext(), CoursesHistory.class);
            startActivity(intent);

        } else if (id == R.id.nav_lectures) {
            Intent intent = new Intent(getApplicationContext(), MyCourses.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if (id == R.id.nav_announcements) {
            Intent intent = new Intent(getApplicationContext(), Withdraw.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            intent.putExtra("email", email);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
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

    private CardView createCourseCardView2(String courseName, String time, String date, String venue, String instructor) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                1030, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        innerLayout.setOrientation(LinearLayout.VERTICAL);


// Create an instance of TextView
        TextView titleTV = new TextView(this);

// Set layout parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        titleTV.setLayoutParams(params);
        titleTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        titleTV.setText("Information System Evaluation");
        titleTV.setTextColor(ContextCompat.getColor(this, R.color.lavender));
        titleTV.setTextSize(16);


// Create an instance of TextView
        TextView instructorTV = new TextView(this);
// Set layout parameters
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        instructorTV.setLayoutParams(params);
        instructorTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        instructorTV.setText("Puspita Kartika Sari, M. Si");
        instructorTV.setTextColor(0xFF000000); // Equivalent to #000000 in hexadecimal


        // Create an instance of TextView
        TextView testV = new TextView(this);
// Set layout parameters
        testV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        testV.setText("0fdff326f5cf4728bd52");
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

        TextView instructorTextView = createTextView(this, instructor, 16, Typeface.DEFAULT, false);
        instructorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_today_purple_24, 0, 0, 0);
        instructorTextView.setCompoundDrawablePadding(32);
        instructorTextView.setPadding(0, 0, 0, 16);

        LinearLayout innerLinearLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        innerLinearLayout1.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout1.setLayoutParams(innerLinearLayoutParams1);
        //innerLinearLayout1.setPadding(0, 0, 0, 0);
//        innerLinearLayout1.setWeightSum(50);

        LinearLayout innerLinearLayout2 = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 50);
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


        mainLinearLayout.addView(imageView);
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
}
