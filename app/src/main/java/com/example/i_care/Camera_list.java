package com.example.i_care;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Camera_list extends RecyclerView.Adapter<Camera_list.ViewHolder> {

    private final List<Camera_add> mData;

    public Camera_list(List<Camera_add> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Camera_add item = mData.get(position);
        holder.camera_img.setImageResource(item.getCamImg());
        holder.camera_name.setText(item.getCamName());
        holder.camera_status.setText(item.getCamStatus());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView camera_img;
        TextView camera_name;
        TextView camera_status;

        // 여기에 버튼 클릭 이벤트 추가
        ViewHolder(View itemView) {
            super(itemView);
            camera_img = itemView.findViewById(R.id.camera_img);
            camera_name = itemView.findViewById(R.id.camera_name);
            camera_status = itemView.findViewById(R.id.camera_status);

            // 회원가입 페이지로 이동
            camera_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 회원가입 화면으로 이동
                    Intent intent = new Intent(itemView.getContext(), Camera_item.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}