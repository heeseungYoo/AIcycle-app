package com.example.project3_test1.PersonFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project3_test1.LoginActivity;
import com.example.project3_test1.R;
import com.example.project3_test1.SaveSharedPreference;
import com.kyleduo.switchbutton.SwitchButton;

public class PersonFragment extends Fragment {

    private View v;
    private Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person, container, false);

        Intent intent = getActivity().getIntent();

        String userId = intent.getStringExtra("userID");

        SwitchButton switchButton = v.findViewById(R.id.slideSwitch);

        TextView userName = v.findViewById(R.id.userName);
        TextView userID = v.findViewById(R.id.userID);
        TextView userEmail = v.findViewById(R.id.userEmail);

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

}
