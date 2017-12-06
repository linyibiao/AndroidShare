package com.lyb.besttimer.androidshare.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;

/**
 * 图像获取器
 *
 * @author linyibiao
 * @since 2017/12/6 16:18
 */
public class ImageSaveGetter implements Html.ImageGetter {

    private Context context;
    private ImageDoneCall imageDoneCall;

    public ImageSaveGetter(Context context, ImageDoneCall imageDoneCall) {
        this.context = context;
        this.imageDoneCall = imageDoneCall;
    }

    private Map<String, Drawable> map = new HashMap<>();

    @Override
    public Drawable getDrawable(final String source) {
        Drawable drawable = map.get(source);
        if (drawable == null) {
            Glide.with(context).load(source).into(new SimpleTarget<Drawable>(/*300,300*/) {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    resource.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                    map.put(source, resource);
                    if (imageDoneCall != null) {
                        imageDoneCall.doneCall();
                    }
                }
            });
        }
        return drawable;
    }

    public interface ImageDoneCall {
        void doneCall();
    }

}
