package com.example.trainingcenter.View.Instructor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trainingcenter.R;

public class ListOfStudents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liststudent_instructor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
