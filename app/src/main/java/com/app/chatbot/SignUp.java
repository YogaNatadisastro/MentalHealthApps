package com.app.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.chatbot.Model.UserDetails;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    private TextInputEditText editTextEmail, editTextPassword, editTextNama;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView textView;
    private static final String TAG = "SignUpActivity";

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        editTextNama = findViewById(R.id.nama);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btn_sign);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login_user.class);
            startActivity(intent);
            finish();
        });

        btnSignUp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password, nama;

            nama = String.valueOf(editTextNama.getText());
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(nama)){
                Toast.makeText(SignUp.this, "Enter Nama", Toast.LENGTH_LONG).show();
                editTextNama.requestFocus();
            } else if (TextUtils.isEmpty(password)){
                Toast.makeText(SignUp.this, "Enter password", Toast.LENGTH_LONG).show();
                editTextPassword.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_LONG).show();
                editTextEmail.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                registerUser(nama, email, password);
            }
    });
}

    private void registerUser(String nama, String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, task -> {
            if (task.isSuccessful()) {

                // Get Uid from Auth Database
                FirebaseUser fUser = auth.getCurrentUser();
                String uid = fUser.getUid();
                String role = "user";

                UserDetails userDetails = new UserDetails(uid, nama, email, password, role);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                reference.child(fUser.getUid()).setValue(userDetails).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                
                        fUser.sendEmailVerification();

                        //  Notification
                        Toast.makeText(SignUp.this, "Account create", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignUp.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Account Registered Failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "registerUser: Error");
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException e) {
                    editTextEmail.setError("Email is Already registered");
                } catch (Exception e) {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(SignUp.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}