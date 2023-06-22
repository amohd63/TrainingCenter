package com.example.trainingcenter.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Instructor_Activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupPasswordConfirm, firstName, lastName, phoneNumField, addressField, specializationField, courseField;
    private Button signupButton, addCourse;
    private TextView loginRedirectText;
    private RadioGroup degreeRadioGroup;
    private FirebaseFirestore db;
    private ImageView personalPhoto;
    String degree = "";
    private final int GALLERY_REQ_CODE = 1000;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fsignup_default.jpg?alt=media&token=83206b02-8fdc-40a1-8259-e39ad0d78d24";
    private ArrayList<String> instructorCourses = new ArrayList<>();
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_instructor);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email_instructor);
        signupPassword = findViewById(R.id.signup_password_instructor);
        db = FirebaseFirestore.getInstance();
        signupButton = findViewById(R.id.signup_button_instructor);
        loginRedirectText = findViewById(R.id.loginRedirectText_instructor);
        signupPasswordConfirm = findViewById(R.id.signup_password_admin_confirm);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phoneNumField = findViewById(R.id.phoneNumber);
        addressField = findViewById(R.id.address);
        personalPhoto = findViewById(R.id.personalPhoto);
        specializationField = findViewById(R.id.specialization);
        degreeRadioGroup = findViewById(R.id.degreeRadioGroup);
        addCourse = findViewById(R.id.add_course);
        courseField = findViewById(R.id.course);


        degree = "";
        degreeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Find the selected radio button
                RadioButton selectedRadioButton = findViewById(checkedId);
                // Get the text of the selected radio button
                degree = selectedRadioButton.getText().toString();
                Toast.makeText(getApplicationContext(), "Selected option: " + degree, Toast.LENGTH_SHORT).show();
            }
        });



        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructorCourses.add(courseField.getText().toString());
                courseField.setText("");
                courseField.setHint("Add course");
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
                String specialization = specializationField.getText().toString().trim();


                if (email.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
//                    signupEmail.setBackgroundColor(Color.RED);
                }
                else if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
//                    signupPassword.setBackgroundColor(Color.RED);
                }
                 else if (pass_conf.isEmpty()){
                     signupPasswordConfirm.setError("You must confirm your Password");
//                    signupPasswordConfirm.setBackgroundColor(Color.RED);
                 }
                 else if (!pass_conf.equals(pass)){
                     signupPassword.setError("Passwords must match");
//                    signupPassword.setBackgroundColor(Color.RED);
                 }
                else if(!User.isValidName(firstName_str)){
                    firstName.setError("Invalid Name\nThe name must be 3 and 20 characters");
//                    firstName.setHighlightColor(Color);
                }
                else if(!User.isValidName(lastName_str)){
                    lastName.setError("Invalid Name\nThe name must be 3 and 20 characters");
//                    lastName.setBackgroundColor(Color.RED);
                }
                else if (address.isEmpty()){
                    addressField.setError("This field is required");
//                    addressField.setBackgroundColor(Color.RED);
                }
                 else if (phoneNum.isEmpty()){
                     phoneNumField.setError("This field is required");
//                    phoneNumField.setBackgroundColor(Color.RED);
                 }
                else if (specialization.isEmpty()){
                    specializationField.setError("This field is required");
//                    specializationField.setBackgroundColor(Color.RED);
                }
                else if (degree.isEmpty()){
                    Toast.makeText(getApplicationContext(), "You must select a degree", Toast.LENGTH_SHORT).show();
//                    specializationField.setBackgroundColor(Color.RED);
                }
                else if (!validatePassword(pass)){
                    signupPassword.setError("Invalid Password\nMinimum 8 characters and maximum 15 characters\n" +
                            "It must contain at least one number, one lowercase letter, and one uppercase letter.");
//                    signupPassword.setBackgroundColor(Color.RED);
                }
                else{
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp_Instructor_Activity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp_Instructor_Activity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUp_Instructor_Activity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    user.put("firstName", firstName_str);
                    user.put("lastName", lastName_str);
                    user.put("personalPhoto", imgUrl);
                    db.collection("User").document(email).set(user);
                    user.clear();
                    user.put("mobileNumber", phoneNum);
                    user.put("address", address);
                    user.put("specialization", specialization);
                    user.put("degree", degree);
                    user.put("courses", instructorCourses);
                    db.collection("User").document(email).collection("Instructor").document(email).set(user);
                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp_Instructor_Activity.this, LoginActivity.class));
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