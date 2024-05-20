package com.example.i_care;

import android.os.Bundle;

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
}