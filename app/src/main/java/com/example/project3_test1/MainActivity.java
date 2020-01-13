package com.example.project3_test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project3_test1.HomeFragments.HomeFragment;
import com.example.project3_test1.PersonFragments.PersonFragment;
import com.example.project3_test1.RecycleFragments.RecycleFragment;
import com.example.project3_test1.ShopFragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private RecycleFragment recycleFragment = new RecycleFragment();
    private ShopFragment shopFragment = new ShopFragment();
    private PersonFragment personFragment = new PersonFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String name = intent.getStringExtra("name");
        String userImg = intent.getStringExtra("userImg");
        Toast.makeText(this, userID + " 로그인 하셨습니다.", Toast.LENGTH_SHORT).show();
        PersonFragment fragment = new PersonFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId())
            {
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.recycleItem:
                    transaction.replace(R.id.frameLayout, recycleFragment).commitAllowingStateLoss();
                    break;
                case R.id.shoppingItem:
                    transaction.replace(R.id.frameLayout, shopFragment).commitAllowingStateLoss();
                    break;
                case R.id.personItem:
                    transaction.replace(R.id.frameLayout, personFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }

}
