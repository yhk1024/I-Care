package com.example.i_care;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.camera_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CameraItem> data = new ArrayList<>();
        // 데이터를 추가합니다.
        for (int i = 0; i < 3; i++) {
            data.add(new CameraItem(R.drawable.ic_launcher_foreground, "카메라" + (i+1), "활성화" + (i+1)));
        }

        Camera_list adapter = new Camera_list(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {  // 상수 값 사용
            // 설정 버튼 클릭 시 동작

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}