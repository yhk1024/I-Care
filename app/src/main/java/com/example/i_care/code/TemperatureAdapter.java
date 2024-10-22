package com.example.i_care.code;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_care.R;

import java.util.List;


// RecyclerView에 랜덤 체온을 생성하고, 가장 최근 데이터 10개 표시
// RecyclerView 어댑터를 만들어 최근 생성된 데이터를 관리하고 화면에 표시
public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.TemperatureViewHolder> {

    private List<String> temperatureList;

    // 생성자
    public TemperatureAdapter(List<String> temperatureList) {
        this.temperatureList = temperatureList;
    }

    @NonNull
    @Override
    public TemperatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temperature, parent, false);
        return new TemperatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemperatureViewHolder holder, int position) {
        String temperature = temperatureList.get(position);
        holder.temperatureTextView.setText(temperature);
    }

    @Override
    public int getItemCount() {
        return temperatureList.size();
    }

    // RecyclerView의 ViewHolder
    public static class TemperatureViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureTextView;

        public TemperatureViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
        }
    }

    // 새로운 데이터를 상단에 추가하는 메서드
    public void addTemperature(String temperature) {
        // 리스트의 가장 위에 새로운 데이터를 추가
        temperatureList.add(0, temperature);

        // 최대 10개의 데이터만 유지 (오래된 데이터는 삭제)
        if (temperatureList.size() > 10) {
            temperatureList.remove(temperatureList.size() - 1);
        }

        // 데이터가 변경되었음을 어댑터에 알림 (RecyclerView 새로고침)
        notifyDataSetChanged();
    }
}

