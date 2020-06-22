package com.lyb.besttimer.pluginwidget.view.complex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;

import com.lyb.besttimer.pluginwidget.view.fragment.WorkStateAppSaver;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Headers9View extends View implements WorkStateAppSaver.Result<Bitmap> {

    public Headers9View(Context context) {
        this(context, null);
    }

    public Headers9View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Headers9View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private static WorkStateAppSaver<String, Bitmap, Headers9View> saver = new WorkStateAppSaver<>();

    private List<RectF> locations = new ArrayList<>(Arrays.asList(new RectF(), new RectF(), new RectF(), new RectF(), new RectF(), new RectF(), new RectF(), new RectF(), new RectF()));
    private List<String> headers = new ArrayList<>();

    private Rect bitmapRect = new Rect();

    private Paint paint;

    public void setHeaders(List<String> headers) {
        this.headers = headers;
        if (this.headers.size() > 9) {
            this.headers = this.headers.subList(0, 9);
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        float size = getWidth();
        float gap = size / 20f;
        if (headers.size() == 0) {

        } else if (headers.size() == 1) {
            float perSize = (size - gap * (2 - 1)) / 2;
            update1RectF(0, size / 2 - perSize / 2, size, gap, perSize, locations.get(0));
        } else if (headers.size() == 2) {
            float perSize = (size - gap * (2 - 1)) / 2;
            update2RectF(0, size / 2 - perSize / 2, size, gap, perSize, locations.get(0), locations.get(1));
        } else if (headers.size() == 3) {
            float perSize = (size - gap * (2 - 1)) / 2;
            update1RectF(0, 0, size, gap, perSize, locations.get(0));
            update2RectF(0, perSize + gap, size, gap, perSize, locations.get(1), locations.get(2));
        } else if (headers.size() == 4) {
            float perSize = (size - gap * (2 - 1)) / 2;
            update2RectF(0, 0, size, gap, perSize, locations.get(0), locations.get(1));
            update2RectF(0, perSize + gap, size, gap, perSize, locations.get(2), locations.get(3));
        } else if (headers.size() == 5) {
            float perSize = (size - gap * (3 - 1)) / 3;
            update2RectF(0, size / 2 - gap / 2 - perSize, size, gap, perSize, locations.get(0), locations.get(1));
            update3RectF(0, size / 2 + gap / 2, size, gap, perSize, locations.get(2), locations.get(3), locations.get(4));
        } else if (headers.size() == 6) {
            float perSize = (size - gap * (3 - 1)) / 3;
            update3RectF(0, size / 2 - gap / 2 - perSize, size, gap, perSize, locations.get(0), locations.get(1), locations.get(2));
            update3RectF(0, size / 2 + gap / 2, size, gap, perSize, locations.get(3), locations.get(4), locations.get(5));
        } else if (headers.size() == 7) {
            float perSize = (size - gap * (3 - 1)) / 3;
            update1RectF(0, 0, size, gap, perSize, locations.get(0));
            update3RectF(0, size / 2 - perSize / 2, size, gap, perSize, locations.get(1), locations.get(2), locations.get(3));
            update3RectF(0, size / 2 + perSize / 2 + gap, size, gap, perSize, locations.get(4), locations.get(5), locations.get(6));
        } else if (headers.size() == 8) {
            float perSize = (size - gap * (3 - 1)) / 3;
            update2RectF(0, 0, size, gap, perSize, locations.get(0), locations.get(1));
            update3RectF(0, size / 2 - perSize / 2, size, gap, perSize, locations.get(2), locations.get(3), locations.get(4));
            update3RectF(0, size / 2 + perSize / 2 + gap, size, gap, perSize, locations.get(5), locations.get(6), locations.get(7));
        } else if (headers.size() == 9) {
            float perSize = (size - gap * (3 - 1)) / 3;
            update3RectF(0, 0, size, gap, perSize, locations.get(0), locations.get(1), locations.get(2));
            update3RectF(0, size / 2 - perSize / 2, size, gap, perSize, locations.get(3), locations.get(4), locations.get(5));
            update3RectF(0, size / 2 + perSize / 2 + gap, size, gap, perSize, locations.get(6), locations.get(7), locations.get(8));
        }
        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        for (int i = 0; i < headers.size(); i++) {
            final String header = headers.get(i);
            final RectF location = locations.get(i);
            Bitmap bitmap = saver.getResult(header);
            if (bitmap != null) {
                int bitmapW = bitmap.getWidth();
                int bitmapH = bitmap.getHeight();
                if (bitmapW >= bitmapH) {
                    bitmapRect.set((bitmapW - bitmapH) / 2, 0, (bitmapW + bitmapH) / 2, bitmapH);
                    canvas.drawBitmap(bitmap, bitmapRect, location, paint);
                } else {
                    bitmapRect.set(0, (bitmapH - bitmapW) / 2, bitmapW, (bitmapH + bitmapW) / 2);
                    canvas.drawBitmap(bitmap, bitmapRect, location, paint);
                }
            } else {
                saver.workWithLife(fragmentManager, header, new WorkStateAppSaver.Worker() {
                    @Override
                    public void work() {
                        Observable
                                .just(header)
                                .map(new Function<String, Bitmap>() {
                                    @Override
                                    public Bitmap apply(String s) throws Exception {
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeStream(new URL(s).openStream(), null, options);
                                        float widthScale = options.outWidth / location.width();
                                        float heightScale = options.outHeight / location.height();
                                        options.inSampleSize = Math.max(1, (int) Math.min(widthScale, heightScale));
                                        options.inJustDecodeBounds = false;
                                        return BitmapFactory.decodeStream(new URL(s).openStream(), null, options);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Bitmap>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Bitmap value) {
                                        if (value != null) {
                                            saver.result(header, value);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        saver.error(header);
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }, this);
            }
        }
    }

    @Override
    public void result(Bitmap bitmap) {
        postInvalidate();
    }

    @Override
    public void error(Object error) {

    }

    private void update1RectF(float startX, float startY, float width, float gap, float perSize, RectF rectF_1) {
        rectF_1.left = width / 2 - perSize / 2;
        rectF_1.top = 0;
        rectF_1.right = rectF_1.left + perSize;
        rectF_1.bottom = rectF_1.top + perSize;
        rectF_1.offset(startX, startY);
    }

    private void update2RectF(float startX, float startY, float width, float gap, float perSize, RectF rectF_1, RectF rectF_2) {
        rectF_1.left = width / 2 - perSize - gap / 2;
        rectF_1.top = 0;
        rectF_1.right = rectF_1.left + perSize;
        rectF_1.bottom = rectF_1.top + perSize;
        rectF_1.offset(startX, startY);
        rectF_2.left = width / 2 + gap / 2;
        rectF_2.top = 0;
        rectF_2.right = rectF_2.left + perSize;
        rectF_2.bottom = rectF_2.top + perSize;
        rectF_2.offset(startX, startY);
    }

    private void update3RectF(float startX, float startY, float width, float gap, float perSize, RectF rectF_1, RectF rectF_2, RectF rectF_3) {
        rectF_1.left = width / 2 - perSize * 1.5f - gap;
        rectF_1.top = 0;
        rectF_1.right = rectF_1.left + perSize;
        rectF_1.bottom = rectF_1.top + perSize;
        rectF_1.offset(startX, startY);
        rectF_2.left = width / 2 - perSize / 2;
        rectF_2.top = 0;
        rectF_2.right = rectF_2.left + perSize;
        rectF_2.bottom = rectF_2.top + perSize;
        rectF_2.offset(startX, startY);
        rectF_3.left = width / 2 + perSize / 2 + gap;
        rectF_3.top = 0;
        rectF_3.right = rectF_3.left + perSize;
        rectF_3.bottom = rectF_3.top + perSize;
        rectF_3.offset(startX, startY);
    }

}
