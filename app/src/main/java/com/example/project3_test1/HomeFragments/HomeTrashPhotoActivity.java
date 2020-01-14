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
    private TextView howToLitter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_photo);

        trashView = findViewById(R.id.trash);
        howToLitter = findViewById(R.id.howToLitter);
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
                                    if((probabilities[i]>maxprob) && (!label.equals("???"))){
                                        maxprob= probabilities[i];
                                        max=label;
                                        Log.d("???", "max"+max);
                                    }
                                    Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));
                                }
                                ans.remove(0);
                                ans.add(0, max);
                                Log.d("result", max);
                                if(max.equals("일반쓰레기")){
                                    howToLitter.setText("일반 종량제 봉투에 담아서 배출, " +
                                            "도자기 및 깨진유리, 태울 수 없는 것은 쓰레기용 봉투(PP봉투)에 담아서 배출");
                                }
                                else if(max.equals("유리")){
                                    howToLitter.setText("병뚜껑을 제거한 후 내용물을 비우고 배출, " +
                                            "깨진 유리는 재활용이 안되므로 신문지에 싸서 종량제 봉투 제출");
                                }
                                else if(max.equals("캔")){
                                    howToLitter.setText("내용물을 비우고 플라스틱 뚜껑이 있는 경우 분리 배출, " +
                                            "부탄가스 용기, 살충제 용기 등은 통풍이 잘 되는 곳에서 구멍을 뚫어 가스를 비운 후 배출");
                                }
                                else if(max.equals("플라스틱")){
                                    howToLitter.setText("다른 재질로 된 뚜껑은 제거 후 내용물을 비우고 배출, " +
                                            "폐스티로폼은 테이프 등 이물질을 제거 후 배출");
                                }
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
