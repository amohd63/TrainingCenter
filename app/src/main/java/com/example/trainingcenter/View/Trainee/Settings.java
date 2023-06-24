package com.example.trainingcenter.View.Trainee;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import com.example.trainingcenter.R;


public class Settings extends AppCompatActivity {

    ListView lst;
    String name[] = {"About Classroom App", "Rate Me", "Refer Classroom App To Your Friend", "Email Your Feedback", "Report a bug"};
    String num[] = {"Version 1.0", "Application", "Share this app with friends", "Tell me your suggestions", "Tell me if you found any problem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_trainee);
        lst = findViewById(R.id.listView);
        CustomAdapterSetting adapter = new CustomAdapterSetting(this, name, num);
        lst.setAdapter(adapter);
    }
}
