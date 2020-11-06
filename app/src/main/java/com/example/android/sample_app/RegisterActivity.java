package com.example.android.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText first_name_reg, last_name_reg, email_reg, password_reg, confirm_password_reg, mobile_number_reg, address_reg;
    Button sign_up_btn;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser mUser;
    public static final int DISPLAY_SHOW_TITLE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        first_name_reg = findViewById(R.id.username_first_name);
        last_name_reg = findViewById(R.id.username_last_name);
        email_reg = findViewById(R.id.email_register);
        password_reg = findViewById(R.id.password_register);
        confirm_password_reg = findViewById(R.id.confirm_password_register);
        mobile_number_reg = findViewById(R.id.mobile_number_register);
        address_reg = findViewById(R.id.address_register);
        sign_up_btn = findViewById(R.id.signUp);

        getSupportActionBar().setTitle("Register");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        final String firstName = first_name_reg.getText().toString().trim();
        final String lastName = last_name_reg.getText().toString().trim();
        final String email = email_reg.getText().toString().trim();
        final String password = password_reg.getText().toString().trim();
        String confirmPassword = confirm_password_reg.getText().toString().trim();
        final String phone = mobile_number_reg.getText().toString().trim();
        final String address = address_reg.getText().toString().trim();

        if(firstName.isEmpty()){
            first_name_reg.setError("Field cannot be empty");
            first_name_reg.requestFocus();
            return;
        }

        if(lastName.isEmpty()){
            last_name_reg.setError("Field cannot be empty");
            last_name_reg.requestFocus();
            return;
        }

        if(email.isEmpty()){
            email_reg.setError("Field cannot be empty");
            email_reg.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_reg.setError("Enter a valid email");
            email_reg.requestFocus();
            return;
        }

        if(password.isEmpty()){
            password_reg.setError("Field cannot be empty");
            password_reg.requestFocus();
            return;
        }

        if (!confirmPassword.equals(password)){
            confirm_password_reg.setError("Confirm Password should be same as password");
            return;
        }

        if(phone.isEmpty()){
            mobile_number_reg.setError("Field cannot be empty");
            mobile_number_reg.requestFocus();
            return;
        }

        if(address.isEmpty()){
            address_reg.setError("Field cannot be empty");
            address_reg.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    userHelper user_credentials = new userHelper(firstName, lastName, email, password, phone, address);
                    databaseReference.child(mAuth.getCurrentUser().getUid())
                            .setValue(user_credentials).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendEmailVerification();
                        }
                    });
                }
            }
        });

    }

    private void sendEmailVerification() {

        mUser = mAuth.getCurrentUser();
        if(mUser!=null){
            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Successfully registered. Verification mail sent!!Kindly login again..", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Verification mail hasn't been sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}