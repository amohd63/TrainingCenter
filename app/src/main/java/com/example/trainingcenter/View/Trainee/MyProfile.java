package com.example.trainingcenter.View.Trainee;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingcenter.Model.Trainee;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    private String email;
    private FirebaseFirestore db;
    private final int GALLERY_REQ_CODE = 1000;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fadmin_default.png?alt=media&token=6db2f3a0-6bed-48a8-9aae-b9bff0deae01";
    private ImageView personalPhoto;
    private Trainee trainee;
    private String[] cities = {"Ramallah", "Hebron", "Salfit", "Nablus", "Jerusalem", "Jenin", "Bethlehem", "Jericho", "Gaza City", "Tulkarm", "Qalqilya", "Rafah", "Khan Yunis", "Beit Lahia", "Beit Hanoun", "Tubas", "Al-Bireh", "Dura", "Beit Jala", "Khan Younis", "Abu Dis", "Rafat", "Deir al-Balah", "Jabalia", "Al-Auja", "Anabta", "Qalqas", "As-Samu", "Other"};
    private AutoCompleteTextView address;
    private ArrayAdapter<String> adapterItems;
    int currentField = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        db = FirebaseFirestore.getInstance();
        personalPhoto = findViewById(R.id.personalPhoto);
        TextView emailBox = findViewById(R.id.emailBox);
        TextView emailView = findViewById(R.id.email);
        EditText firstName = findViewById(R.id.firstname);
        EditText lastName = findViewById(R.id.lastname);
        EditText mobileNum = findViewById(R.id.mobile_number);
        TextView name = findViewById(R.id.name);
        Button update = findViewById(R.id.update_btn);

        address = findViewById(R.id.cities);
        adapterItems = new ArrayAdapter<String>(this, R.layout.item_drop_down, cities);
        address.setAdapter(adapterItems);



        trainee = new Trainee();

        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        trainee.setEmail(email);
                        trainee.setFirstName(document.getString("firstName"));
                        trainee.setLastName(document.getString("lastName"));
                        trainee.setPictureObj(document.getString("personalPhoto"));
                        Picasso.get().load(trainee.getPictureObj()).into(personalPhoto);
                        firstName.setText(trainee.getFirstName());
                        lastName.setText(trainee.getLastName());
                        emailBox.setText(email);
                        emailView.setText(email);
                        name.setText(trainee.getFullName());
                    } else {
                    }
                } else {
                }
            }
        });

        DocumentReference docRef2 = db.collection("User").document(email).collection("Trainee").document(email);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        trainee.setAddress(document.getString("address"));
                        trainee.setMobileNumber(document.getString("mobileNumber"));
                        address.setText(trainee.getAddress());
                        mobileNum.setText(trainee.getMobileNumber());
                    } else {
                    }
                } else {
                }
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
                    currentField = 0;
                    String newFirstName = firstName.getText().toString();
                    if (Trainee.isValidName(newFirstName)) {
                        db.collection("User").document(email)
                                .update(
                                        "firstName", newFirstName
                                );
                        Toast.makeText(MyProfile.this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                        trainee.setFirstName(newFirstName);
                        name.setText(trainee.getFullName());
                    }else{
                        Toast.makeText(MyProfile.this, "Enter a valid first name!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    currentField = 1;
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currentField = 0;
                    String newLastName = lastName.getText().toString();
                    if (Trainee.isValidName(newLastName)) {
                        db.collection("User").document(email)
                                .update(
                                        "lastName", newLastName
                                );
                        Toast.makeText(MyProfile.this, "Last name updated successfully", Toast.LENGTH_SHORT).show();
                        trainee.setLastName(newLastName);
                        name.setText(trainee.getFullName());
                    }else{
                        Toast.makeText(MyProfile.this, "Enter a valid last name!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    currentField = 2;
                }
            }
        });


        mobileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currentField = 0;
                    String newMobile = mobileNum.getText().toString();
                    if (Trainee.isValidMobileNumber(newMobile)) {
                        db.collection("User").document(email).collection("Trainee").document(email)
                                .update(
                                        "mobileNumber", newMobile
                                );
                        Toast.makeText(MyProfile.this, "Mobile No. updated successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MyProfile.this, "Invalid mobile No.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    currentField = 3;
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentField == 1){
                    String newFirstName = firstName.getText().toString();
                    if (Trainee.isValidName(newFirstName)) {
                        db.collection("User").document(email)
                                .update(
                                        "firstName", newFirstName
                                );
                        Toast.makeText(MyProfile.this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                        trainee.setFirstName(newFirstName);
                        name.setText(trainee.getFullName());
                    }else{
                        Toast.makeText(MyProfile.this, "Enter a valid first name!", Toast.LENGTH_SHORT).show();
                    }
                }else if (currentField == 2){
                    String newLastName = lastName.getText().toString();
                    if (Trainee.isValidName(newLastName)) {
                        db.collection("User").document(email)
                                .update(
                                        "lastName", newLastName
                                );
                        Toast.makeText(MyProfile.this, "Last name updated successfully", Toast.LENGTH_SHORT).show();
                        trainee.setLastName(newLastName);
                        name.setText(trainee.getFullName());
                    }else{
                        Toast.makeText(MyProfile.this, "Enter a valid last name!", Toast.LENGTH_SHORT).show();
                    }
                }else if (currentField == 3){
                    String newMobile = mobileNum.getText().toString();
                    if (Trainee.isValidMobileNumber(newMobile)) {
                        db.collection("User").document(email).collection("Trainee").document(email)
                                .update(
                                        "mobileNumber", newMobile
                                );
                        Toast.makeText(MyProfile.this, "Mobile No. updated successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MyProfile.this, "Invalid mobile No.", Toast.LENGTH_SHORT).show();
                    }
                }
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
    protected void onResume() {
        super.onResume();
        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                db.collection("User").document(email).collection("Trainee").document(email)
                        .update(
                                "address", item
                        );
                Toast.makeText(MyProfile.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
