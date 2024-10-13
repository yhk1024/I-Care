package com.example.i_care;


// RecyclerView 리스트 항목에 들어갈 데이터를 담는 클래스
public class Camera_add {
    private final int camera_img;
    private final String camera_name;
    private final String camera_status;

    public Camera_add(int imageResId, String text1, String text2) {
        this.camera_img = imageResId;
        this.camera_name = text1;
        this.camera_status = text2;
    }

    // 카메라 이미지 데이터
    public int getCamImg() {
        return camera_img;
    }

    // 카메라 이름 데이터
    public String getCamName() {
        return camera_name;
    }

    // 카메라 활성화 상태 데이터
    public String getCamStatus() {
        return camera_status;
    }
}
