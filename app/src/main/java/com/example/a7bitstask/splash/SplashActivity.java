package com.example.a7bitstask.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a7bitstask.R;
import com.example.a7bitstask.home.MainActivity;
import com.example.a7bitstask.otp.MobileNumberGetActivity;
import com.example.a7bitstask.user.UserDataSession;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_white);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        UserDataSession userDataSession = new UserDataSession(getApplicationContext());

        new Handler().postDelayed(() -> {

            String name = userDataSession.getUSerValue("name");
            Log.v("tag", "UserName" + name);

            if (name.length() != 0) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
                SplashActivity.this.finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, MobileNumberGetActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }


        }, 2000);
    }
}