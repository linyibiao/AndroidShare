package com.lyb.besttimer.cameracore;

public interface CameraResultCaller {
    enum ResultType {
        PICTURE, VIDEO
    }

    void onStartVideo();

    void onResult(String fileUrl, ResultType resultType);
}
