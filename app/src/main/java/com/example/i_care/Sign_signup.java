package com.example.i_care;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Sign_signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_signup);

        TextView privacyPolicyText = findViewById(R.id.text_privacy_policy);
        privacyPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 개인정보 처리방침 다이얼로그를 표시
                PrivacyPolicyDialog privacyPolicyDialog = new PrivacyPolicyDialog();
                privacyPolicyDialog.show(getSupportFragmentManager(), "privacyPolicyDialog");
            }
        });
    }

    // 회원가입
    public void saveSignUp(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, Sign_login.class);
        startActivity(intent);
    }
}