package com.lyb.besttimer.androidshare.activity.media;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.utils.MediaPlayerControlHelper;
import com.lyb.besttimer.androidshare.utils.MediaRecorderHelper;
import com.lyb.besttimer.commonutil.utils.FileUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MediaControlActivity extends AppCompatActivity {

    private MediaPlayerControlHelper mediaPlayerControlHelper;
    private MediaRecorderHelper mediaRecorderHelper;

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_control);
        mediaPlayerControlHelper = new MediaPlayerControlHelper(getSupportFragmentManager());
        mediaRecorderHelper = new MediaRecorderHelper(this);
        findViewById(R.id.tv_play_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = FileUtil.getDir(MediaControlActivity.this, "htjy_audio");
                File[] files = file.listFiles();
                List<String> urls = new ArrayList<>();
                for (File eachFile : files) {
                    urls.add(eachFile.getPath());
                }
//                file = new File(file, "下午 4时50分 .3gpp");
                mediaPlayerControlHelper.getMediaPlayerHelper().stop();
                mediaPlayerControlHelper.getMediaPlayerHelper().play(urls);
            }
        });
        findViewById(R.id.btn_record_audio_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rxPermissions != null) {
                    rxPermissions = new RxPermissions(MediaControlActivity.this);
                }
                if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {
                    mediaRecorderHelper.startRecord();
                } else {
                    Disposable disposable = rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {

                            } else {
                                Toast.makeText(MediaControlActivity.this, "请开启录音相关权限", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                mediaRecorderHelper.startRecord();
            }
        });
        findViewById(R.id.btn_record_audio_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorderHelper.stopRecord();
            }
        });
    }

}
