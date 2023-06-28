package com.example.trainingcenter.View.admin;

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

public class add_courses extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseFirestore db2;
    EditText cName, cMainTopics, cPre;
    Button addCourseAdmin;
    private final int GALLERY_REQ_CODE = 1000;
    Uri selectedImageUri;
    private ImageView coursePhoto;
    private String imgUrl = "https://firebasestorage.googleapis.com/v0/b/training-center-new.appspot.com/o/images%2Fcourse_default.png?alt=media&token=68dd1b73-90b6-4cb9-ac91-460e3dfe6768&fbclid=IwAR0iXsAX8uaRcIH71NxiN0bDtrkUFm0MS_aZaLxUKhtZj4PUxrW_jZ0DEEE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);
        cName = findViewById(R.id.course_title_insert);
        cMainTopics = findViewById(R.id.course_main_topics_insert);
        cPre = findViewById(R.id.prerequisites_insert);
        addCourseAdmin = findViewById(R.id.add_coutse_admin_burron);
        coursePhoto = findViewById(R.id.coursePhoto);
        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();

        coursePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });
        addCourseAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c_name = cName.getText().toString();

                String c_main_topics[] = cMainTopics.getText().toString().split("\n");
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(c_main_topics));

                String c_pre[] = cPre.getText().toString().split("\n");

                UUID uuid = UUID.randomUUID();
                String courseID = uuid.toString().replace("-", "").substring(0, 20);

                Map<String, Object> course = new HashMap<>();
                course.put("courseTitle", c_name);
                course.put("mainTopics", arrayList);
                course.put("isAvailableForRegistration", false);
                course.put("courseID", courseID);
                course.put("photo",imgUrl);
                for (int i = 0; i < c_pre.length; i++) {
                    CollectionReference courseRef = db.collection("Course");
                    Query courseQuery = courseRef.whereEqualTo("courseTitle", c_pre[i]);
                    courseQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String courseId2 = document.getId();
                                    UUID uuid = UUID.randomUUID();
                                    String prerequisiteID = uuid.toString().replace("-", "").substring(0, 20);
                                    Map<String, Object> pre = new HashMap<>();
                                    pre.put("courseID",courseID);
                                    pre.put("prerequisiteCourseID",courseId2);
                                    pre.put("prerequisiteID",prerequisiteID);
                                    db.collection("Prerequisite").document(prerequisiteID).set(pre);
                                }
                            }
                        }

                    });
                }
                db.collection("Course").document(courseID).set(course);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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