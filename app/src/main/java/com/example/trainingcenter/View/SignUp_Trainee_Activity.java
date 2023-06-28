package com.example.trainingcenter.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainingcenter.Model.User;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Trainee_Activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupPasswordConfirm, firstName, lastName, phoneNumField, addressField;
    private Button signupButton;
    private TextView loginRedirectText;
    private FirebaseFirestore db;
    private ImageView personalPhoto;
    private final int GALLERY_REQ_CODE = 1000;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Ftrainee_default.png?alt=media&token=f4d8a2fd-787d-4af5-9f4d-7cfb5327b8f0";
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_trainee);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email_trainee);
        signupPassword = findViewById(R.id.signup_password_trainee);
        db = FirebaseFirestore.getInstance();
        signupButton = findViewById(R.id.signup_button_trainee);
        loginRedirectText = findViewById(R.id.loginRedirectText_trainee);
        signupPasswordConfirm = findViewById(R.id.signup_password_admin_confirm);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        personalPhoto = findViewById(R.id.personalPhoto);
        phoneNumField = findViewById(R.id.phoneNumber);
        addressField = findViewById(R.id.address);
        personalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String pass_conf = signupPasswordConfirm.getText().toString().trim();
                String firstName_str = firstName.getText().toString().trim();
                String lastName_str = lastName.getText().toString().trim();
                String phoneNum = phoneNumField.getText().toString().trim();
                String address = addressField.getText().toString().trim();

                if(!User.isValidName(firstName_str)){
                    firstName.setError("Invalid Name\nThe name must be 3 and 20 characters");
                }
                else if(!User.isValidName(lastName_str)){
                    lastName.setError("Invalid Name\nThe name must be 3 and 20 characters");
                }
                else if (email.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                else if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }
                else if (pass_conf.isEmpty()){
                    signupPasswordConfirm.setError("You must confirm your Password");
                }
                else if (!pass_conf.equals(pass)){
                    signupPassword.setError("Passwords must match");
                }
                else if (phoneNum.isEmpty()){
                    phoneNumField.setError("This field is required");
                }
                else if (address.isEmpty()){
                    addressField.setError("This field is required");
                }
                else if (!validatePassword(pass)){
                    signupPassword.setError("Invalid Password\nMinimum 8 characters and maximum 15 characters\n" +
                            "It must contain at least one number, one lowercase letter, and one uppercase letter.");
                }
                else{
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp_Trainee_Activity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp_Trainee_Activity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUp_Trainee_Activity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    user.put("firstName", firstName_str);
                    user.put("lastName", lastName_str);
                    user.put("personalPhoto", imgUrl);
                    user.put("role", "Trainee");
                    db.collection("User").document(email).set(user);
                    user.clear();
                    user.put("mobileNumber", phoneNum);
                    user.put("address", address);
                    db.collection("User").document(email).collection("Trainee").document(email).set(user);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp_Trainee_Activity.this, LoginActivity.class));
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_REQ_CODE){
                selectedImageUri = data.getData();
                String fileName = String.valueOf(System.currentTimeMillis());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);
                storageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            storageRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        String imageUrl = uri.toString();
                                        Picasso.get().load(imageUrl).into(personalPhoto);
                                        imgUrl = uri.toString();
                                    })
                                    .addOnFailureListener(exception -> {
                                    });
                        })
                        .addOnFailureListener(exception -> {
                        });
            }
        }
    }
    public static boolean validatePassword(String pass) {
        // Minimum 8 characters and maximum 15 characters
        if (pass.length() < 8 || pass.length() > 15) {
            return false;
        }

        // At least one number, one lowercase letter, and one uppercase letter
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).+$");
        Matcher matcher = pattern.matcher(pass);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
}