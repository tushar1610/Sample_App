package com.example.android.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Password_reset extends AppCompatActivity {

    Button tap_email;
    EditText email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        tap_email = findViewById(R.id.tap_for_email);
        email = findViewById(R.id.enter_email);

        mAuth = FirebaseAuth.getInstance();

        tap_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterEmail();
            }
        });
    }

    private void enterEmail() {

        String userEmail = email.getText().toString().trim();

        if(userEmail.isEmpty()){
            email.setError("Field cannot be empty");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Password_reset.this, "Email link sent", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(Password_reset.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(Password_reset.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}