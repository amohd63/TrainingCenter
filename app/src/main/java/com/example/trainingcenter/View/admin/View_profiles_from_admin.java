package com.example.trainingcenter.View.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View_profiles_from_admin extends AppCompatActivity {
    private FirebaseFirestore db;
    LinearLayout secondLinearLayout;
    Spinner type;
    LinearLayout mainView;
    FirebaseFirestore firestore;
    private AutoCompleteTextView address;
    private ArrayAdapter<String> adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profiles_from_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainView = findViewById(R.id.new_lets_show_the_data);
        secondLinearLayout = new LinearLayout(this);
        secondLinearLayout.setOrientation(LinearLayout.VERTICAL);
        address = findViewById(R.id.roles);
        type = new Spinner(this);
        String[] options = {"Trainee", "Instructor"};
        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(spinnerAdapter);
        //mainView.addView(type);
        mainView.addView(secondLinearLayout);
        adapterItems = new ArrayAdapter<String>(this, R.layout.item_drop_down, options);
        address.setAdapter(adapterItems);
//        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = adapterView.getItemAtPosition(i).toString();
//                selectedCity = item;
//            }
//        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        firestore = FirebaseFirestore.getInstance();
        // Get a reference to the collection
        CollectionReference collectionRef = firestore.collection("User");
        // Retrieve the documents in the collection
        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                secondLinearLayout.removeAllViews();
                String role = adapterView.getItemAtPosition(i).toString();
                if (role.equals("Trainee")) {
                    collectionRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    String email = document.getId();
                                    String firstName = document.getString("firstName");
                                    String lastName = document.getString("lastName");
                                    String photo = document.getString("personalPhoto");
                                    DocumentReference docRef = collectionRef.document(email);
                                    CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Trainee");
                                    collectionRef2.get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot querySnapshot2 = task1.getResult();
                                            if (querySnapshot2 != null) {
                                                for (QueryDocumentSnapshot document2 : querySnapshot2) {
                                                    CardView c = createCourseCardView2(email,firstName,lastName,photo);
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
                                    String photo = document.getString("personalPhoto");
                                    DocumentReference docRef = collectionRef.document(email);
                                    CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Instructor");
                                    collectionRef2.get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot querySnapshot2 = task1.getResult();
                                            if (querySnapshot2 != null) {
                                                for (QueryDocumentSnapshot document2 : querySnapshot2) {
                                                    CardView c = createCourseCardView2(email,firstName,lastName,photo);
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
        });

//        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                secondLinearLayout.removeAllViews();
//                String role = type.getSelectedItem().toString();
//                if (role.equals("Trainee")) {
//                    collectionRef.get().addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            if (querySnapshot != null) {
//                                for (QueryDocumentSnapshot document : querySnapshot) {
//                                    String email = document.getId();
//                                    String firstName = document.getString("firstName");
//                                    String lastName = document.getString("lastName");
//                                    String photo = document.getString("personalPhoto");
//                                    DocumentReference docRef = collectionRef.document(email);
//                                    CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Trainee");
//                                    collectionRef2.get().addOnCompleteListener(task1 -> {
//                                        if (task1.isSuccessful()) {
//                                            QuerySnapshot querySnapshot2 = task1.getResult();
//                                            if (querySnapshot2 != null) {
//                                                for (QueryDocumentSnapshot document2 : querySnapshot2) {
//                                                    CardView c = createCourseCardView2(email,firstName,lastName,photo);
//                                                    secondLinearLayout.addView(c);
//                                                    c.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View view) {
//                                                            Bundle args = new Bundle();
//                                                            args.putString("pointer1", docRef.getPath());
//                                                            args.putString("pointer", role);
//                                                            show_profile_from_admin_all exampleDialog = new show_profile_from_admin_all();
//                                                            exampleDialog.setArguments(args);
//                                                            exampleDialog.show(getSupportFragmentManager(), "example dialog");
//                                                        }
//                                                    });
//
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//                } else if (role.equals("Instructor")) {
//                    collectionRef.get().addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            if (querySnapshot != null) {
//                                for (QueryDocumentSnapshot document : querySnapshot) {
//                                    String email = document.getId();
//                                    String firstName = document.getString("firstName");
//                                    String lastName = document.getString("lastName");
//                                    String photo = document.getString("personalPhoto");
//                                    DocumentReference docRef = collectionRef.document(email);
//                                    CollectionReference collectionRef2 = firestore.collection("User").document(email).collection("Instructor");
//                                    collectionRef2.get().addOnCompleteListener(task1 -> {
//                                        if (task1.isSuccessful()) {
//                                            QuerySnapshot querySnapshot2 = task1.getResult();
//                                            if (querySnapshot2 != null) {
//                                                for (QueryDocumentSnapshot document2 : querySnapshot2) {
//                                                    CardView c = createCourseCardView2(email,firstName,lastName,photo);
//                                                    secondLinearLayout.addView(c);
//                                                    c.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View view) {
//                                                            Bundle args = new Bundle();
//                                                            args.putString("pointer1", docRef.getPath());
//                                                            args.putString("pointer", role);
//                                                            show_profile_from_admin_all exampleDialog = new show_profile_from_admin_all();
//                                                            exampleDialog.setArguments(args);
//                                                            exampleDialog.show(getSupportFragmentManager(), "example dialog");
//                                                        }
//                                                    });
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }
    private CardView createCourseCardView2(String email, String F, String L, String photo) {
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
        Picasso.get().load(photo).into(imageView);

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
        titleTV.setText(email);
        titleTV.setTextColor(ContextCompat.getColor(this, R.color.lavender));
        titleTV.setTextSize(16);

// Create an instance of TextView
        TextView instructorTV = new TextView(this);
// Set layout parameters
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        instructorTV.setLayoutParams(params);
        instructorTV.setTypeface(ResourcesCompat.getFont(this, R.font.calibri));
        instructorTV.setText(F+" "+L);
        instructorTV.setTextColor(0xFF000000); // Equivalent to #000000 in hexadecimal

        innerLayout.addView(titleTV);
        innerLayout.addView(instructorTV);

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
}