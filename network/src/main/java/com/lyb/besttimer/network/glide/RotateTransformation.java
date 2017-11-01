package com.lyb.besttimer.network.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * glide rotate
 * Created by linyibiao on 2017/2/8.
 */

public class RotateTransformation extends BitmapTransformation {

    private static final String ID = "com.lyb.besttimer.network.glide.RotateTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);

    private int degreesToRotate;

    public RotateTransformation(int degreesToRotate) {
        this.degreesToRotate = degreesToRotate;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.rotateImage(toTransform, degreesToRotate);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RotateTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

}
