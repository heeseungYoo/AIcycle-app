package com.example.project3_test1.PersonFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project3_test1.LoginActivity;
import com.example.project3_test1.MainActivity;
import com.example.project3_test1.R;
import com.example.project3_test1.SaveSharedPreference;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONArray;

public class PersonFragment extends Fragment {

    private View v;
    private Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person, container, false);

        Intent intent = getActivity().getIntent();

        SwitchButton switchButton = v.findViewById(R.id.slideSwitch);

        final TextView userName = v.findViewById(R.id.userName);
        TextView userID = v.findViewById(R.id.userID);
        final TextView userEmail = v.findViewById(R.id.userEmail);
        final String userId = intent.getStringExtra("userID");
        final ImageView imageView = v.findViewById(R.id.imageView);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    String userImg = jsonResponse.getJSONObject(0).getString("userImg");
                    String name = jsonResponse.getJSONObject(0).getString("name");
                    Bitmap bitmap = getBitmapFromString(userImg);
                    imageView.setImageBitmap(bitmap);
                    userName.setText(name);
                    if (jsonResponse.getJSONObject(0).has("email")) {
                        String email = jsonResponse.getJSONObject(0).getString("email");
                        userEmail.setText(email);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(personalInfoRequest);


        //userName.setText(name);
        userID.setText(userId);
        //userEmail.setText(email);


        boolean check = intent.getBooleanExtra("isChecked", false);
        switchButton.setCheckedImmediately(check);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SaveSharedPreference.setSlide(v.getContext(), isChecked);
                if(isChecked) {
                    Intent intent = new Intent(v.getContext(), ScreenService.class);
                    getContext().startService(intent);

                } else  {
                    Intent intent = new Intent(v.getContext(), ScreenService.class);
                    getContext().stopService(intent);

                }
            }
        });

        logoutButton = v.findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserName(getContext());
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return v;
    }

    private Bitmap getBitmapFromString(String string){
        String[] bytevalues = string.substring(1, string.length() -1).split(",");
        byte[] bytes = new byte[bytevalues.length];
        for(int j=0, len=bytes.length; j<len; j++){
            bytes[j] = Byte.parseByte(bytevalues[j].trim());
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}
