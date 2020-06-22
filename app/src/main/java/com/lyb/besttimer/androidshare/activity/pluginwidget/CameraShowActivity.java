package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.cameracore.CameraConstants;
import com.lyb.besttimer.cameracore.CameraMode;
import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.activity.CameraMixActivity;

public class CameraShowActivity extends AppCompatActivity {

    private final int cameraMixCode = 10086;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_show);
        startActivityForResult(new Intent(this, CameraMixActivity.class).putExtras(CameraMixActivity.getBundle(20 * 1000, CameraMode.ALL)), cameraMixCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case cameraMixCode:
                    String fileUrl = data.getStringExtra(CameraConstants.fileUrl);//资源本地链接
                    CameraResultCaller.ResultType resultType = (CameraResultCaller.ResultType) data.getSerializableExtra(CameraConstants.resultType);
                    if (resultType == CameraResultCaller.ResultType.PICTURE) {
                        // TODO: 2019/4/18 图片处理
                    } else if (resultType == CameraResultCaller.ResultType.VIDEO) {
                        // TODO: 2019/4/18 视频处理
                    }
                    break;
            }
        }
    }
}
