package com.example.i_care.code;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ServerConnect {

    private static final String SERVER_URL = "ws://192.168.0.9:8001";  // WebSocket 서버의 URL
//    private WebSocket webSocket;
    private Handler handler = new Handler(Looper.getMainLooper());

    public void connectWebSocket(TextView temperatureTextView, ImageView videoImageView) {
        OkHttpClient client = new OkHttpClient();

        // WebSocket 요청 생성
        Request request = new Request.Builder().url(SERVER_URL).build();

        // WebSocket 연결 및 데이터 수신 설정
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {

            // WebSocket 연결이 성공하면 호출되며, 연결 성공 로그가 찍힘.
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.d("WebSocket", "서버 연결 성공");
            }

            // 서버로부터 수신된 텍스트 데이터를 처리. 이 예에서는 temperature:36.5와 같은 형식으로 체온 데이터를 수신.
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("WebSocket", "서버에서 받은 메시지: " + text);

                // 서버로부터 받은 데이터가 온도일 경우 처리
                if (text.startsWith("temperature:")) {
                    String temperature = text.split(":")[1];
                    handler.post(() -> temperatureTextView.setText("체온: " + temperature + "°C"));
                }
            }

            // 서버로부터 수신된 바이너리 데이터(영상 프레임)를 처리. Bitmap으로 변환 후 ImageView에 표시.
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.d("WebSocket", "서버에서 영상 데이터 수신 중");

                // 바이너리 데이터를 영상으로 처리 (Bitmap으로 변환)
                byte[] data = bytes.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // 메인 스레드에서 UI 업데이트 (영상 표시)
                // UI 업데이트는 메인 스레드에서 실행해야 하므로, Handler를 사용해 메인 스레드에서 TextView와 ImageView를 업데이트.
                handler.post(() -> videoImageView.setImageBitmap(bitmap));
            }

            // WebSocket 연결 실패 시 로그.
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.e("WebSocket", "연결 실패: " + t.getMessage());
            }

            // WebSocket 연결이 종료되면 호출.
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "연결 종료: " + reason);
            }
        });

        client.dispatcher().executorService().shutdown();
    }

}
