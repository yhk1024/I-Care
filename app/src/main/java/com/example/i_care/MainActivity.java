package com.example.i_care;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    // 서버에서 받아온 데이터 보여주기
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<String> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //기기 토큰 확인 하는 함수
        fbToken();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // 리스트에 데이터 추가(과거 오류 데이터)
        recyclerView = findViewById(R.id.recyclerView);

        // 역순으로 데이터를 배치하기 위해 LinearLayoutManager 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);  // 데이터를 역순으로 추가
        layoutManager.setStackFromEnd(true);   // 새 데이터가 상단에 추가됨
        recyclerView.setLayoutManager(layoutManager);

        // 데이터 리스트 초기화 및 어댑터 설정
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);

        // 임의의 데이터 추가 예시
        addData("첫 번째 데이터");
        addData("두 번째 데이터");
        addData("세 번째 데이터");

//        카메라 추가
//        cameraAdd();

        // 메뉴바
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Toast.makeText(MainActivity.this, "홈 메뉴 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "알 수 없는 메뉴 항목입니다.", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    // 데이터 추가 함수
    private void addData(String newData) {
        dataList.add(0, newData);  // 리스트의 가장 위에 데이터 추가
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);  // 추가된 데이터를 보기 위해 상단으로 스크롤
    }

    // 메뉴 아이템 추가
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 화면 이동 함수
    public void moveToPage(View view) {
        int v_id = view.getId();

        if(v_id == R.id.camera_img) {
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


    //카메라 추가
//    public void cameraAdd() {
//        //카메라 목록 추가
//        RecyclerView recyclerView = findViewById(R.id.camera_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        List<Camera_add> data = new ArrayList<>();
//        // 데이터를 추가합니다.
//        for (int i = 0; i < 1; i++) {
//            data.add(new Camera_add(R.drawable.ic_launcher_foreground, "카메라" + (i+1), "활성화" + (i+1)));
//        }
//        //카메라 목록 출력
//        Camera_list adapter = new Camera_list(data);
//        recyclerView.setAdapter(adapter);
//    }
}