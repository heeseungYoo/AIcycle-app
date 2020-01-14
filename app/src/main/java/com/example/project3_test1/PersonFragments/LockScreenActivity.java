package com.example.project3_test1.PersonFragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.example.project3_test1.R;
import com.example.project3_test1.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;


public class LockScreenActivity extends Activity {

    final static private String URL = "http://192.249.19.252:2080/personalInfo";

    private String userID;
    private int point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_lock_screen);

        userID = SaveSharedPreference.getUserID(this);

        ///Intent intent = getIntent();
        //userID = intent.getStringExtra("userID");

        SwipeButton swipeButton = findViewById(R.id.swipe_btn);
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

                String jsonString = "";
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                try {

                    Random random1 = new Random();
                    int num = random1.nextInt(100);
                    if (num >= 0 && num < 50) {
                        point = 0;
                    }
                    else if (num >= 50 && num < 70) {
                        point = 1;
                    }
                    else if (num >= 70 && num < 90) {
                        point = 2;
                    }
                    else if (num >= 90 && num <100) {
                        point = 3;
                    }

                    jsonObject.put("type","unlock");
                    jsonObject.put("date", "2020-01-15");
                    jsonObject.put("point", point);

                    jsonArray.put(jsonObject);

                    jsonString = jsonObject.toString();
                    System.out.println(jsonString + "-------------------");

                } catch (Exception e) {
                    e.printStackTrace();
                }


/*
                final RequestQueue requestQueue = Volley.newRequestQueue(LockScreenActivity.this);
                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT,
                        URL + "/" + userID, jsonArray, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Toast.makeText(LockScreenActivity.this, point + "point ++", Toast.LENGTH_LONG).show();
                            System.out.println("-----response-----" + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
                );
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonArrayRequest);*/


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(LockScreenActivity.this, point + "point ++", Toast.LENGTH_LONG).show();
                        System.out.println("-----response-----" + response);

                    }
                };

                PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(userID, jsonString, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LockScreenActivity.this);
                queue.add(personalInfoRequest);
                finishAffinity();
            }
        });

        TypedArray typedArray = getResources().obtainTypedArray(R.array.recycle);
        Random random = new Random();
        int item = random.nextInt(9);
        ImageView lockImage = findViewById(R.id.lockImage);
        lockImage.setImageDrawable(typedArray.getDrawable(item));
        typedArray.recycle();


    }
}
