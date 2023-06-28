package com.example.trainingcenter.View.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.example.trainingcenter.Model.Course;
import com.example.trainingcenter.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class available_dialog extends AppCompatDialogFragment {
    private FirebaseFirestore db;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_available_dialog, null);
        builder.setView(view);

        Bundle args = getArguments();
        String documentPath = args.getString("pointer");
        DocumentReference docRef = FirebaseFirestore.getInstance().document(documentPath);

        EditText registration_deadline = view.findViewById(R.id.new_registration_in_make_available);
        EditText course_start_date = view.findViewById(R.id.new_start_date_in_make_available);
        EditText course_schedule = view.findViewById(R.id.new_course_schedule_in_make_available);
        EditText venue = view.findViewById(R.id.new_venue_in_make_available);
        EditText instroctor = view.findViewById(R.id.new_Instructor_in_make_available);
        Button mKa = view.findViewById(R.id.make_available_in_make_available_daialog);
        ImageButton mma = view.findViewById(R.id.close_make_availbal_dialog_now);
        db = FirebaseFirestore.getInstance();
        mKa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timestamp timestamp1 = Timestamp.valueOf(registration_deadline.getText().toString());
                Timestamp timestamp2 = Timestamp.valueOf(course_start_date.getText().toString());
                String venuePlace = venue.getText().toString();
                String schedule = course_schedule.getText().toString();
                String instroctorw = instroctor.getText().toString();
                UUID uuid = UUID.randomUUID();
                String offeringID = uuid.toString().replace("-", "").substring(0, 20);
                String cId = docRef.getId();
                Map<String, Object> offer = new HashMap<>();
                offer.put("registrationDeadline",timestamp1);
                offer.put("startDate",timestamp2);
                offer.put("venue",venuePlace);
                offer.put("schedule",schedule);
                offer.put("offeringID",offeringID);
                offer.put("courseID",cId);
                offer.put("instructorID",instroctorw);
                db.collection("CourseOffering").document(offeringID).set(offer);
                dismiss();
                getActivity().recreate();
            }
        });
        mma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}