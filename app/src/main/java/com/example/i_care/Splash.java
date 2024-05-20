package com.example.i_care;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    // 스플래시 화면 표시 시간 (밀리초)
    private static final int SPLASH_DISPLAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // SPLASH_DISPLAY_TIME 이후에 메인 액티비티로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Sign_login.class);
                startActivity(intent);
                finish(); // 스플래시 액티비티 종료
            }
        }, SPLASH_DISPLAY_TIME);
    }
}