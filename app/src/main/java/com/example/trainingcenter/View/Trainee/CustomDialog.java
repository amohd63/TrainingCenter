package com.example.trainingcenter.View.Trainee;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.Model.CourseOffering;
import com.example.trainingcenter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CustomDialog extends AppCompatDialogFragment {
    private CustomDialogListener listener;
    private Course course;
    private CourseOffering courseOffering;
    private FirebaseFirestore db;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_trainee, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        db = FirebaseFirestore.getInstance();
        ImageButton close = view.findViewById(R.id.course_details_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        TextView tv = view.findViewById(R.id.start_date);
        tv.setText(course.getCourseID());

        Button enroll = view.findViewById(R.id.enroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ImageView coursePhoto = view.findViewById(R.id.course_photo);
        Picasso.get().load(course.getCoursePhoto()).into(coursePhoto);
        return dialog;
    }



    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        try {
//            listener = (CustomDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement ExampleDialogListener");
//        }
    }

    public interface CustomDialogListener {
        void enroll(String username, String password);
    }
}
