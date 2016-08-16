package com.lyb.besttimer.androidshare.activity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.caller.DrawCaller;
import com.lyb.besttimer.pluginwidget.view.textview.BaseTextView;

public class TextViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        BaseTextView btv = (BaseTextView) findViewById(R.id.btv);
        btv.addBGDrawCaller(new OneDrawCaller());
    }

    private class OneDrawCaller implements DrawCaller {

        private int padding = 4;
        private int triangleW = 10;
        private int triangleH = 10;
        private int lineW = 4;

        @Override
        public Rect getPadding() {
            return new Rect(triangleW + padding, padding, padding, padding);
        }

        @Override
        public void ondraw(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            int extraPad = lineW / 2;
            Path path = new Path();
            path.moveTo(triangleW + extraPad, extraPad);
            path.lineTo(width - extraPad, extraPad);
            path.lineTo(width - extraPad, height - extraPad);
            path.lineTo(triangleW + extraPad, height - extraPad);
            path.lineTo(extraPad, height / 2);
            path.lineTo(triangleW + extraPad, (height - triangleH) / 2);
            path.close();

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(0xFFEAE2DB);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineW);
            canvas.drawPath(path, paint);

        }

    }

}
