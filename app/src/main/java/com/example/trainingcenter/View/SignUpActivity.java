package com.example.trainingcenter.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trainingcenter.R;

public class SignUpActivity extends AppCompatActivity {
    private Button btnInstructor;
    private Button btnTrainee;
    private Button btnAdmin;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnInstructor = findViewById(R.id.btnInstructor);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnTrainee = findViewById(R.id.btnTrainee);

        loginRedirectText = findViewById(R.id.loginRedirectText_admin);

        btnInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignUp_Instructor_Activity.class));
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignUp_Admin_Activity.class));
            }
        });

        btnTrainee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignUp_Trainee_Activity.class));
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
}