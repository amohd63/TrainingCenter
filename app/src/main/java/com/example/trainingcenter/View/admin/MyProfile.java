package com.example.trainingcenter.View.admin;

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

import com.example.trainingcenter.Model.Admin;
import com.example.trainingcenter.Model.Trainee;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyProfile extends AppCompatActivity {
    private String email;
    private FirebaseFirestore db;
    private final int GALLERY_REQ_CODE = 1000;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fadmin_default.png?alt=media&token=6db2f3a0-6bed-48a8-9aae-b9bff0deae01";
    private ImageView personalPhoto;

    private Admin adminO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        db = FirebaseFirestore.getInstance();
        personalPhoto = findViewById(R.id.personalPhoto);
        TextView emailBox = findViewById(R.id.emailBox);
        TextView emailView = findViewById(R.id.email);
        EditText firstName = findViewById(R.id.firstname);
        EditText lastName = findViewById(R.id.lastname);
        TextView name = findViewById(R.id.name);

        adminO = new Admin();

        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        adminO.setEmail(email);
                        adminO.setFirstName(document.getString("firstName"));
                        adminO.setLastName(document.getString("lastName"));
                        adminO.setPictureObj(document.getString("personalPhoto"));
                        Picasso.get().load(adminO.getPictureObj()).into(personalPhoto);
                        firstName.setText(adminO.getFirstName());
                        lastName.setText(adminO.getLastName());
                        emailBox.setText(email);
                        emailView.setText(email);
                        name.setText(adminO.getFullName());
                    } else {
                    }
                } else {
                }
            }
        });

        emailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(com.example.trainingcenter.View.admin.MyProfile.this, "Email uneditable", Toast.LENGTH_SHORT).show();
            }
        });

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newFirstName = firstName.getText().toString();
                    if (Trainee.isValidName(newFirstName)) {
                        db.collection("User").document(email)
                                .update(
                                        "firstName", newFirstName
                                );
                        Toast.makeText(com.example.trainingcenter.View.admin.MyProfile.this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                        adminO.setFirstName(newFirstName);
                        name.setText(adminO.getFullName());
                    }else{
                        Toast.makeText(com.example.trainingcenter.View.admin.MyProfile.this, "Enter a valid first name!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newLastName = lastName.getText().toString();
                    if (Trainee.isValidName(newLastName)) {
                        db.collection("User").document(email)
                                .update(
                                        "lastName", newLastName
                                );
                        Toast.makeText(com.example.trainingcenter.View.admin.MyProfile.this, "Last name updated successfully", Toast.LENGTH_SHORT).show();
                        adminO.setLastName(newLastName);
                        name.setText(adminO.getFullName());
                    }else{
                        Toast.makeText(com.example.trainingcenter.View.admin.MyProfile.this, "Enter a valid last name!", Toast.LENGTH_SHORT).show();
                    }
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
                                        Toast.makeText(com.example.trainingcenter.View.admin.MyProfile.this, "Personal photo updated successfully", Toast.LENGTH_SHORT).show();
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