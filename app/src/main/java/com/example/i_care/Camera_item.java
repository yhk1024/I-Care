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

    private MultiWebSocketManager multiWebSocketManager;

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

        // MultiWebSocketManager 초기화
        multiWebSocketManager = new MultiWebSocketManager();
    }


    // 화면에 다시 들어왔을 때 WebSocket 연결
    @Override
    protected void onResume() {
        super.onResume();

//        webSocketManager.connectWebSocket(temperatureTextView, videoImageView);

        // 각각의 포트에 대해 WebSocket 연결 시작
        multiWebSocketManager.connectWebSocket1(temperatureTextView, videoImageView); // 포트 8765
        multiWebSocketManager.connectWebSocket2(temperatureTextView, videoImageView); // 포트 8766
    }

    // 화면을 떠날 때 WebSocket 연결 해제
    @Override
    protected void onPause() {
        super.onPause();

//        webSocketManager.disconnectWebSocket();

        multiWebSocketManager.disconnectAll();
    }

    // 사용자가 액티비티를 완전히 떠나거나, 앱이 종료될 때 호출
    @Override
    protected void onDestroy() {
        super.onDestroy();

//        // WebSocket 연결 해제
//        webSocketManager.disconnectWebSocket();

        // 모든 WebSocket 연결 해제
        multiWebSocketManager.disconnectAll();
    }
}