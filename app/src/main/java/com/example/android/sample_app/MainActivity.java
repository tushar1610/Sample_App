package com.example.android.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email_log, password_log;
    Button logIn_btn, register_btn, forgot_pass;
    ProgressBar progress_log;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser mUser;
    public static final int DISPLAY_SHOW_TITLE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_log=findViewById(R.id.email_login);
        password_log=findViewById(R.id.password_login);
        forgot_pass = findViewById(R.id.forgot_password);
        logIn_btn=findViewById(R.id.LogIn);
        register_btn=findViewById(R.id.register);
        progress_log=findViewById(R.id.progress_logIn);

        getSupportActionBar().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Password_reset.class);
                startActivity(intent);
            }
        });
        
        logIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {

        final String email = email_log.getText().toString().trim();
        final String password = password_log.getText().toString().trim();

        if(email.isEmpty()){
            email_log.setError("Field cannot be empty");
            email_log.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_log.setError("Enter a valid email");
            email_log.requestFocus();
            return;
        }

        if(password.isEmpty()){
            password_log.setError("Field cannot be empty");
            password_log.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()){
                    databaseReference.child(mAuth.getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    checkEmailVerification();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkEmailVerification() {

        mUser = mAuth.getCurrentUser();
        Boolean emailFlag = mUser.isEmailVerified();

        if(emailFlag){
            finish();
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            //some user logged in
            finish();
            startActivity(new Intent(MainActivity.this, HomePageActivity.class));
        }

    }
}