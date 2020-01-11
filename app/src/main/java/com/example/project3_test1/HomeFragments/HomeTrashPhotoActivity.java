package com.example.project3_test1.HomeFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project3_test1.R;

public class HomeTrashPhotoActivity extends AppCompatActivity  {

    ImageView trashView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_photo);

        trashView = findViewById(R.id.trash);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BitmapImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //Bitmap bitmap = intent.getParcelableExtra("BitmapImage");
        trashView.setImageBitmap(bitmap);

        //byte[] bytes = intent.getByteArrayExtra("BitmapImage");
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //Bitmap bitmap = intent.getParcelableExtra("");


        //trashView.setImageBitmap(bitmap);

        TextView trashName = findViewById(R.id.trashName);
        Button okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setTrashImage(@Nullable Bitmap bitmap) {
        trashView.setImageBitmap(bitmap);
    }
}
