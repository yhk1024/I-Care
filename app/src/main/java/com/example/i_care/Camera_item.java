package com.example.i_care;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.i_care.code.MultiWebSocketManager;
import com.example.i_care.code.WebSocketManager;


public class Camera_item extends AppCompatActivity {

    // WebSocket
    private WebSocketManager webSocketManager;
    private TextView temperatureTextView;
    private ImageView videoImageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.camera_item);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // WebSocket으로 받을 데이터를 보여줄 영역
        temperatureTextView = findViewById(R.id.temperatureTextView);
        videoImageView = findViewById(R.id.videoImageView);

        // WebSocketManager 초기화
        webSocketManager = new WebSocketManager();
    }


    // 화면에 다시 들어왔을 때 WebSocket 연결
    @Override
    protected void onResume() {
        super.onResume();
        webSocketManager.connectWebSocket(temperatureTextView, videoImageView);
    }
}