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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.URL;
import java.util.Date;

public class Camera_item extends AppCompatActivity {

    private static final String COGNITO_POOL_ID = "YOUR_COGNITO_POOL_ID";
    private static final String BUCKET_NAME = "YOUR_BUCKET_NAME";
    private static final String VIDEO_FILE_KEY = "path/to/your/video.mp4";  // S3에 저장된 파일 경로
    private static final Regions REGION = Regions.YOUR_AWS_REGION; // 예: Regions.US_EAST_1

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

        AWSVideo(); // AWS 동영상 출력 함수

//        ViewVideo();
    }


    // AWS 동영상 출력
    void AWSVideo() {
        VideoView videoView = findViewById(R.id.videoView);

        // AWS Cognito 인증 공급자 설정
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                COGNITO_POOL_ID,
                REGION
        );

        // S3 클라이언트 생성
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);

        // S3에서 동영상 URL 가져오기
        URL videoUrl = generatePresignedUrl(s3Client);

        // 동영상 URL을 VideoView에 설정
        videoView.setVideoURI(Uri.parse(videoUrl.toString()));

        // 미디어 컨트롤러(재생, 일시정지 등) 추가
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // 동영상 재생
        videoView.start();
    }

    // Pre-signed URL 생성
    private URL generatePresignedUrl(AmazonS3Client s3Client) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 10; // 10분 유효 시간
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, VIDEO_FILE_KEY)
                        .withMethod(com.amazonaws.HttpMethod.GET)
                        .withExpiration(expiration);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }



    // 로컬 내의 동영상 출력 코드
    void ViewVideo() {
        TextView showTemperature = findViewById(R.id.showTemperature);
        VideoView mVideoView = findViewById(R.id.videoView);    // 비디오 뷰 아이디 연결

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.icare_video);

        Log.d("videoUri : " , String.valueOf(uri));

        // 재생이나 정지와 같은 미디어 제어 버튼부를 담당
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController); // 미디어 제어 버튼부 세팅

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