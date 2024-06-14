package com.example.i_care;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Sign_edit_check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_edit_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 비밀번호가 맞다면 수정 화면으로 이동.
    public void checkPwd(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, Sign_edit.class);
        startActivity(intent);
    }

    // 취소 버튼을 누르면 뒤로 가기
    public void moveToBack(View view) {
        finish();
    }
}