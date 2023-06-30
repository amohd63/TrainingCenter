package com.example.trainingcenter.View.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.trainingcenter.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View_profiles_from_admin extends AppCompatActivity {
    private FirebaseFirestore db;
    LinearLayout secondLinearLayout;
    LinearLayout firstLinearLayout;
    Spinner type;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profiles_from_admin);
        firstLinearLayout = new LinearLayout(this);
        secondLinearLayout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);

        type = new Spinner(this);
        String[] options = {"Trainee", "Instructor"};
        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(spinnerAdapter);

        firstLinearLayout.setOrientation(LinearLayout.VERTICAL);
        secondLinearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(secondLinearLayout);
        firstLinearLayout.addView(type);
        firstLinearLayout.addView(scrollView);
        setContentView(firstLinearLayout);
    }
    @Override
    protected void onResume() {
        super.onResume();
        firestore = FirebaseFirestore.getInstance();
        // Get a reference to the collection
        CollectionReference collectionRef = firestore.collection("User");
        // Retrieve the documents in the collection
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secondLinearLayout.removeAllViews();
                String role = type.getSelectedItem().toString();
                if (role.equals("Trainee")) {
                    collectionRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    String email = document.getId();
                                    String firstName = document.getString("firstName");
                                    String lastName = document.getString("lastName");
                                    DocumentReference docRef = collectionRef.document(email);
                                    CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Trainee");
                                    collectionRef2.get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot querySnapshot2 = task1.getResult();
                                            if (querySnapshot2 != null) {
                                                for (QueryDocumentSnapshot document2 : querySnapshot2) {
                                                    CardView c = createCourseCardView("Show the profile of trainee " + firstName + " " + lastName + " with email " + email);
                                                    secondLinearLayout.addView(c);
                                                    c.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Bundle args = new Bundle();
                                                            args.putString("pointer1", docRef.getPath());
                                                            args.putString("pointer", role);
                                                            show_profile_from_admin_all exampleDialog = new show_profile_from_admin_all();
                                                            exampleDialog.setArguments(args);
                                                            exampleDialog.show(getSupportFragmentManager(), "example dialog");
                                                        }
                                                    });

                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else if (role.equals("Instructor")) {
                    collectionRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    String email = document.getId();
                                    String firstName = document.getString("firstName");
                                    String lastName = document.getString("lastName");
                                    DocumentReference docRef = collectionRef.document(email);
                                    CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Instructor");
                                    collectionRef2.get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot querySnapshot2 = task1.getResult();
                                            if (querySnapshot2 != null) {
                                                for (QueryDocumentSnapshot document2 : querySnapshot2) {
                                                    CardView c = createCourseCardView("Show the profile of instructor " + firstName + " " + lastName + " with email " + email);
                                                    secondLinearLayout.addView(c);
                                                    c.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Bundle args = new Bundle();
                                                            args.putString("pointer1", docRef.getPath());
                                                            args.putString("pointer", role);
                                                            show_profile_from_admin_all exampleDialog = new show_profile_from_admin_all();
                                                            exampleDialog.setArguments(args);
                                                            exampleDialog.show(getSupportFragmentManager(), "example dialog");
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private CardView createCourseCardView(String F) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardViewParams.setMargins(4, 10, 0, 10);
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

        TextView titleTextView = createTextView(this, F, 18, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));
        titleTextView.setPadding(0, 0, 0, 16);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);
        // Add the LinearLayout to the CardView
        cardView.addView(innerLinearLayout);
        return cardView;
    }
    private TextView createTextView(Context context, String text, int textSize, Typeface typeface) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextSize(textSize);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        textView.setTypeface(typeface);
        return textView;
    }
}