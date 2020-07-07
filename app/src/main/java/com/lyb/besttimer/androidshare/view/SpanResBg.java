package com.lyb.besttimer.androidshare.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : linyibiao
 * @date : 2020/7/1
 */
public class SpanResBg extends ReplacementSpan {

    private Context context;
    private int resId;
    //左右padding
    private int paddingLR;
    //左右margin
    private int marginLR;

    public SpanResBg(Context context, int resId, int paddingLR, int marginLR) {
        this.context = context;
        this.resId = resId;
        this.paddingLR = paddingLR;
        this.marginLR = marginLR;

    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return (int) paint.measureText(text, start, end) + paddingLR * 2 + marginLR * 2;
    }

    private Bitmap bitmap;

    private Bitmap createBitmap(int resId, Rect bounds) {
        Drawable bg = context.getResources().getDrawable(resId);
        bg.setBounds(bounds);
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(),
                bounds.height(), bg.getOpacity() != PixelFormat.OPAQUE
                        ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas bitmapCanvas = new Canvas(bitmap);
        bg.draw(bitmapCanvas);
        return bitmap;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        float textSize = paint.measureText(text, start, end);
        Rect bounds = new Rect(0, 0, (int) (textSize + paddingLR * 2), (int) (paint.descent() - paint.ascent()));
        if (bitmap == null) {
            bitmap = createBitmap(resId, bounds);
        }
        bounds.offset((int) (x + marginLR), (int) (y + paint.ascent()));
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), bounds, null);
        canvas.drawText(text, start, end, x + marginLR + paddingLR, y, paint);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        super.updateMeasureState(p);
    }
}
