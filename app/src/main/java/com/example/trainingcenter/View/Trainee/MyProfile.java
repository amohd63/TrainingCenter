package com.example.trainingcenter.View.Trainee;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        db = FirebaseFirestore.getInstance();
        personalPhoto = findViewById(R.id.personalPhoto);
        final String[] documentData = new String[2];
        TextView emailBox = findViewById(R.id.emailBox);
        TextView emailView = findViewById(R.id.email);
        EditText firstName = findViewById(R.id.firstname);
        EditText lastName = findViewById(R.id.lastname);
        EditText address = findViewById(R.id.address);
        EditText mobileNum = findViewById(R.id.mobile_number);
        TextView name = findViewById(R.id.name);

        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        documentData[0] = document.getString("personalPhoto");
                        Picasso.get().load(documentData[0]).into(personalPhoto);
                        documentData[1] = document.getString("firstName") + " " + document.getString("lastName");
                        name.setText(documentData[1]);
                        firstName.setText(document.getString("firstName"));
                        lastName.setText(document.getString("lastName"));
                        emailBox.setText(email);
                        emailView.setText(email);
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
                        address.setText(document.getString("address"));
                        mobileNum.setText(document.getString("mobileNumber"));
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
                    String newFirstName = firstName.getText().toString();
                    db.collection("User").document(email)
                            .update(
                                    "firstName", newFirstName
                            );
                    Toast.makeText(MyProfile.this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                    String fullName = newFirstName + " " + lastName.getText();
                    name.setText(fullName);
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newFirstName = lastName.getText().toString();
                    db.collection("User").document(email)
                            .update(
                                    "lastName", newFirstName
                            );
                    Toast.makeText(MyProfile.this, "Last name updated successfully", Toast.LENGTH_SHORT).show();
                    String fullName = firstName.getText() + " " + newFirstName;
                    name.setText(fullName);
                }
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newAddress = address.getText().toString();
                    db.collection("User").document(email).collection("Trainee").document(email)
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
                    db.collection("User").document(email).collection("Trainee").document(email)
                            .update(
                                    "mobileNumber", newMobile
                            );
                    Toast.makeText(MyProfile.this, "Mobile No. updated successfully", Toast.LENGTH_SHORT).show();
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
