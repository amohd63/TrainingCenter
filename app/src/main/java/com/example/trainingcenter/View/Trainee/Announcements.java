package com.example.trainingcenter.View.Trainee;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trainingcenter.R;

public class Announcements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}