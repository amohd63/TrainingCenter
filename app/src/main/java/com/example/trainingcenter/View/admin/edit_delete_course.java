package com.example.trainingcenter.View.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import com.example.trainingcenter.Model.Course;
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
import java.util.UUID;

public class edit_delete_course extends AppCompatActivity {
    private FirebaseFirestore db;
    LinearLayout secondLinearLayout;
    UUID uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout firstLinearLayout=new LinearLayout(this);
        secondLinearLayout=new LinearLayout(this);
        ScrollView scrollView=new ScrollView(this);
        firstLinearLayout.setOrientation(LinearLayout.VERTICAL);
        secondLinearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(secondLinearLayout);
        firstLinearLayout.addView(scrollView);
        setContentView(firstLinearLayout);
        uuid = UUID.randomUUID();

    }
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Get a reference to the collection
        CollectionReference collectionRef = firestore.collection("Course");
        // Retrieve the documents in the collection
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        LinearLayout thiredLinearLayout = new LinearLayout(this);
                        thiredLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        TextView textView = new TextView(edit_delete_course.this);
                        Button deleteBtn = new Button(edit_delete_course.this);
                        Button update = new Button(edit_delete_course.this);
                        deleteBtn.setText("Delete");
                        update.setText("update");
                        String Id = document.getId();
                        String course_name = (String) document.get("courseTitle");
                        TextView textView2 = createTextView(edit_delete_course.this , course_name , 18 ,Typeface.DEFAULT);
                        textView.setText("\nName= " + course_name);
                        DocumentReference docRef = collectionRef.document(Id);
                        thiredLinearLayout.addView(textView2);
                        thiredLinearLayout.addView(deleteBtn);
                        thiredLinearLayout.addView(update);
                        secondLinearLayout.addView(thiredLinearLayout);
                        deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleatAll(course_name);
                                recreate(); // Refresh the activity
                            }
                        });
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle args = new Bundle();
                                args.putString("pointer", docRef.getPath());
                                args.putString("pointer2",course_name);
                                update_dialog exampleDialog = new update_dialog();
                                exampleDialog.setArguments(args);
                                exampleDialog.show(getSupportFragmentManager(), "example dialog");
                            }
                        });

                    }
                }
            }
        });
    }
    private void deleatAll(String course_name){
        CollectionReference courseRef = db.collection("Course");
        Query courseQuery = courseRef.whereEqualTo("courseTitle", course_name);
        courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String courseId = document.getId();
                        String courseTitle = (String) document.get("courseTitle");
                        // Delete course from Course collection
                        db.collection("Course").document(courseId).delete();
                        // 2. Retrieve offeringIDs by courseID
                        CollectionReference offeringRef = db.collection("CourseOffering");
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId);
                        offeringQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String offeringId = document.getId();
                                        // Delete offering from CourseOffering collection
                                        db.collection("CourseOffering").document(offeringId).delete();
                                        CollectionReference registrationRef = db.collection("Registration");
                                        Query registrationQuery = registrationRef.whereEqualTo("offeringID", offeringId);
                                        registrationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String registrationID = (String) document.get("traineeID");
                                                        // Delete registration from Registration collection
                                                        db.collection("Registration").document(registrationID).delete();
                                                        CollectionReference userRef = db.collection("User");
                                                        Query userQuery = userRef.whereEqualTo("email", registrationID);
                                                        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        //notification
                                                                        String title = "Deleted Course";
                                                                        String body = "The course " + courseTitle + " has been deleted";
                                                                        String noteID = uuid.toString().replace("-", "").substring(0, 20);
                                                                        Map<String, Object> note = new HashMap<>();
                                                                        note.put("body", body);
                                                                        note.put("title", title);
                                                                        note.put("userID", registrationID);
                                                                        db.collection("NotificationBackup").document(noteID).set(note);
                                                                    }
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

                        CollectionReference prerequisiteRef = db.collection("Prerequisite");
                        Query prerequisiteQuery = prerequisiteRef.whereEqualTo("courseID", courseId);
                        prerequisiteQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String prerequisiteID =(String) document.getId();;
                                        // Delete offering from CourseOffering collection
                                        db.collection("Prerequisite").document(prerequisiteID).delete();
                                    }
                                }
                            }
                        });

                        CollectionReference instructorCourseRef = db.collection("InstructorCourse");
                        Query instructorCourseQuery = instructorCourseRef.whereEqualTo("courseID", courseId);
                        instructorCourseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String InstructorCourseId = document.getId();
                                        // Delete offering from CourseOffering collection
                                        db.collection("InstructorCourse").document(InstructorCourseId).delete();
                                    }
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
    private TextView createTextView(Context context, String text, int textSize, Typeface typeface) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextSize(textSize);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        textView.setTypeface(typeface);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(0, 0, 10, 16);

        return textView;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}