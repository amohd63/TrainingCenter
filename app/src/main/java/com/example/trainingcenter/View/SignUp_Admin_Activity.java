package com.example.trainingcenter.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainingcenter.Model.User;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Admin_Activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupPasswordConfirm, firstName, lastName;
    private Button signupButton;
    private TextView loginRedirectText;
    private final int PICK_IMAGE_REQUEST = 10;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);
        selectedImageUri = Uri.EMPTY;
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email_admin);
        signupPassword = findViewById(R.id.signup_password_admin);
        signupPasswordConfirm = findViewById(R.id.signup_password_admin_confirm);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        signupButton = findViewById(R.id.signup_button_admin);
        loginRedirectText = findViewById(R.id.loginRedirectText_admin);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String pass_conf = signupPasswordConfirm.getText().toString().trim();
                String firstName_str = firstName.getText().toString().trim();
                String lastName_str = lastName.getText().toString().trim();

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
                else if (!validatePassword(pass)){
                    signupPassword.setError("Invalid Password\nMinimum 8 characters and maximum 15 characters\n" +
                            "It must contain at least one number, one lowercase letter, and one uppercase letter.");
                }
                else{
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp_Admin_Activity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp_Admin_Activity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUp_Admin_Activity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp_Admin_Activity.this, LoginActivity.class));
            }
        });

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