package com.example.android.sample_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    TextView first_name, last_name, email, mobile_number, address;
    FirebaseAuth mAuth;
    Button log_out;
    DatabaseReference mDatabase;
    ProgressBar mProgressBar;
    public static final int DISPLAY_SHOW_TITLE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        first_name = findViewById(R.id.user_firstName);
        last_name = findViewById(R.id.user_lastName);
        email = findViewById(R.id.user_email);
        mobile_number = findViewById(R.id.user_phone);
        address = findViewById(R.id.user_address);
        mProgressBar = findViewById(R.id.progress_profile);
        log_out = findViewById(R.id.log_out);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("My Profile");

        mDatabase = FirebaseDatabase.getInstance().getReference("user");

        mProgressBar.setVisibility(View.VISIBLE);

        showUserData();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void showUserData() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               userHelper user = snapshot.getValue(userHelper.class);
               try {
                   first_name.setText(user.firstName);
                   last_name.setText(user.lastName);
                   email.setText(user.email);
                   mobile_number.setText(user.mobileNumber);
                   address.setText(user.address);
               } catch (Exception e){
                   Log.e("Error: ", e.getMessage());
                   Toast.makeText(MyProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   finishAffinity();
                   Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(MyProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }
}