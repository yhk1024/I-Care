package com.example.i_care;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Camera_item extends AppCompatActivity {

//    TextView showTemperature = findViewById(R.id.showTemperature);

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


        VideoView mVideoView = (VideoView) findViewById(R.id.videoView);    // 비디오 뷰 아이디 연결

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.icare_video);

        // 재생이나 정지와 같은 미디어 제어 버튼부를 담당
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController); // 미디어 제어 버튼부 세팅

        mVideoView.setVideoURI(uri);    // 미디어 뷰 주소 설정
        mVideoView.requestFocus();
        mVideoView.start();

//        mVideoView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mVideoView.isPlaying()) {
//                    mVideoView.pause();
//                    return false;
//                } else {
//                    mVideoView.start();
//                    return false;
//                }
//            }
//        });


//        // 일정 시간 지난 후, 아기 체온 변화
//        // 1초 뒤 뒤로 뒤로 가기
//        new Handler().postDelayed(() -> {
//            //딜레이 후 시작할 코드 작성
//            //아기 체온 변화
//            showTemperature.setText(R.string.tem_2);
//        }, 6000);//6초 정도 딜레이를 준 후 시작
    }
}