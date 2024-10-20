package com.example.i_care;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.i_care.code.ServerConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Camera_item extends AppCompatActivity {

    // WebSocket
    private static final String SERVER_URL = "ws://192.168.0.9:12345";  // WebSocket 서버의 URL
    private WebSocket webSocket;
    private Handler handler = new Handler(Looper.getMainLooper());
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

        //데이터 보여주기
        temperatureTextView = findViewById(R.id.temperatureTextView);
        videoImageView = findViewById(R.id.videoImageView);

        // WebSocket 서버에 연결
//        ServerConnect serverConnect = new ServerConnect();
//        serverConnect.connectWebSocket(temperatureTextView, videoImageView);
        connectWebSocket();

//        TextView showTemperature = findViewById(R.id.showTemperature);
//        VideoView mVideoView = findViewById(R.id.videoView);    // 비디오 뷰 아이디 연결
//
////        Uri uri = Uri.parse("http://3.214.87.90/videos/icare_video.mp4");
//        Uri uri = Uri.parse("http://172.16.42.154");
//
//        Log.d("videoUri : " , String.valueOf(uri));
//
//        // 재생이나 정지와 같은 미디어 제어 버튼부를 담당
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(mVideoView);
//        // 미디어 제어 버튼부 세팅
//        mVideoView.setMediaController(mediaController);
//
//        mVideoView.setVideoURI(uri);    // 미디어 뷰 주소 설정
//        mVideoView.requestFocus();
//        mVideoView.start();
//
//
//        // 일정 시간 지난 후, 아기 체온 변화
//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                //딜레이 후 시작할 코드 작성
//                //아기 체온 변화
//                showTemperature.setText(R.string.tem_2);
//            }
//        }, 6000);// 1초 정도 딜레이를 준 후 시작
    }

    // WebSocket 서버에 연결
    private void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();

        // WebSocket 요청 생성
        Request request = new Request.Builder().url(SERVER_URL).build();

        // WebSocket 연결 및 데이터 수신 설정
        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.d("WebSocket", "서버 연결 성공");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("WebSocket", "서버에서 받은 메시지: " + text);

                // 메시지가 JSON 형식인지 확인
                if (isJSONValid(text)) {
                    try {
                        // JSON 형식일 경우에만 JSONObject로 변환
                        JSONObject jsonObject = new JSONObject(text);
                        String temperature = jsonObject.getString("temperature");

                        // UI 업데이트 (예: 체온 데이터 표시)
                        runOnUiThread(() -> temperatureTextView.setText(temperature));
                    } catch (JSONException e) {
                        Log.e("WebSocket", "JSON 파싱 오류: " + e.getMessage());
                    }
                } else {
                    // JSON이 아닌 일반 텍스트 메시지 처리
                    Log.d("WebSocket", "JSON 형식이 아닌 메시지: " + text);
                }
            }


            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.d("WebSocket", "서버에서 영상 데이터 수신 중");

                // 바이너리 데이터를 영상으로 처리 (Bitmap으로 변환)
                byte[] data = bytes.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // 메인 스레드에서 UI 업데이트 (영상 표시)
//                handler.post(() -> videoImageView.setImageBitmap(bitmap));
                runOnUiThread(() -> videoImageView.setImageBitmap(bitmap));
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.e("WebSocket", "연결 실패: " + t.getMessage());

                // 5초 후 재연결 시도
                handler.postDelayed(() -> connectWebSocket(), 5000);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "연결 종료: " + reason);
            }
        });

        client.dispatcher().executorService().shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webSocket != null) {
            webSocket.close(1000, "Activity paused");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectWebSocket();  // 화면 복귀 시 재연결
    }


    // JSON 형식인지 확인하는 메서드
    private boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);  // 메시지가 JSON 배열일 경우
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}