package com.lyb.besttimer.androidshare.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/1.
 */
public class PorterDuffView extends View {

    public PorterDuffView(Context context) {
        this(context, null);
    }

    public PorterDuffView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorterDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private String title = "CLEAR";
    private PorterDuff.Mode mode = PorterDuff.Mode.CLEAR;

    private float msgHeightFactor = 0.25f;

    public void setMode(String title, PorterDuff.Mode mode) {
        this.title = title;
        this.mode = mode;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void init() {
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize_width = MeasureSpec.getSize(widthMeasureSpec);
        int specSize_height = MeasureSpec.getSize(heightMeasureSpec);
        if (specSize_width != 0 || specSize_height != 0) {
            int specSize = Math.min(specSize_width / 2, specSize_height);
            if (specSize_width == 0) {
                specSize = specSize_height;
            } else if (specSize_height == 0) {
                specSize = specSize_width / 2;
            }
            super.onMeasure(MeasureSpec.makeMeasureSpec(specSize * 2, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (specSize / (1 - msgHeightFactor)), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        int view_translate_dy = (int) (canvasHeight * msgHeightFactor);

        drawMsg(canvas, canvasWidth, view_translate_dy);
        drawView(canvas, canvasWidth, canvasHeight - view_translate_dy, 0, view_translate_dy);

    }

    private void drawMsg(Canvas canvas, int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30);
        canvas.drawText("simple", width / 4, height * 3 / 4, paint);
        canvas.drawText(title, width / 2, height * 3 / 4, paint);
        canvas.drawText("bitmap", width * 3 / 4, height * 3 / 4, paint);
    }

    private void drawView(Canvas canvas, int width, int height, int translate_dx, int translate_dy) {

        int layerId = canvas.saveLayer(translate_dx, translate_dy, width + translate_dx, height + translate_dy, null, Canvas.ALL_SAVE_FLAG);
        canvas.translate(translate_dx, translate_dy);

        drawEdge(canvas, width, height);
        drawBackground(canvas, width, height);
        int pad = 20;
        drawPorterDuff(canvas, width / 2 - pad * 2, height - pad * 2, pad, pad, false);
        drawPorterDuff(canvas, width / 2 - pad * 2, height - pad * 2, width / 2 + pad, pad, true);

        canvas.restoreToCount(layerId);

    }

    private void drawEdge(Canvas canvas, int width, int height) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(0.5f, 0.5f, width - 0.5f, height - 0.5f, paint);

    }

    private void drawBackground(Canvas canvas, int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(new int[]{0xFFFFFFFF, 0xFFCCCCCC, 0xFFCCCCCC, 0xFFFFFFFF}, 2, 2, Bitmap.Config.ARGB_8888);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Matrix matrix = new Matrix();
        matrix.setScale(6, 6);
        bitmapShader.setLocalMatrix(matrix);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(bitmapShader);
        canvas.drawRect(0.5f, 0.5f, width - 0.5f, height - 0.5f, paint);

    }

    private void drawPorterDuff(Canvas canvas, int width, int height, int translate_dx, int translate_dy, boolean isBitmap) {

        int layerId = canvas.saveLayer(translate_dx, translate_dy, width + translate_dx, height + translate_dy, null, Canvas.ALL_SAVE_FLAG);
        canvas.translate(translate_dx, translate_dy);

        drawEdge(canvas, width, height);

        int pad = 5;
        drawCircle(canvas, 0xFFFFCC44, width, pad, pad, false, isBitmap);
        drawCircle(canvas, 0xFF66AAFF, width, width / 4 - pad, width / 4 - pad, true, isBitmap);

        canvas.restoreToCount(layerId);

    }

    private void drawCircle(Canvas canvas, int color, int d, int padLeft, int padTop, boolean showMode, boolean isBitmap) {
        if (isBitmap) {
            Bitmap bitmap = Bitmap.createBitmap(d * 3 / 4, d * 3 / 4, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(bitmap);
            drawCircle(bitmapCanvas, color, d, 0, 0, false, false);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if (showMode) {
                paint.setXfermode(new PorterDuffXfermode(mode));
            }
            canvas.drawBitmap(bitmap, padLeft, padTop, paint);
        } else {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
            if (showMode) {
                paint.setXfermode(new PorterDuffXfermode(mode));
            }
            canvas.drawCircle(d * 3 / 8 + padLeft, d * 3 / 8 + padTop, d * 3 / 8, paint);
        }
    }

}
