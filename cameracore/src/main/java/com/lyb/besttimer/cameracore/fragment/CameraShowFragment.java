package com.lyb.besttimer.cameracore.fragment;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;

public class CameraShowFragment extends Fragment {

    public static Bundle createArg(String fileUrl, CameraResultCaller.ResultType resultType) {
        Bundle bundle = new Bundle();
        bundle.putString("fileUrl", fileUrl);
        bundle.putSerializable("resultType", resultType);
        return bundle;
    }

    private VideoView vvVideo;
    private ImageView ivPic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_show, container, false);
        vvVideo = view.findViewById(R.id.vv_video);
        ivPic = view.findViewById(R.id.iv_pic);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        handle();
    }

    @Override
    public void onResume() {
        super.onResume();
        vvVideo.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        vvVideo.pause();
    }

    private void handle() {
        final String fileUrl = getArguments().getString("fileUrl");
        CameraResultCaller.ResultType resultType = (CameraResultCaller.ResultType) getArguments().getSerializable("resultType");
        if (resultType == CameraResultCaller.ResultType.PICTURE) {
            ivPic.setVisibility(View.VISIBLE);
            vvVideo.setVisibility(View.GONE);
            Glide.with(this).load(fileUrl).into(ivPic);
        } else if (resultType == CameraResultCaller.ResultType.VIDEO) {
            ivPic.setVisibility(View.GONE);
            vvVideo.setVisibility(View.VISIBLE);
            vvVideo.setVideoPath(fileUrl);
            vvVideo.start();
            vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
            vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    vvVideo.setVideoPath(fileUrl);
                    vvVideo.start();
                }
            });
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
