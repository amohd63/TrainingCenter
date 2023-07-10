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
import android.util.Log;
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
import com.example.trainingcenter.View.admin.StatusUpdater;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LinearLayout profile;
    private String email;
    private FirebaseFirestore db;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fsignup_default.jpg?alt=media&token=83206b02-8fdc-40a1-8259-e39ad0d78d24";
    SimpleDateFormat dateFormat;
    LinearLayout dailySchedule;
    LinearLayout rejectedCourses;
    LinearLayout pendingCourses;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start the timer to run the StatusUpdater periodically
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
        dailySchedule = findViewById(R.id.daily_schedule);
        rejectedCourses = findViewById(R.id.rejected_courses);
        pendingCourses = findViewById(R.id.pending_courses);
        mainLayout = findViewById(R.id.main_layout);
        dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
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

        CollectionReference usersRef = db.collection("Notification");
        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                    DocumentSnapshot notificationDoc = documentChange.getDocument();
                    String documentId = notificationDoc.getId();
                    String userID = notificationDoc.getString("userID");
                    String title = notificationDoc.getString("title");
                    String body = notificationDoc.getString("body");
                    Timestamp noteDate = notificationDoc.getTimestamp("noteDate");
                    Boolean fetched = notificationDoc.getBoolean("fetch");
                    if (userID == null) {
                        continue;
                    }
                    if ((userID.equals("all") && Boolean.FALSE.equals(fetched))|| (userID.equals(email) && Boolean.FALSE.equals(fetched))) {

                        createNotification(documentId, title, body);
                        displayNotifications();
                        db.collection("Notification").document(documentId)
                                .update(
                                        "fetch", true
                                );


//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                db.collection("Notification").document(documentId)
//                                        .delete()
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Map<String, Object> notificationBackup = new HashMap<>();
//                                                notificationBackup.put("userID", userID);
//                                                notificationBackup.put("title", title);
//                                                notificationBackup.put("body", body);
//                                                notificationBackup.put("noteDate", noteDate);
//                                                db.collection("NotificationBackup").document().set(notificationBackup);
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@android.support.annotation.NonNull Exception e) {
//                                            }
//                                        });
//                            }
//                        }, 500); // Delay time in milliseconds (e.g., 1000ms = 1 second)
                    }
                }
            }
        });

    }

    //private int NOTIFICATION_ID = 123;
    private static final String NOTIFICATION_TITLE = "Notification Title";
    private static final String NOTIFICATION_BODY = "This is the body of my notification";

    public void createNotification(String NOTIFICATION_ID, String title, String body) {
        Intent intent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
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
//        Intent intent = new Intent(getApplicationContext(), MyProfile.class);
//        intent.putExtra("email", email);
//        startActivity(intent);
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
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_previous_courses) {
            Intent intent = new Intent(getApplicationContext(), TraineeCourseHistory.class);
            intent.putExtra("email", email);
            startActivity(intent);
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

    private CardView createCourseCardView2(String courseID, String instructor, String courseName, String days, String date, String venue, String time, String imgURL) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                1380, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private CardView createNotificationCard(String title, String body, String time) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins(32, 0, 32, 0);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(32);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(32, 32, 32, 32);
        cardView.setElevation(32);


        LinearLayout mainLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mainLinearLayout.setLayoutParams(innerLinearLayoutParams);
        innerLinearLayoutParams.setMargins(10, 10, 10, 10);

        float scale = this.getResources().getDisplayMetrics().density;

// Create an instance of TextView
        TextView titleTV = new TextView(this);

// Set layout parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        titleTV.setLayoutParams(params);
        titleTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        titleTV.setText(title);
        titleTV.setTextColor(ContextCompat.getColor(this, R.color.white));
        titleTV.setBackground(getResources().getDrawable(R.drawable.blue_rounded_solid));
        titleTV.setTextSize(14);
        titleTV.setPadding((int) (12 * scale + 0.5f), (int) (8 * scale + 0.5f), (int) (12 * scale + 0.5f), (int) (8 * scale + 0.5f));


// Create an instance of TextView
        TextView instructorTV = new TextView(this);
// Set layout parameters
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        instructorTV.setLayoutParams(params);
        instructorTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        instructorTV.setText(body);
        instructorTV.setTextColor(0xFF000000); // Equivalent to #000000 in hexadecimal
        instructorTV.setTextSize(12);


        // Create an instance of TextView
        TextView testV = new TextView(this);
// Set layout parameters
        testV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        testV.setText(time);
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

        mainLinearLayout.addView(titleTV);
        mainLinearLayout.addView(view);
        mainLinearLayout.addView(instructorTV);
        mainLinearLayout.addView(testV);

        cardView.addView(mainLinearLayout);
        return cardView;
    }

    private void displayNotifications() {
        mainLayout.removeAllViews();
        CardView emptyNotification = createEmptyCardView("You don't have any notifications!");
        mainLayout.addView(emptyNotification);
        final boolean[] flag = {false};
        db.collection("Notification")
                .orderBy("noteDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                        if (regTask.isSuccessful()) {
                            for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                if (regDoc.getString("userID").equals(email) || regDoc.getString("userID").equals("all")) {
                                    TimeZone.setDefault(TimeZone.getTimeZone("EET"));
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault());
                                    String formattedTimestamp = sdf.format(regDoc.getTimestamp("noteDate").toDate());
                                    CardView cv = createNotificationCard(regDoc.getString("title"), regDoc.getString("body"), formattedTimestamp);
                                    if (!flag[0]) {
                                        flag[0] = true;
                                        mainLayout.removeAllViews();
                                    }
                                    mainLayout.addView(cv);
                                }
                            }
                        } else {
                        }
                    }
                });
    }

    protected void onResume() {
        super.onResume();
        ImageView profileImg = profile.findViewById(R.id.profileimage);
        TextView profileName = profile.findViewById(R.id.profilename);
        String[] documentData = new String[2];
        pendingCourses.removeAllViews();
        rejectedCourses.removeAllViews();
        dailySchedule.removeAllViews();
        CardView emptyPending = createEmptyCardView("You don't have any pending course!");
        pendingCourses.addView(emptyPending);
        CardView emptyRejected = createEmptyCardView("You don't have any rejected courses!");
        rejectedCourses.addView(emptyRejected);
        CardView emptySchedule = createEmptyCardView("You are not enrolled in any course yet!");
        dailySchedule.addView(emptySchedule);
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

        displayNotifications();

        final int[] flag = {0, 0, 0};
        Timestamp timestamp = Timestamp.now();
        db.collection("Registration")
                .whereEqualTo("traineeID", email)
                .whereEqualTo("status", "Accepted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> regTask) {
                        if (regTask.isSuccessful()) {
                            for (QueryDocumentSnapshot regDoc : regTask.getResult()) {
                                db.collection("CourseOffering")
                                        .whereEqualTo("offeringID", regDoc.getString("offeringID"))
                                        .whereIn("status", Arrays.asList("Ongoing", "Pending"))
                                        .get()
                                        .addOnCompleteListener(courseOfferingTask -> {
                                            if (courseOfferingTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot courseOfferingDoc : courseOfferingTask.getResult()) {
                                                    if (courseOfferingDoc.getTimestamp("startDate").compareTo(timestamp) <= 0) {
                                                        db.collection("Course")
                                                                .whereEqualTo("courseID", courseOfferingDoc.getString("courseID"))
                                                                .get()
                                                                .addOnCompleteListener(courseTask -> {
                                                                    if (courseTask.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                                                            String schedule = courseOfferingDoc.getString("schedule");
                                                                            TimeZone.setDefault(TimeZone.getTimeZone("EET"));
                                                                            Calendar calendar = Calendar.getInstance();
                                                                            calendar.setTime(timestamp.toDate());
                                                                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                                            String dayName = getDayName(dayOfWeek);
                                                                            Log.d("schedule ", schedule);
                                                                            Log.d("dayName ", dayName);
                                                                            if (schedule.contains(dayName)) {
                                                                                db.collection("User")
                                                                                        .whereEqualTo("email", courseOfferingDoc.getString("instructorID"))
                                                                                        .get()
                                                                                        .addOnCompleteListener(userTask -> {
                                                                                            if (userTask.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                                                                                    CardView test = createCourseCardView2(
                                                                                                            courseDoc.getString("courseID"),
                                                                                                            userDoc.getString("firstName") + " " + userDoc.getString("lastName"),
                                                                                                            courseDoc.getString("courseTitle"),
                                                                                                            courseOfferingDoc.getString("schedule").split(" ")[0],
                                                                                                            dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                                            courseOfferingDoc.getString("venue"),
                                                                                                            courseOfferingDoc.getString("schedule").split(" ")[1],
                                                                                                            courseDoc.getString("photo")
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
                                                                                                    courseDoc.getString("courseID"),
                                                                                                    userDoc.getString("firstName") + " " +  userDoc.getString("lastName"),
                                                                                                    courseDoc.getString("courseTitle"),
                                                                                                    courseOfferingDoc.getString("schedule").split(" ")[0],
                                                                                                    dateFormat.format(courseOfferingDoc.getTimestamp("startDate").toDate()),
                                                                                                    courseOfferingDoc.getString("venue"),
                                                                                                    courseOfferingDoc.getString("schedule").split(" ")[1],
                                                                                                    courseDoc.getString("photo")
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

        CollectionReference offeringRef = db.collection("CourseOffering");
        offeringRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Timestamp startDate = document.getTimestamp("startDate");
                        String status;
                        if (startDate != null) {
                            Date date1 = startDate.toDate(); // Your first date object
                            Date date2 = Timestamp.now().toDate(); // Your second date object

                            // Create calendar instances for both dates
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(date1);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(date2);

                            // Clear the time portion of both calendars
                            cal1.set(Calendar.HOUR_OF_DAY, 0);
                            cal1.set(Calendar.MINUTE, 0);
                            cal1.set(Calendar.SECOND, 0);
                            cal1.set(Calendar.MILLISECOND, 0);
                            cal2.set(Calendar.HOUR_OF_DAY, 0);
                            cal2.set(Calendar.MINUTE, 0);
                            cal2.set(Calendar.SECOND, 0);
                            cal2.set(Calendar.MILLISECOND, 0);

                            // Compare the dates without considering the time
                            int comparison = cal1.compareTo(cal2);
                            // Output the result
                            if (comparison == 0) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                                SimpleDateFormat stf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                String date = sdf.format(startDate.toDate());
                                String time = stf.format(startDate.toDate());
                                db.collection("Registration")
                                        .whereEqualTo("offeringID", document.getString("offeringID"))
                                        .whereEqualTo("status", "Accepted")
                                        .get()
                                        .addOnCompleteListener(userTask -> {
                                            if (userTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot regDoc : userTask.getResult()) {
                                                    db.collection("Course")
                                                            .whereEqualTo("courseID", document.getString("courseID"))
                                                            .get()
                                                            .addOnCompleteListener(courseTask -> {
                                                                if (courseTask.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot courseDoc : courseTask.getResult()) {
                                                                        String courseTitle = courseDoc.getString("courseTitle");
                                                                        String title = "Reminder | " + courseTitle;
                                                                        String body = "Today [" + date + "] at " + time + ", " + courseTitle + " lectures begin. Please be ready!";
                                                                        Map<String, Object> note = new HashMap<>();
                                                                        note.put("body", body);
                                                                        note.put("title", title);
                                                                        note.put("userID", regDoc.getString("traineeID"));
                                                                        TimeZone.setDefault(TimeZone.getTimeZone("EET"));
                                                                        Timestamp timestampNote = Timestamp.now();
                                                                        note.put("noteDate", timestampNote);
                                                                        note.put("fetch", false);

                                                                        db.collection("Notification")
                                                                                .whereEqualTo("body", body)
                                                                                .whereEqualTo("title", title)
                                                                                .get()
                                                                                .addOnCompleteListener(notificationTask -> {
                                                                                    if (notificationTask.isSuccessful()) {
                                                                                        if (notificationTask.getResult().isEmpty()) {
                                                                                            db.collection("Notification").document().set(note);
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


                            // Retrieve the start date value from the Timestamp
                            Date startDateTime = startDate.toDate();
                            Calendar startCalendar = Calendar.getInstance();
                            startCalendar.setTime(startDateTime);

                            // Calculate the end date by adding 4 months to the start date
                            Calendar endCalendar = Calendar.getInstance();
                            endCalendar.setTime(startDateTime);
                            endCalendar.add(Calendar.MONTH, 4);
                            Date endDateTime = endCalendar.getTime();

                            // Get the current date and time
                            Calendar currentCalendar = Calendar.getInstance();
                            Date currentDateTime = currentCalendar.getTime();

                            // Compare the current date with the start and end dates to determine the status
                            if (currentDateTime.compareTo(startDateTime) >= 0 && currentDateTime.compareTo(endDateTime) <= 0) {
                                // Current date is between the start date and end date
                                // Set status as "on going"
                                status = "Ongoing";
                            } else if (currentDateTime.compareTo(startDateTime) < 0) {
                                // Current date is before the start date
                                // Set status as "pending"
                                status = "Pending";
                            } else {
                                // Current date is after the end date
                                // Set status as "ended"
                                status = "Ended";
                            }

                            // Update the status field in Firestore
                            document.getReference().update("status", status);
                        }
                    }
                }
            } else {
                System.out.println("filed to update status");
            }
        });
    }
}
