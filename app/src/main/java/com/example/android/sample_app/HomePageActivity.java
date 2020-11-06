package com.example.android.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView mNavigationView;
    TextView tvError;
    private ProgressBar mProgressBar;
    RecyclerView rvDetails;
    public static final int DISPLAY_SHOW_TITLE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_detail);
        rvDetails = (RecyclerView) findViewById(R.id.rv_detail);
        tvError = (TextView) findViewById(R.id.tv_error);
        toggle = new ActionBarDrawerToggle(HomePageActivity.this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.bringToFront();

        getSupportActionBar().setTitle("Home Page");

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_profile){
                    onBackPressed();
                    Intent intent = new Intent(HomePageActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        LinearLayoutManager detailsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDetails.setLayoutManager(detailsLayoutManager);

        try {
            URL detailUrl = AppUtils.buildUrl();
            new DetailQueryTask().execute(detailUrl);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public class DetailQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {

            URL searchUrl = urls[0];
            String result = null;

            try {
                result = AppUtils.getJSON(searchUrl);
            } catch (IOException e) {
                Log.e("Error ", e.getMessage().toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.INVISIBLE);

            if (result == null) {
                rvDetails.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
                tvError.setText("Error while loading data");
            } else {
                tvError.setVisibility(View.INVISIBLE);
            }
            ArrayList<Details> details = AppUtils.getDetailsFromJson(result);
            Log.d("Error: ", result);
            Log.d("Error: ", String.valueOf(details.size()));

            Details_Adapter adapter = new Details_Adapter(details, HomePageActivity.this);
            rvDetails.setAdapter(adapter);

        }
    }
}