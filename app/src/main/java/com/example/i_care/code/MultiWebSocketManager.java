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


// 멀티 포트 연결
public class MultiWebSocketManager {

    private WebSocket webSocket1;
    private WebSocket webSocket2;
    private static final String SERVER_IP = "ws://192.168.0.9:"; // 서버 IP
    private int port1, port2;

    private final Handler handler;

    public MultiWebSocketManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    // 첫 번째 WebSocket 연결 (포트 8765)
    public void connectWebSocket1(TextView temperatureTextView, ImageView videoImageView) {
        port1 = 8765;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_IP + port1).build();
        webSocket1 = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
                Log.d("WebSocket1", "서버 연결 성공 (포트 " + port1 + ")");
            }

            // 체온 정보 처리
            // 서버로부터 받은 메시지 처리 (JSON 형식의 텍스트 메시지)
            // 체온 정보와 영상 정보를 받아온다.
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("WebSocket1", "서버에서 받은 메시지 (포트 " + port1 + "): " + text);

                // 받아온 정보 처리
                msgContent(text, temperatureTextView, videoImageView);
            }

            // 영상 정보 처리
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                // 계속해서 바이너리 데이터를 로그로 출력
                Log.d("WebSocket1", "서버에서 영상 데이터 수신 중 (포트 " + port1 + ")");

                // 받아온 영상 정보 처리
//                videoContent(bytes, videoImageView);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, okhttp3.Response response) {
                // 연결 실패 이유를 로그로 출력
                Log.e("WebSocket1", "연결 실패", t);  // t 객체를 통해 예외 스택 추적을 로그로 남김

                if (response != null) {
                    // 서버로부터 받은 응답이 있는 경우, 상태 코드와 메시지를 로그로 출력
                    Log.e("WebSocket1", "응답 코드: " + response.code());
                    Log.e("WebSocket1", "응답 메시지: " + response.message());
                } else {
                    // 서버 응답이 없으면 요청이 서버에 도달하지 않았을 가능성 (네트워크 문제 등)
                    Log.e("WebSocket1", "서버 응답 없음 (네트워크 문제일 가능성)");
                }

                // 5초 후 재연결 시도
                handler.postDelayed(() -> connectWebSocket1(temperatureTextView, videoImageView), 5000);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d("WebSocket1", "연결 종료: " + reason);
            }
        });
    }

    // 두 번째 WebSocket 연결 (포트 8766)
    public void connectWebSocket2(TextView temperatureTextView, ImageView videoImageView) {
        port2 = 8766;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_IP + port2).build();
        webSocket2 = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
                Log.d("WebSocket2", "서버 연결 성공 (포트 " + port2 + ")");
            }

            // 체온 정보 처리
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("WebSocket2", "서버에서 받은 메시지 (포트 " + port2 + "): " + text);
                // 이곳에서 수신된 데이터를 처리

                // 받아온 체온 정보 처리
                msgContent(text, temperatureTextView, videoImageView);
            }

            // 영상 정보 처리
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                // 계속해서 바이너리 데이터를 로그로 출력
                Log.d("WebSocket2", "서버에서 영상 데이터 수신 중 (포트 " + port2 + ")");

                // 받아온 영상 정보 처리
//                videoContent(bytes, videoImageView);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, okhttp3.Response response) {
                // 연결 실패 이유를 로그로 출력
                Log.e("WebSocket2", "연결 실패", t);  // t 객체를 통해 예외 스택 추적을 로그로 남김

                if (response != null) {
                    // 서버로부터 받은 응답이 있는 경우, 상태 코드와 메시지를 로그로 출력
                    Log.e("WebSocket2", "응답 코드: " + response.code());
                    Log.e("WebSocket2", "응답 메시지: " + response.message());
                } else {
                    // 서버 응답이 없으면 요청이 서버에 도달하지 않았을 가능성 (네트워크 문제 등)
                    Log.e("WebSocket2", "서버 응답 없음 (네트워크 문제일 가능성)");
                }

                // 5초 후 재연결 시도
                handler.postDelayed(() -> connectWebSocket2(temperatureTextView, videoImageView), 5000);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d("WebSocket2", "연결 종료: " + reason);
            }
        });
    }

    // 두 WebSocket 연결 종료
    public void disconnectAll() {
        if (webSocket1 != null) {
            webSocket1.close(1000, "Connection closed");
        }
        if (webSocket2 != null) {
            webSocket2.close(1000, "Connection closed");
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

    // 체온 정보를 받아서 보여주는 함수
    public void msgContent(String text, TextView temperatureTextView, ImageView videoImageView) {
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

                // 영상 데이터 처리 (Base64로 인코딩된 JPG)
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

    // 영상 정보를 받아서 보여주는 함수
    public void videoContent(ByteString bytes, ImageView videoImageView) {
        // 바이너리 데이터를 영상으로 처리 (Bitmap으로 변환)
        // 바이너리 데이터를 Bitmap으로 변환하여 영상 처리
        byte[] data = bytes.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        // 메인 스레드에서 UI 업데이트 (영상 표시)
        handler.post(() -> videoImageView.setImageBitmap(bitmap));
    }

}

