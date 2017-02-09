package com.lyb.besttimer.network.glide;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

/**
 * glide rotate
 * Created by linyibiao on 2017/2/8.
 */

public class RotateTransformation extends BitmapTransformation {

    private int degreesToRotate;

    public RotateTransformation(Context context, int degreesToRotate) {
        super(context);
        this.degreesToRotate = degreesToRotate;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.rotateImage(toTransform, degreesToRotate);
    }

    @Override
    public String getId() {
        return "RotateTransformation.com.cailele.android.thirdUtils.glide";
    }

}
