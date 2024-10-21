package com.example.i_care.code;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {

    private WebSocket webSocket;
    private static final String SERVER_URL = "ws://192.168.0.9:8765";  // 서버 IP 및 포트 번호
    private final Handler handler;

    // 생성자
    public WebSocketManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    // WebSocket 서버에 연결
    public void connectWebSocket(TextView temperatureTextView, ImageView videoImageView) {
        OkHttpClient client = new OkHttpClient();

        // WebSocket 요청 생성
        Request request = new Request.Builder().url(SERVER_URL).build();

        // WebSocket 연결 및 데이터 수신 설정
        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
                Log.d("WebSocket", "서버 연결 성공");
            }

            // 서버로부터 받은 메시지 처리 (JSON 형식의 텍스트 메시지)
            // 체온 정보와 영상 정보를 받아온다.
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("WebSocket", "서버에서 받은 메시지: " + text);

                // 메시지가 JSON 형식인지 확인
                if (isJSONValid(text)) {
                    try {
                        JSONObject jsonObject = new JSONObject(text);

                        // 체온 정보 처리
                        if (jsonObject.has("value")) {
                            String temperature = jsonObject.getString("value");
                            String tem_msg = "체온: " + temperature + "°C";
                            handler.post(() -> temperatureTextView.setText(tem_msg));
                        }

                        // 영상 데이터 처리 (Base64로 인코딩된 영상)
                        if (jsonObject.has("video")) {
                            String base64Video = jsonObject.getString("video");

                            // Base64 문자열을 디코딩하여 Bitmap으로 변환
                            byte[] decodedBytes = Base64.decode(base64Video, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                            // UI 업데이트 (영상 표시)
                            handler.post(() -> videoImageView.setImageBitmap(bitmap));
                        }

                    } catch (JSONException e) {
                        Log.e("WebSocket", "JSON 파싱 오류: " + e.getMessage());
                    }
                } else {
                    // JSON 형식이 아닌 메시지 처리
                    Log.d("WebSocket", "JSON 형식이 아닌 메시지: " + text);
                }
            }

            // 영상 정보 처리
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                // 계속해서 바이너리 데이터를 로그로 출력
                Log.d("WebSocket", "서버에서 영상 데이터 수신 중");
                Log.d("WebSocket", "서버에서 받은 바이너리 메시지 (길이): " + bytes.size());

                // 바이너리 데이터를 Base64로 인코딩하여 로그에 출력 (크기가 크지 않을 때)
                String base64Data = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                Log.d("WebSocket", "Base64로 인코딩된 바이너리 데이터: " + base64Data);

                // 바이너리 데이터를 영상으로 처리 (Bitmap으로 변환)
                // 바이너리 데이터를 Bitmap으로 변환하여 영상 처리
                byte[] data = bytes.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // 메인 스레드에서 UI 업데이트 (영상 표시)
                handler.post(() -> videoImageView.setImageBitmap(bitmap));
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, okhttp3.Response response) {
                // 연결 실패 이유를 로그로 출력
                Log.e("WebSocket", "연결 실패", t);  // t 객체를 통해 예외 스택 추적을 로그로 남김

                if (response != null) {
                    // 서버로부터 받은 응답이 있는 경우, 상태 코드와 메시지를 로그로 출력
                    Log.e("WebSocket", "응답 코드: " + response.code());
                    Log.e("WebSocket", "응답 메시지: " + response.message());
                } else {
                    // 서버 응답이 없으면 요청이 서버에 도달하지 않았을 가능성 (네트워크 문제 등)
                    Log.e("WebSocket", "서버 응답 없음 (네트워크 문제일 가능성)");
                }

                // 5초 후 재연결 시도
                handler.postDelayed(() -> connectWebSocket(temperatureTextView, videoImageView), 5000);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d("WebSocket", "연결 종료: " + reason);
            }
        });

        // WebSocket이 작업을 완료할 때 호출하여 자원을 해제
        client.dispatcher().executorService().shutdown();
    }


    // WebSocket 연결 해제
    public void disconnectWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Connection closed");
        }
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
