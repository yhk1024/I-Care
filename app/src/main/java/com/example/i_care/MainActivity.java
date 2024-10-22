package com.example.i_care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_care.code.TemperatureAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    // 5초마다 랜덤 체온 데이터를 생성하고, RecyclerView에 추가
    private RecyclerView recyclerView;
    private TemperatureAdapter adapter;
    private SharedPreferences sharedPreferences;
    private List<String> temperatureHistory;
    private static final String PREFS_NAME = "TempPrefs";
    private static final String KEY_HISTORY = "temperature_history";
    private Random random;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //기기 토큰 확인 하는 함수
        fbToken();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar를 ActionBar로 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // 리스트에 데이터 추가(과거 오류 데이터)
        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // 과거 데이터 불러오기
        temperatureHistory = loadTemperatureHistory();

        // RecyclerView Adapter 초기화
        adapter = new TemperatureAdapter(temperatureHistory);
        recyclerView.setAdapter(adapter);

        // 랜덤 체온 생성을 위한 Random 객체
        random = new Random();

        // 5초마다 랜덤 체온 생성 및 추가
        startTemperatureGeneration();
    }


    // 5초마다 랜덤 체온 생성
    private void startTemperatureGeneration() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateRandomTemperature();
                // 5초 후 다시 실행
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    // 랜덤 체온 생성 및 RecyclerView에 추가
    private void generateRandomTemperature() {
        double randomTemperature = 36.0 + (random.nextDouble() * 2.0); // 36.0°C ~ 38.0°C
        String temperatureString = String.format("%.2f°C", randomTemperature);

        // RecyclerView에 추가
        adapter.addTemperature(temperatureString);

        // 생성된 체온을 저장
        saveTemperature(temperatureString);
    }

    // 과거 데이터를 SharedPreferences에서 불러오는 메서드
    private List<String> loadTemperatureHistory() {
        String savedHistory = sharedPreferences.getString(KEY_HISTORY, "");
        List<String> historyList = new ArrayList<>();

        if (!savedHistory.isEmpty()) {
            String[] savedTemps = savedHistory.split(",");
            for (String temp : savedTemps) {
                historyList.add(temp);
            }
        }

        // 최대 10개의 데이터만 유지
        while (historyList.size() > 10) {
            historyList.remove(0);
        }

        return historyList;
    }

    // 생성된 체온 데이터를 SharedPreferences에 저장하는 메서드
    private void saveTemperature(String temperature) {
        temperatureHistory.add(0, temperature);

        // 최대 10개의 데이터만 유지
        if (temperatureHistory.size() > 10) {
            temperatureHistory.remove(temperatureHistory.size() - 1);
        }

        // 데이터를 SharedPreferences에 저장
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HISTORY, String.join(",", temperatureHistory));
        editor.apply();
    }


    // 화면 이동 함수
    public void moveToPage(View view) {
        int v_id = view.getId();

        if(v_id == R.id.btn_camera) {
            // 클릭한 버튼이 카메라 버튼일 경우, 카메라 영상 확인 화면으로 이동
            Intent intent = new Intent(this, Camera_item.class);
            startActivity(intent);
        } else if(v_id == R.id.btn_sign_edit) {
            // 클릭한 버튼이 회원 수정 버튼일 경우, 회원 인증 화면으로 이동
            Intent intent = new Intent(this, Sign_edit_check.class);
            startActivity(intent);
        }
    }


    //기기 토큰 확인 하는 함수
    public void fbToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MyFirebaseMsgService: ", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "Refreshed token: " + token;
                        Log.d("MyFirebaseMsgService: ", msg);
                    }
                });
    }
}