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
import android.widget.Toast;

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

    enum LoadingMode {
        IDLE, LOADING
    }

    private LoadingMode loadingMode = LoadingMode.IDLE;
    private LoadingTime loadingTime;

    private LoadingCaller loadingCaller;

    private Paint paint;
    private RectF rectF;

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffff0000);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingMode == LoadingMode.IDLE) {
                    Toast.makeText(v.getContext(), "点击", Toast.LENGTH_LONG).show();
                }
            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (loadingMode == LoadingMode.IDLE) {
                    startLoading();
                }
                return true;
            }
        });

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
        }
    }

    private void endLoading() {
        if (loadingMode == LoadingMode.LOADING) {
            loadingMode = LoadingMode.IDLE;
            loadingTime.cancel();
            postInvalidate();
        }
    }

    private class LoadingTime extends CountDownTimer {

        private static final long millisInFuture = 10 * 1000;
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
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null) {
            float padding = paint.getStrokeWidth() / 2;
            rectF = new RectF(0 + padding, 0 + padding, getWidth() - padding, getHeight() - padding);
        }
        if (loadingMode == LoadingMode.LOADING) {
            canvas.drawArc(rectF, -90, loadingTime.getPercent() * 360, false, paint);
        }
    }

    private float initY;

    private void initTouch(MotionEvent event) {
        initY = event.getY(0);
        if (loadingCaller != null) {
            loadingCaller.init();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
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
                        loadingCaller.offset(dy / 200);
                        loadingCaller.offset((int) (dy / 50));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (loadingMode == LoadingMode.LOADING) {
                    endLoading();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
