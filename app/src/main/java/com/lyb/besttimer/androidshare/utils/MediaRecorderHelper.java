package com.lyb.besttimer.androidshare.utils;

import android.content.Context;
import android.media.MediaRecorder;

import com.lyb.besttimer.commonutil.utils.FileUtil;

import java.io.File;
import java.io.IOException;

public class MediaRecorderHelper {

    private final Context context;

    private MediaRecorder mediaRecorder;
    private File targetFile;

    public MediaRecorderHelper(Context context) {
        this.context = context;
    }

    public void startRecord() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        } else {
            mediaRecorder.reset();
        }
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            targetFile = FileUtil.getDir(context, "htjy_audio");
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            String audioFileName = "audio_" + System.currentTimeMillis() + ".amr";
            targetFile = new File(targetFile, audioFileName);
            mediaRecorder.setOutputFile(targetFile.getPath());

            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mediaRecorder = null;
        }
    }

}
