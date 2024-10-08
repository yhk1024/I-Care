package com.example.i_care;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Camera_item extends AppCompatActivity {

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

        TextView showTemperature = findViewById(R.id.showTemperature);
        VideoView mVideoView = findViewById(R.id.videoView);    // 비디오 뷰 아이디 연결

        Uri uri = Uri.parse("http://3.214.87.90/videos/icare_video.mp4");

        Log.d("videoUri : " , String.valueOf(uri));

        // 재생이나 정지와 같은 미디어 제어 버튼부를 담당
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        // 미디어 제어 버튼부 세팅
        mVideoView.setMediaController(mediaController);

        mVideoView.setVideoURI(uri);    // 미디어 뷰 주소 설정
        mVideoView.requestFocus();
        mVideoView.start();


        // 일정 시간 지난 후, 아기 체온 변화
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //딜레이 후 시작할 코드 작성
                //아기 체온 변화
                showTemperature.setText(R.string.tem_2);
            }
        }, 6000);// 1초 정도 딜레이를 준 후 시작
    }
}