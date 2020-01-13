package com.example.project3_test1.HomeFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project3_test1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeTrashPhotoActivity extends AppCompatActivity  {

    ImageView trashView;
    private TextView trashName;
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
        int sendsize = 224;
        trashName = findViewById(R.id.trashName);
        Button okBtn = findViewById(R.id.okBtn);
        Bitmap sendbitmap = Bitmap.createScaledBitmap(bitmap, sendsize, sendsize, true);
        try {
            trashIs(sendbitmap);
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void trashIs(Bitmap bitmap) throws FirebaseMLException, IOException {
        FirebaseApp.initializeApp(HomeTrashPhotoActivity.this);
        final FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("image_classifier.tflite")
                .build();
        FirebaseModelInterpreter firebaseInterpreter = null;
        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(localModel).build();
            firebaseInterpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            // ...
        }
        FirebaseModelInputOutputOptions inputOutputOptions =
                new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3})
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 6})
                        .build();

        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        final ArrayList<String> ans = new ArrayList();
        ans.add(0, "fail");
        int batchNum = 0;
        float[][][][] input = new float[1][224][224][3];
        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = bitmap.getPixel(x, y);
                input[batchNum][x][y][0] = (Color.red(pixel)-127.5f)/127.5f;
                input[batchNum][x][y][1] = (Color.green(pixel)-127.5f)/127.5f;
                input[batchNum][x][y][2] = (Color.blue(pixel)-127.5f)/127.5f;
            }
        }
//        Object[] input = {convertBitmapToByteBuffer(bitmap)};
        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(input)  // add() as many input arrays as your model requires
                .build();
        firebaseInterpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                float[][] output = result.getOutput(0);
                                float[] probabilities = output[0];
                                BufferedReader reader = null;
                                float maxprob=0;
                                String max = null;
                                try {
                                    reader = new BufferedReader(
                                            new InputStreamReader(getAssets().open("image_labels.txt")));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < probabilities.length; i++) {
                                    String label = null;
                                    try {
                                        label = reader.readLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(probabilities[i]>maxprob && (label!="???")){
                                        maxprob= probabilities[i];
                                        max=label;
                                    }
                                    Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));
                                }
                                ans.remove(0);
                                ans.add(0, max);
                                Log.d("result", max);
                                trashName.setText(ans.get(0));

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT);
                                Log.d("output", "fail");
                            }
                        });
    }
}
