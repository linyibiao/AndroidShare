package com.lyb.besttimer.androidshare.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MediaPlayerHelper {

    private MediaPlayer mediaPlayer;

    public boolean isPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public void stop() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(List<String> urls) {
        if (urls.size() == 0) {
            return;
        }
        onDestroy();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                urls.remove(0);
                if (urls.size() > 0) {
                    play(urls);
                }
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.reset();
                return false;
            }
        });
        try {
            mediaPlayer.setDataSource(urls.get(0));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException("读取文件异常：" + e.getMessage());
        }
    }

    public void play(String url) {
        play(Collections.singletonList(url));
    }

    public void onDestroy() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mediaPlayer = null;
        }
    }

}
