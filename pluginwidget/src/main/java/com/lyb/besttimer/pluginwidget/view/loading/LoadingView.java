package com.lyb.besttimer.pluginwidget.view.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class LoadingView extends View {
    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public enum LoadingMode {
        IDLE, LOADING
    }

    private LoadingMode loadingMode = LoadingMode.IDLE;
    private LoadingTime loadingTime;

    private LoadingCaller loadingCaller;

    private Paint paint;
    private RectF rectF = new RectF();

    public LoadingMode getLoadingMode() {
        return loadingMode;
    }

    private long millisInFuture = 10 * 1000;
    private boolean canLoad = true;

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public void setCanLoad(boolean canLoad) {
        this.canLoad = canLoad;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff3cb264);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingMode == LoadingMode.IDLE) {
                    if (loadingCaller != null) {
                        loadingCaller.takeOneShot();
                    }
                }
            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (loadingMode == LoadingMode.IDLE) {
                    if (canLoad) {
                        startLoading();
                    }
                }
                return true;
            }
        });

    }

    private void showPressAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(100);
        startAnimation(scaleAnimation);
    }

    private void showReleaseAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1, 0.8f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(100);
        startAnimation(scaleAnimation);
    }

    public void setLoadingCaller(LoadingCaller loadingCaller) {
        this.loadingCaller = loadingCaller;
    }

    private void startLoading() {
        if (loadingMode != LoadingMode.LOADING) {
            loadingMode = LoadingMode.LOADING;
            if (loadingTime != null) {
                loadingTime.cancel();
            }
            loadingTime = new LoadingTime();
            loadingTime.start();
            if (loadingCaller != null) {
                loadingCaller.startLoading();
            }
        }
    }

    public void reStartLoading() {
        loadingMode = LoadingMode.LOADING;
        if (loadingTime != null) {
            loadingTime.cancel();
        }
        loadingTime = new LoadingTime();
        loadingTime.start();
    }

    private void endLoading() {
        if (loadingMode == LoadingMode.LOADING) {
            loadingMode = LoadingMode.IDLE;
            loadingTime.cancel();
            postInvalidate();
            if (loadingCaller != null) {
                loadingCaller.endLoading();
            }
        }
    }

    private class LoadingTime extends CountDownTimer {

        private static final long countDownInterval = 10 * 1000 / 360;
        private float percent = 0;

        public float getPercent() {
            return percent;
        }

        public LoadingTime() {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            percent = (millisInFuture - millisUntilFinished) * 1.0f / millisInFuture;
            postInvalidate();
        }

        @Override
        public void onFinish() {
            percent = 1;
            loadingMode = LoadingMode.IDLE;
            postInvalidate();
            if (loadingCaller != null) {
                loadingCaller.endLoading();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int minValue = Math.min(maxWidth, maxHeight);
        super.onMeasure(MeasureSpec.makeMeasureSpec(minValue, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(minValue, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(0x77ffffff);
        paint.setStyle(Paint.Style.FILL);
        if (loadingMode == LoadingMode.LOADING) {
            float outSize = getWidth();
            float radius = outSize / 2f;
            rectF.set(outSize / 2 - radius, outSize / 2 - radius, outSize / 2 + radius, outSize / 2 + radius);
            canvas.drawCircle(outSize / 2, outSize / 2, radius, paint);
        } else {
            float outSize = getWidth();
            float radius = outSize / 2f * 2 / 3;
            rectF.set(outSize / 2 - radius, outSize / 2 - radius, outSize / 2 + radius, outSize / 2 + radius);
            canvas.drawCircle(outSize / 2, outSize / 2, radius, paint);
        }

        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        if (loadingMode == LoadingMode.LOADING) {
            float outSize = getWidth();
            float radius = outSize / 2f * 5 / 12;
            rectF.set(outSize / 2 - radius, outSize / 2 - radius, outSize / 2 + radius, outSize / 2 + radius);
            canvas.drawCircle(outSize / 2, outSize / 2, radius, paint);
        } else {
            float outSize = getWidth();
            float radius = outSize / 2f * 1 / 2;
            rectF.set(outSize / 2 - radius, outSize / 2 - radius, outSize / 2 + radius, outSize / 2 + radius);
            canvas.drawCircle(outSize / 2, outSize / 2, radius, paint);
        }

        if (loadingMode == LoadingMode.LOADING) {
            paint.setColor(0xff3cb264);
            paint.setStrokeWidth(20);
            paint.setStyle(Paint.Style.STROKE);
            float padding = paint.getStrokeWidth() / 2;
            rectF.set(0 + padding, 0 + padding, getWidth() - padding, getHeight() - padding);
            canvas.drawArc(rectF, -90, loadingTime.getPercent() * 360, false, paint);
        }

    }

    private float initY;

    private void initTouch(MotionEvent event) {
        initY = event.getY(0);
        if (loadingCaller != null) {
            loadingCaller.moveInit();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                showPressAnimation();
                initTouch(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                initTouch(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                initTouch(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (loadingMode != LoadingMode.LOADING) {
                    initTouch(event);
                } else {
                    float currY = event.getY(0);
                    float dy = Math.max(initY - currY, 0);
                    if (loadingCaller != null) {
                        loadingCaller.moveOffset(dy / 200);
                        loadingCaller.moveOffset((int) (dy / 50));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showReleaseAnimation();
                if (loadingMode == LoadingMode.LOADING) {
                    endLoading();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
