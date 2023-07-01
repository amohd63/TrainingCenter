package com.example.trainingcenter.View.admin;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class show_profile_from_admin_all extends AppCompatDialogFragment {
    private ImageView personalPhoto;
    private TextView emailBox2;
    private TextView emailView2;
    private TextView firstName2;
    private TextView lastName2;
    private TextView address2;
    private TextView mobileNum2;
    private TextView name2;
    private TextView degree2;
    private TextView spe2;
    private TextView hide1;
    private TextView hide2;
    private FirebaseFirestore db;
    private ImageButton cloas;
    private String role;
    private String documentPath;
    private DocumentReference docRef33;
    private String email;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_show_profile_from_admin_all, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        db = FirebaseFirestore.getInstance();
        cloas = view.findViewById(R.id.imageButton_for_closing);
        personalPhoto = view.findViewById(R.id.personalPhoto2);
        emailBox2 = view.findViewById(R.id.emailBox2);
        emailView2 = view.findViewById(R.id.email2);
        firstName2 = view.findViewById(R.id.firstname2);
        lastName2 = view.findViewById(R.id.lastname2);
        address2 = view.findViewById(R.id.address2);
        mobileNum2 = view.findViewById(R.id.mobile_number2);
        name2 = view.findViewById(R.id.name2);
        degree2 = view.findViewById(R.id.degree2);
        spe2 = view.findViewById(R.id.specialization);
        hide1 = view.findViewById(R.id.hide_number_one);
        hide2 = view.findViewById(R.id.hide_number_two);
        hide1.setVisibility(View.INVISIBLE);
        hide2.setVisibility(View.INVISIBLE);
        degree2.setVisibility(View.INVISIBLE);
        spe2.setVisibility(View.INVISIBLE);


        Bundle args = getArguments();
        role = args.getString("pointer");
        documentPath = args.getString("pointer1");
        docRef33 = FirebaseFirestore.getInstance().document(documentPath);
        email = docRef33.getId();

        if(role.equals("Instructor")){
            hide1.setVisibility(View.VISIBLE);
            hide2.setVisibility(View.VISIBLE);
            degree2.setVisibility(View.VISIBLE);
            spe2.setVisibility(View.VISIBLE);
        }

        if(role.equals("Trainee")) {
            DocumentReference docRef = db.collection("User").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Picasso.get().load(document.getString("personalPhoto")).into(personalPhoto);
                            String documentData = document.getString("firstName") + " " + document.getString("lastName");
                            name2.setText(documentData);
                            emailView2.setText(email);
                            firstName2.setText(document.getString("firstName"));
                            lastName2.setText(document.getString("lastName"));
                            emailBox2.setText(email);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef2 = db.collection("User").document(email).collection("Trainee").document(email);
            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            address2.setText(document.getString("address"));
                            mobileNum2.setText(document.getString("mobileNumber"));
                        } else {
                        }
                    } else {
                    }
                }
            });
        }
        else if(role.equals("Instructor")){
            DocumentReference docRef = db.collection("User").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Picasso.get().load(document.getString("personalPhoto")).into(personalPhoto);
                            String documentData = document.getString("firstName") + " " + document.getString("lastName");
                            name2.setText(documentData);
                            emailView2.setText(email);
                            firstName2.setText(document.getString("firstName"));
                            lastName2.setText(document.getString("lastName"));
                            emailBox2.setText(email);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef2 = db.collection("User").document(email).collection("Instructor").document(email);
            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            address2.setText(document.getString("address"));
                            mobileNum2.setText(document.getString("mobileNumber"));
                            degree2.setText(document.getString("degree"));
                            spe2.setText(document.getString("specialization"));
                        } else {
                        }
                    } else {
                    }
                }
            });
        }

        cloas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialog;
    }
}