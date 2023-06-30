package com.example.trainingcenter.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        getSupportActionBar().hide();
        btnInstructor = findViewById(R.id.btnInstructor);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnTrainee = findViewById(R.id.btnTrainee);

        //loginRedirectText = findViewById(R.id.loginRedirectText_admin);

        btnInstructor.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, SignupActivityInstructor.class)));

        btnAdmin.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, SignupActivityAdmin.class)));

        btnTrainee.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, SignupActivityTrainee.class)));

        //loginRedirectText.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

    }
}