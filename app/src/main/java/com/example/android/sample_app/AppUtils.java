package com.example.android.sample_app;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AppUtils {

    private AppUtils(){}

    public static final String BASE_API_URL = "https://reqres.in/api/users?page=1";
    public static URL buildUrl(){
        URL url = null;

        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getJSON(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e){
            Log.e("Error", e.getMessage().toString());
            return null;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static ArrayList<Details> getDetailsFromJson(String json){
        ArrayList<Details> detail = new ArrayList<Details>();

        try {
            JSONObject jsonDetails = new JSONObject(json);
            JSONArray arrayDetails = jsonDetails.getJSONArray("data");
            int numOfDetails = arrayDetails.length();
            for (int i = 0; i < numOfDetails; i++){
                JSONObject detailsJSON = arrayDetails.getJSONObject(i);

                Details details1 = new Details(
                        detailsJSON.getString("email"),
                        detailsJSON.getString("first_name"),
                        detailsJSON.getString("last_name"),
                        detailsJSON.getString("avatar")
                );
                detail.add(details1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;
    }


}
