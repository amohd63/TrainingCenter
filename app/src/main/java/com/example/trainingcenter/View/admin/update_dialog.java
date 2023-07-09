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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Timestamp;
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
import android.widget.Toast;

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

public class update_dialog extends AppCompatDialogFragment {
    private final int GALLERY_REQ_CODE = 1000;
    Uri selectedImageUri;
    private ImageButton im;
    FirebaseFirestore db;
    private ImageView coursePhoto;
    String nameTosent;
    UUID uuid;

    AlertDialog dialog;

    boolean flag= false;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fcourse_default.png?alt=media&token=68dd1b73-90b6-4cb9-ac91-460e3dfe6768&fbclid=IwAR0iXsAX8uaRcIH71NxiN0bDtrkUFm0MS_aZaLxUKhtZj4PUxrW_jZ0DEEE";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_update_dialog, null);
        builder.setView(view);
        String[] options = { "Course title", "Main topics", "Photo"};
        EditText value =  view.findViewById(R.id.new_value);
        Button updateB = view.findViewById(R.id.update_in_dialog_update);
        Spinner spinner = view.findViewById(R.id.spinner);
        im = view.findViewById(R.id.close_update_dialog_now);
        coursePhoto = view.findViewById(R.id.updateCoursePhoto);
        db =  FirebaseFirestore.getInstance();
        dialog = builder.create();
        coursePhoto.setVisibility(View.INVISIBLE);
        value.setVisibility(View.INVISIBLE);
        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        uuid = UUID.randomUUID();
        Bundle args = getArguments();
        String documentPath = args.getString("pointer");
        nameTosent = args.getString("pointer2");
        DocumentReference docRef = FirebaseFirestore.getInstance().document(documentPath);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String filed = spinner.getSelectedItem().toString();
                if(filed.equals("Course title")){
                    value.setVisibility(View.VISIBLE);
                    coursePhoto.setVisibility(View.INVISIBLE);
                } else if(filed.equals("Main topics")) {
                    value.setVisibility(View.VISIBLE);
                    coursePhoto.setVisibility(View.INVISIBLE);
                } else if(filed.equals("Photo")){
                    coursePhoto.setVisibility(View.VISIBLE);
                    value.setVisibility(View.INVISIBLE);
                }else if(filed.equals("Make Available For Registration")){
                    coursePhoto.setVisibility(View.INVISIBLE);
                    value.setVisibility(View.INVISIBLE);
                }
                coursePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent iGallery = new Intent(Intent.ACTION_PICK);
                        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(iGallery, GALLERY_REQ_CODE);
                    }
                });

                updateB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(filed.equals("Course title")){
                            if(value.getText().toString().isEmpty()){
                                Toast.makeText(dialog.getContext(), "new value is empty", Toast.LENGTH_SHORT).show();
                            }else {
                                String newValue = value.getText().toString();
                                docRef.update("courseTitle", newValue)
                                        .addOnSuccessListener(aVoid -> System.out.println("Document updated successfully"))
                                        .addOnFailureListener(e -> System.out.println("Error updating document: " + e.getMessage()));
                                sendNotifacation(newValue);
                                dismiss();
                                getActivity().recreate();
                            }
                        } else if(filed.equals("Main topics")) {
                            if(value.getText().toString().isEmpty()){
                                Toast.makeText(dialog.getContext(), "new value is empty", Toast.LENGTH_SHORT).show();
                            }else {
                                String newValue[] = value.getText().toString().split(",");
                                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(newValue));
                                docRef.update("mainTopics", arrayList)
                                        .addOnSuccessListener(aVoid -> System.out.println("Document updated successfully"))
                                        .addOnFailureListener(e -> System.out.println("Error updating document: " + e.getMessage()));
                                sendNotifacation(nameTosent);
                                dismiss();
                                getActivity().recreate();
                            }
                        }else if(filed.equals("Photo")){
                            docRef.update("photo", imgUrl)
                                    .addOnSuccessListener(aVoid -> System.out.println("Document updated successfully"))
                                    .addOnFailureListener(e -> System.out.println("Error updating document: " + e.getMessage()));
                            sendNotifacation(nameTosent);
                            dismiss();
                            getActivity().recreate();
                        }
                    }
                });
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        getActivity().recreate();
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                coursePhoto.setVisibility(View.INVISIBLE);
                value.setVisibility(View.INVISIBLE);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
    public void sendNotifacation(String data){
        CollectionReference courseRef = db.collection("Course");
        Query courseQuery = courseRef.whereEqualTo("courseTitle", data);
        courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String courseId = document.getId();
                        // 2. Retrieve offeringIDs by courseID
                        CollectionReference offeringRef = db.collection("CourseOffering");
                        Query offeringQuery = offeringRef.whereEqualTo("courseID", courseId);
                        offeringQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String offeringId = document.getId();
                                        CollectionReference registrationRef = db.collection("Registration");
                                        Query registrationQuery = registrationRef.whereEqualTo("offeringID", offeringId);
                                        registrationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String registrationID = (String) document.get("traineeID");
                                                        CollectionReference userRef = db.collection("User");
                                                        Query userQuery = userRef.whereEqualTo("email", registrationID);
                                                        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        String userId = document.getId();
                                                                        //notification
                                                                        LocalDateTime currentDateTime = LocalDateTime.now();
                                                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                                        String formattedDateTime = currentDateTime.format(formatter);
                                                                        Timestamp timestampNote = Timestamp.valueOf(formattedDateTime);
                                                                        String title = "Update course | " + nameTosent;
                                                                        String body;
                                                                        if(nameTosent.equals(data)) {
                                                                             body = "The course " + nameTosent + " has been updated";
                                                                        }else{
                                                                             body = "The course " + nameTosent + " has been updated so the name now is "+data;
                                                                        }
                                                                        String noteID = uuid.toString().replace("-", "").substring(0, 20);
                                                                        Map<String, Object> note = new HashMap<>();
                                                                        note.put("body", body);
                                                                        note.put("title", title);
                                                                        note.put("userID", userId);
                                                                        note.put("noteDate",timestampNote);
                                                                        note.put("fetch", false);
                                                                        db.collection("Notification").document(noteID).set(note);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Log.w("Firestore", "10Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.w("Firestore", "20Error getting documents.", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.w("Firestore", "30Error getting documents.", task.getException());
                }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_REQ_CODE){
                selectedImageUri = data.getData();
                String fileName = String.valueOf(System.currentTimeMillis());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);
                storageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            storageRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        String imageUrl = uri.toString();
                                        Picasso.get().load(imageUrl).into(coursePhoto);
                                        imgUrl = uri.toString();
                                    })
                                    .addOnFailureListener(exception -> {
                                    });
                        })
                        .addOnFailureListener(exception -> {
                        });
            }
        }
    }

}