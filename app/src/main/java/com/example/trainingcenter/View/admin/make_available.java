package com.example.trainingcenter.View.admin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class make_available extends AppCompatActivity {
    private FirebaseFirestore db;
    LinearLayout secondLinearLayout;
    private LinearLayout coursesMainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_available);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        coursesMainView = (LinearLayout) findViewById(R.id.make_available_layout_to_add_courses);
        // Create the courses_list LinearLayout
        LinearLayout coursesListLayout = new LinearLayout(this);
        LinearLayout.LayoutParams coursesListLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        coursesListLayout.setOrientation(LinearLayout.HORIZONTAL);
        coursesListLayout.setWeightSum(100);
        int marginInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        coursesListLayoutParams.setMargins(coursesListLayoutParams.leftMargin, coursesListLayoutParams.topMargin, coursesListLayoutParams.rightMargin, marginInPixels);
        coursesListLayout.setLayoutParams(coursesListLayoutParams);
        coursesListLayout.setId(View.generateViewId());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Get a reference to the collection
        CollectionReference collectionRef = firestore.collection("Course");
        // Retrieve the documents in the collection
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        /*TextView textView = new TextView(make_available.this);
                        Button makeAvailable = new Button(make_available.this);
                        makeAvailable.setText("Make Available");*/
                        String Id = document.getId();
                        String course_name = (String) document.get("courseTitle");
                        //textView.setText("\nName= " + course_name);
                        CardView c = createCourseCardView(course_name,Id);
                        coursesListLayout.addView(c);
                        coursesMainView.addView(coursesListLayout);
                        DocumentReference docRef = collectionRef.document(Id);
                        //secondLinearLayout.addView(textView);
                        //secondLinearLayout.addView(makeAvailable);
                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle args = new Bundle();
                                args.putString("pointer", docRef.getPath());
                                available_dialog exampleDialog = new available_dialog();
                                exampleDialog.setArguments(args);
                                exampleDialog.show(getSupportFragmentManager(), "example dialog");
                            }
                        });
                    }
                }
            }
        });
        /*LinearLayout firstLinearLayout=new LinearLayout(this);
        secondLinearLayout=new LinearLayout(this);
        ScrollView scrollView=new ScrollView(this);
        firstLinearLayout.setOrientation(LinearLayout.VERTICAL);
        secondLinearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(secondLinearLayout);
        firstLinearLayout.addView(scrollView);
        setContentView(firstLinearLayout);*/

    }
    private CardView createCourseCardView(String courseName, String Id) {
        String firstFiveChars = Id.substring(0, Math.min(Id.length(), 5));
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

        TextView timeTextView = createTextView(this, firstFiveChars, 16, Typeface.DEFAULT);
        timeTextView.setCompoundDrawablePadding(16);
        timeTextView.setPadding(0, 0, 0, 16);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);
        innerLinearLayout.addView(timeTextView);

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
}