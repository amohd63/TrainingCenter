package com.example.trainingcenter.View.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
public class Registration_applications_dialog extends AppCompatDialogFragment {
    DocumentReference docRef;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_registration_applications_dialog, null);
        builder.setView(view);

        Button accept = view.findViewById(R.id.accept_in_dialog_registration_applications);
        Button reject = view.findViewById(R.id.reject_in_dialog_registration_applications);
        ImageButton close = view.findViewById(R.id.close_registration_applications_dialog_now);
        UUID uuid = UUID.randomUUID();
        Bundle args = getArguments();
        String id = args.getString("pointer");
        String userId = args.getString("pointer2");
        String courseTitle = args.getString("pointer3");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Registration");
        CollectionReference Ref = db.collection("Registration");
        Query regstrationQuery = Ref.whereEqualTo("registrationID", id);
        regstrationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String Id = document.getId();
                        docRef = collectionRef.document(Id);
                    }
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.update("status", "Accepted")
                        .addOnSuccessListener(aVoid -> System.out.println("Document updated successfully"))
                        .addOnFailureListener(e -> System.out.println("Error updating document: " + e.getMessage()));
                //notification
                Timestamp timestampNote = Timestamp.now();
                UUID uuid2 = UUID.randomUUID();
                String noteID = uuid.toString().replace("-", "").substring(0, 20);
                String title = "Acceptance | " + courseTitle;
                String body = "Your application to join "+courseTitle+" is accepted. Welcome!";
                Map<String, Object> note = new HashMap<>();
                note.put("body",body);
                note.put("title",title);
                note.put("userID",userId);
                note.put("noteDate",timestampNote);
                note.put("fetch", false);
                db.collection("Notification").document(noteID).set(note);
                dismiss();
                getActivity().recreate();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.update("status", "Rejected")
                        .addOnSuccessListener(aVoid -> System.out.println("Document updated successfully"))
                        .addOnFailureListener(e -> System.out.println("Error updating document: " + e.getMessage()));
                //notification
                UUID uuid2 = UUID.randomUUID();
                String noteID = uuid.toString().replace("-", "").substring(0, 20);
                String title = "Rejection | " + courseTitle;
                String body = "Sadly, your request to enroll in "+courseTitle+" is rejected. See you next times!";
                Map<String, Object> note = new HashMap<>();
                note.put("body",body);
                note.put("title",title);
                note.put("userID",userId);
                Timestamp timestampNote = Timestamp.now();
                note.put("noteDate",timestampNote);
                note.put("fetch", false);
                db.collection("Notification").document(noteID).set(note);
                dismiss();
                getActivity().recreate();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
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