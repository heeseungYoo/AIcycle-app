package com.example.project3_test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project3_test1.PersonFragments.ScreenService;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SaveSharedPreference.getUserID(FirstAuthActivity.this).length() == 0) {
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            intent = new Intent(FirstAuthActivity.this, MainActivity.class);
            intent.putExtra("userID", SaveSharedPreference.getUserID(this));
            intent.putExtra("isChecked", SaveSharedPreference.getSlide(this));

            if(SaveSharedPreference.getSlide(this)) {
                Intent intent1 = new Intent(FirstAuthActivity.this, ScreenService.class);
                startService(intent1);
            } else {
                Intent intent1 = new Intent(FirstAuthActivity.this, ScreenService.class);
                stopService(intent1);
            }
            startActivity(intent);
            this.finish();
        }
    }

}
