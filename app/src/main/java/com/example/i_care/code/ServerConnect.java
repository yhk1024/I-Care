package com.example.i_care.code;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ServerConnect {

    public void connectWebSocket(String SERVER_URL, WebSocket webSocket, Handler handler, TextView temperatureTextView, ImageView imageView) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_URL).build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.d("WebSocket", "서버 연결 성공");  // 서버 연결 성공 로그
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 수신된 메시지가 텍스트(체온 데이터)인 경우
                if (text.startsWith("temperature:")) {
                    String temperature = text.split(":")[1];
                    handler.post(() -> temperatureTextView.setText("체온: " + temperature + "°C"));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 수신된 메시지가 바이너리(영상 데이터)인 경우
                byte[] data = bytes.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // 메인 스레드에서 UI 업데이트
                handler.post(() -> imageView.setImageBitmap(bitmap));
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.e("WebSocket", "연결 실패: " + t.getMessage());
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "연결 종료: " + reason);
            }
        });

        client.dispatcher().executorService().shutdown();
    }
}
