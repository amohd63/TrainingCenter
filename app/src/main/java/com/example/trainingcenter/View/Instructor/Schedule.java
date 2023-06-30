package com.example.trainingcenter.View.Instructor;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.trainingcenter.R;
public class Schedule extends AppCompatActivity {
    TextView Sun1,Sun2,Sun3,Sun4,Sun5,Sun6,
            Mon1,Mon2,Mon3,Mon4,Mon5,Mon6,
            Tue1,Tue2,Tue3,Tue4,Tue5,Tue6,
            Wed1,Wed2,Wed3,Wed4,Wed5,Wed6,
            Thu1,Thu2,Thu3,Thu4,Thu5,Thu6,
            Fri1,Fri2,Fri3,Fri4,Fri5,Fri6,
            Sat1,Sat2,Sat3,Sat4,Sat5,Sat6;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule_instructor);
        Sun1 = findViewById(R.id.Sunday1);
        Sun2 = findViewById(R.id.Sunday2);
        Sun3 = findViewById(R.id.Sunday3);
        Sun4 = findViewById(R.id.Sunday4);
        Sun5 = findViewById(R.id.Sunday5);
        Sun6 = findViewById(R.id.Sunday6);

        Mon1 = findViewById(R.id.Monday1);
        Mon2 = findViewById(R.id.Monday2);
        Mon3 = findViewById(R.id.Monday3);
        Mon4 = findViewById(R.id.Monday4);
        Mon5 = findViewById(R.id.Monday5);
        Mon6 = findViewById(R.id.Monday6);

        Tue1 = findViewById(R.id.Tuesday1);
        Tue2 = findViewById(R.id.Tuesday2);
        Tue3 = findViewById(R.id.Tuesday3);
        Tue4 = findViewById(R.id.Tuesday4);
        Tue5 = findViewById(R.id.Tuesday5);
        Tue6 = findViewById(R.id.Tuesday6);

        Wed1 = findViewById(R.id.Wednesday1);
        Wed2 = findViewById(R.id.Wednesday2);
        Wed3 = findViewById(R.id.Wednesday3);
        Wed4 = findViewById(R.id.Wednesday4);
        Wed5 = findViewById(R.id.Wednesday5);
        Wed6 = findViewById(R.id.Wednesday6);


        Thu1 = findViewById(R.id.Thursday1);
        Thu2 = findViewById(R.id.Thursday2);
        Thu3 = findViewById(R.id.Thursday3);
        Thu4 = findViewById(R.id.Thursday4);
        Thu5 = findViewById(R.id.Thursday5);
        Thu6 = findViewById(R.id.Thursday6);


        Fri1 = findViewById(R.id.Friday1);
        Fri2 = findViewById(R.id.Friday2);
        Fri3 = findViewById(R.id.Friday3);
        Fri4 = findViewById(R.id.Friday4);
        Fri5 = findViewById(R.id.Friday5);
        Fri6 = findViewById(R.id.Friday6);


        Sat1 = findViewById(R.id.Saturday1);
        Sat2 = findViewById(R.id.Saturday2);
        Sat3 = findViewById(R.id.Saturday3);
        Sat4 = findViewById(R.id.Saturday4);
        Sat5 = findViewById(R.id.Saturday5);
        Sat6 = findViewById(R.id.Saturday6);



        Sun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sun1.setText("NLP\nMASRI204");
            }
        });
        Sun2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
        Sun3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Sun4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Sun5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Sun6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Mon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Mon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Mon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Mon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Mon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Mon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Tue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Tue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Tue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Tue4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Tue5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Tue6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Wed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Wed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Wed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Wed4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Wed5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Wed6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Thu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Thu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Thu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Thu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Thu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Thu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Fri1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Fri2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Fri3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Fri4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Fri5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Fri6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Sat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Sat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Sat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Sat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Sat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Sat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });





    }
}