package com.example.trainingcenter.View.Trainee;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trainingcenter.R;

public class Lectures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures_trainee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
