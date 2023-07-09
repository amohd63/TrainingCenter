package com.example.trainingcenter.View.Instructor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingcenter.Model.Instructor;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MyProfile extends AppCompatActivity {
    private String email;
    private FirebaseFirestore db;
    private final int GALLERY_REQ_CODE = 1000;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fadmin_default.png?alt=media&token=6db2f3a0-6bed-48a8-9aae-b9bff0deae01";
    private ImageView personalPhoto;
    private Instructor instructor;
    private String degree;
    private TextView courseTextView;
    private ArrayList<String> instructorCourses = new ArrayList<>();
    ArrayList<String> myList = new ArrayList<>();
    ArrayList<Integer> courseList = new ArrayList<>();
    ArrayList<String> coursesIDs = new ArrayList<>();
    String[] courseArray;
    boolean[] selectedCourses;
    String[] insCourses;
    private boolean radioButtonFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_instructor);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        db = FirebaseFirestore.getInstance();
        personalPhoto = findViewById(R.id.personalPhoto);

        TextView emailBox = findViewById(R.id.emailBox);
        TextView emailView = findViewById(R.id.email);
        EditText firstName = findViewById(R.id.firstname);
        EditText lastName = findViewById(R.id.lastname);
        EditText address = findViewById(R.id.address);
        EditText mobileNum = findViewById(R.id.mobile_number);
        EditText spec = findViewById(R.id.specialization);
        TextView name = findViewById(R.id.name);
        RadioGroup degreeRadioGroup = findViewById(R.id.degreeRadioGroup);
        courseTextView = findViewById(R.id.courseInsert);

        instructor = new Instructor();
        degree = "";

        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        instructor.setEmail(email);
                        instructor.setFirstName(document.getString("firstName"));
                        instructor.setLastName(document.getString("lastName"));
                        instructor.setPictureObj(document.getString("personalPhoto"));
                        Picasso.get().load(instructor.getPictureObj()).into(personalPhoto);
                        firstName.setText(instructor.getFirstName());
                        lastName.setText(instructor.getLastName());
                        emailBox.setText(email);
                        emailView.setText(email);
                        name.setText(instructor.getFullName());
                    } else {
                    }
                } else {
                }
            }
        });

        DocumentReference docRef2 = db.collection("User").document(email).collection("Instructor").document(email);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> coursesList1 = ((List<String>) document.get("courses"));
                        insCourses = coursesList1.toArray(new String[coursesList1.size()]);
                        instructor.setAddress(document.getString("address"));
                        instructor.setSpecialization(document.getString("specialization"));
                        instructor.setMobileNumber(document.getString("mobileNumber"));
                        instructor.setDegree(document.getString("degree"));
                        address.setText(instructor.getAddress());
                        spec.setText(instructor.getSpecialization());
                        mobileNum.setText(instructor.getMobileNumber());
                        RadioButton temp;
                        if(instructor.getDegree().equals("BSc")){
                            temp = findViewById(R.id.bscRadioButton);
                            temp.setChecked(true);
                        }else if(instructor.getDegree().equals("MSc")){
                            temp = findViewById(R.id.mscRadioButton);
                            temp.setChecked(true);
                        }else if(instructor.getDegree().equals("PhD")){
                            temp = findViewById(R.id.phdRadioButton);
                            temp.setChecked(true);
                        }
                        radioButtonFlag = true;
                    } else {
                    }
                } else {
                }
            }
        });

        CollectionReference collectionRef = db.collection("Course");
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String courseTitle = (String) document.get("courseTitle");
                        myList.add(courseTitle);
                        coursesIDs.add(document.getString("courseID"));
                    }
                    courseArray = myList.toArray(new String[myList.size()]);
                    selectedCourses = new boolean[courseArray.length];

                    for(int i = 0; i < insCourses.length; i++){
                        for(int j=0; j < courseArray.length; j++){
                            if(insCourses[i].equals(courseArray[j])){
                                selectedCourses[j] = true;
                                courseList.add(j);
                            }
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int j = 0; j< insCourses.length; j++) {
                        stringBuilder.append(insCourses[j]);
                        if(j != insCourses.length - 1){
                            stringBuilder.append(",");
                        }
                    }
                    courseTextView.setText(stringBuilder.toString());
                }
            }
        });

        courseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MyProfile.this
                );

                builder.setTitle("Select Courses");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(courseArray, selectedCourses, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b) {
                            courseList.add(i);
                            Collections.sort(courseList);
                        }else {
                            if(courseList.contains(i)) {
                                courseList.remove((Integer) i);
                            }
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j = 0; j < courseList.size(); j++) {
                            stringBuilder.append(courseArray[courseList.get(j)]);
                            if(j != courseList.size() - 1){
                                stringBuilder.append(",");
                            }
                        }
                        courseTextView.setText(stringBuilder.toString());
                        String[] data = stringBuilder.toString().split(",");
                        ArrayList<String> data_n= new ArrayList<String>(Arrays.asList(data));
                        db.collection("User").document(email).collection("Instructor").document(email).update("courses", data_n);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j = 0; j< courseList.size(); j++) {
                            try {
                                selectedCourses[j] = false;
                            }catch (ArrayIndexOutOfBoundsException ignored){

                            }
                            courseTextView.setText("");
                        }
                        courseList.clear();
                    }
                });
                builder.show();
            }
        });

        emailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyProfile.this, "Email uneditable", Toast.LENGTH_SHORT).show();
            }
        });

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newFirstName = firstName.getText().toString();
                    if (Instructor.isValidName(newFirstName)) {
                        db.collection("User").document(email)
                                .update(
                                        "firstName", newFirstName
                                );
                        Toast.makeText(MyProfile.this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                        instructor.setFirstName(newFirstName);
                        name.setText(instructor.getFullName());
                    }else{
                        Toast.makeText(MyProfile.this, "Enter a valid first name!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newLastName = lastName.getText().toString();
                    if (Instructor.isValidName(newLastName)) {
                        db.collection("User").document(email)
                                .update(
                                        "lastName", newLastName
                                );
                        Toast.makeText(MyProfile.this, "Last name updated successfully", Toast.LENGTH_SHORT).show();
                        instructor.setLastName(newLastName);
                        name.setText(instructor.getFullName());
                    }else{
                        Toast.makeText(MyProfile.this, "Enter a valid last name!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newAddress = address.getText().toString();
                    db.collection("User").document(email).collection("Instructor").document(email)
                            .update(
                                    "address", newAddress
                            );
                    Toast.makeText(MyProfile.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mobileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newMobile = mobileNum.getText().toString();
                    db.collection("User").document(email).collection("Instructor").document(email)
                            .update(
                                    "mobileNumber", newMobile
                            );
                    Toast.makeText(MyProfile.this, "Mobile No. updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spec.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newSpec = spec.getText().toString();
                    db.collection("User").document(email).collection("Instructor").document(email)
                            .update(
                                    "specialization", newSpec
                            );
                    Toast.makeText(MyProfile.this, "Specialization updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        degreeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButtonFlag) {
                    // Find the selected radio button
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    // Get the text of the selected radio button
                    degree = selectedRadioButton.getText().toString();
                    db.collection("User").document(email).collection("Instructor").document(email)
                            .update(
                                    "degree", degree
                            );
                    Toast.makeText(MyProfile.this, "Degree updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        personalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_REQ_CODE){
                String fileName = String.valueOf(System.currentTimeMillis());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);
                storageRef.putFile(data.getData())
                        .addOnSuccessListener(taskSnapshot -> {
                            storageRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        String imageUrl = uri.toString();
                                        Picasso.get().load(imageUrl).into(personalPhoto);
                                        imgUrl = uri.toString();
                                        db.collection("User").document(email)
                                                .update(
                                                        "personalPhoto", imgUrl
                                                );
                                        Toast.makeText(MyProfile.this, "Personal photo updated successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(exception -> {
                                    });
                        })
                        .addOnFailureListener(exception -> {
                        });
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
