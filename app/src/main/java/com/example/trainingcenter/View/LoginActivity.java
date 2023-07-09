package com.example.trainingcenter.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.gbuttons.GoogleSignInButton;
import com.example.trainingcenter.R;
import com.example.trainingcenter.View.Trainee.Home;
import com.example.trainingcenter.View.Trainee.MyLoadingButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, MyLoadingButton.MyLoadingButtonClick {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    //private Button loginButton;
    private FirebaseFirestore db;
    private CheckBox rememberMe;
    private FirebaseAuth auth;
    private ImageView personalPhoto;
    TextView forgotPassword;
    GoogleSignInButton googleBtn;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    MyLoadingButton loginButton;
    TextView name;
    final int[] i = {0};
    String[] role = {""};

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "myKey";

    public static void saveData(Context context, String text) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, text);
        editor.apply();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(KEY, "NULL");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setMyButtonClickListener(this);
        //signupRedirectText = findViewById(R.id.signUpRedirectText);
        forgotPassword = findViewById(R.id.forgot_password);
        googleBtn = findViewById(R.id.googleBtn);
        rememberMe = findViewById(R.id.rememberMe);
        personalPhoto = findViewById(R.id.login_personalphoto);
        name = findViewById(R.id.name);
        setLoadingButtonStyle();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (!loadData(getApplicationContext()).equals("NULL") && !loadData(getApplicationContext()).equals("false")) {
            loginEmail.setText(loadData(getApplicationContext()));
        } else {
            //loginEmail.setText("");
        }

//        loginEmail.setText("instructor@gmail.com");
//        loginPassword.setText("282610As");

//        loginEmail.setText("mf@gmail.com");
//        loginPassword.setText("123456789Mf");

//        loginEmail.setText("ali@gmail.com");
//        loginPassword.setText("123456");
        loginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String[] documentData = new String[1];
                    DocumentReference docRef = db.collection("User").document(loginEmail.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    documentData[0] = document.getString("personalPhoto");
                                    role[0] = document.getString("role");
                                    String fullName = document.getString("firstName") + " " + document.getString("lastName");
                                    name.setText(fullName);
                                    Picasso.get().load(documentData[0]).into(personalPhoto);
                                } else {
                                }
                            } else {
                            }
                        }
                    });
                }
            }
        });

        loginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!loginEmail.getText().toString().isEmpty()) {
                        final String[] documentData = new String[1];
                        DocumentReference docRef = db.collection("User").document(loginEmail.getText().toString());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        documentData[0] = document.getString("personalPhoto");
                                        role[0] = document.getString("role");
                                        String fullName = document.getString("firstName") + " " + document.getString("lastName");
                                        name.setText(fullName);
                                        Picasso.get().load(documentData[0]).into(personalPhoto);
                                    } else {
                                    }
                                } else {
                                }
                            }
                        });
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();

                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText(LoginActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
        //Inside onCreate
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                task.getResult(ApiException.class);
                                Intent intent = new Intent(LoginActivity.this, Home.class);
                                startActivity(intent);
                                finish();
                            } catch (ApiException e) {
                                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = gClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });
        loginButton.setOnClickListener(this);
    }

    private void setLoadingButtonStyle() {

        loginButton.setAnimationDuration(1000)
                .setButtonColor(R.color.lavender)
                .setButtonLabel("Login")
                .setButtonLabelSize(20)
                .setProgressLoaderColor(Color.WHITE)
                .setButtonLabelColor(R.color.white);

    }

    @Override
    public void onClick(View view) {
        loginButton.showLoadingButton();
        String email = loginEmail.getText().toString();
        String pass = loginPassword.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                final String[] documentData = new String[1];
                                DocumentReference docRef = db.collection("User").document(loginEmail.getText().toString());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                documentData[0] = document.getString("personalPhoto");
                                                role[0] = document.getString("role");
                                                String fullName = document.getString("firstName") + " " + document.getString("lastName");
                                                name.setText(fullName);
                                                Picasso.get().load(documentData[0]).into(personalPhoto);
                                                loginButton.showDoneButton();
                                            } else {
                                            }
                                        } else {
                                            loginButton.showErrorButton();
                                        }
                                    }
                                });
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (role[0].equals("Trainee")) {
                                            Intent intent = new Intent(LoginActivity.this, com.example.trainingcenter.View.Trainee.Home.class);
                                            intent.putExtra("email", email);
                                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        } else if (role[0].equals("Instructor")) {
                                            Intent intent = new Intent(LoginActivity.this, com.example.trainingcenter.View.Instructor.Home.class);
                                            intent.putExtra("email", email);
                                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }else if (role[0].equals("Admin")) {
                                            Intent intent = new Intent(LoginActivity.this, com.example.trainingcenter.View.admin.home_admin.class);
                                            intent.putExtra("email", email);
                                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(LoginActivity.this, "Couldn't fetch the role", Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    }
                                }, 3500); // Delay time in milliseconds (e.g., 1000ms = 1 second)
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                loginButton.showErrorButton();
                            }
                        });
            } else {
                loginPassword.setError("Empty fields are not allowed");
                loginButton.showErrorButton();
            }
        } else if (email.isEmpty()) {
            loginEmail.setError("Empty fields are not allowed");
            loginButton.showErrorButton();
        } else {
            loginEmail.setError("Please enter correct email");
            loginButton.showErrorButton();
        }
        if (rememberMe.isChecked()) {
            saveData(getApplicationContext(), loginEmail.getText().toString());
        } else if (!rememberMe.isChecked()) {
            saveData(getApplicationContext(), "false");
        }
    }

    @Override
    public void onMyLoadingButtonClick() {
        //Toast.makeText(this, "MyLoadingButton Click", Toast.LENGTH_SHORT).show();
    }
}