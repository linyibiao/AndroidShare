package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.ColorInt;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.pluginwidget.caller.DrawCaller;
import com.lyb.besttimer.pluginwidget.view.textview.BaseTextView;

public class TextViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        BaseTextView btv = (BaseTextView) findViewById(R.id.btv);
        btv.getDrawCallerManager().addBGDrawCaller(new OneDrawCaller());
    }

    private class OneDrawCaller implements DrawCaller {

        private int padding = 4;
        private int triangleW = 10;
        private int triangleH = 10;
        private float lineW = 6;

        @ColorInt
        private int color = 0xffff0000;

        @Override
        public Rect getPadding() {
            return new Rect(triangleW + padding, padding, padding, padding);
        }

        @Override
        public void setbackgroundcolor(@ColorInt int color) {
            if (this.color == color) {
                return;
            }
            this.color = color;
        }

        @Override
        public void ondraw(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            Path path = new Path();

            drawPath(path, width, height, lineW / 2);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setColor(0xFFEAE2DB);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineW);
            canvas.drawPath(path, paint);

            drawPath(path, width, height, lineW);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(lineW);
            canvas.drawPath(path, paint);

        }

        private void drawPath(Path path, int width, int height, float extraPad) {
            path.reset();
            path.moveTo(triangleW + extraPad, extraPad);
            path.lineTo(width - extraPad, extraPad);
            path.lineTo(width - extraPad, height - extraPad);
            path.lineTo(triangleW + extraPad, height - extraPad);
            path.lineTo(extraPad, height / 2);
            path.lineTo(triangleW + extraPad, (height - triangleH) / 2);
            path.close();
        }

    }

}
