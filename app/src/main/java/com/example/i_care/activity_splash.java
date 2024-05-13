package com.example.i_care;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_splash extends AppCompatActivity {

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
                Intent intent = new Intent(activity_splash.this, MainActivity.class);
                startActivity(intent);
                finish(); // 스플래시 액티비티 종료
            }
        }, SPLASH_DISPLAY_TIME);
    }
}