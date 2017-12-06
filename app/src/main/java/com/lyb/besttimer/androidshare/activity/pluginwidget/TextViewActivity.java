package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.androidshare.view.ImageMovementMethod;
import com.lyb.besttimer.androidshare.view.ImageSaveGetter;
import com.lyb.besttimer.pluginwidget.caller.DrawCaller;
import com.lyb.besttimer.pluginwidget.view.textview.BaseTextView;

import org.xml.sax.XMLReader;

public class TextViewActivity extends BaseActivity {

    private Spanned spanned;

    Html.ImageGetter imageGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        final BaseTextView btv = findViewById(R.id.btv);
        btv.getDrawCallerManager().addBGDrawCaller(new OneDrawCaller());

        final String sourceText = "666666666666666666666666666<img src='" + R.drawable.leak_canary_icon + "'/>6666666666666666666666666666666"
                + "<img src='https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512558246169&di=efcdbb5bce823a14a4769f8e5326e22f&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201504%2F24%2F20150424H0622_82mkM.jpeg'></img>"
                + "66666666666666666666666666666666666666";

        imageGetter = new ImageSaveGetter(this, new ImageSaveGetter.ImageDoneCall() {
            @Override
            public void doneCall() {
                btv.setText(Html.fromHtml(sourceText, imageGetter, null));
            }
        });

        btv.setText(Html.fromHtml(sourceText, imageGetter, null));

        btv.setMovementMethod(ImageMovementMethod.getInstance());
    }

    public class MTagHandler implements Html.TagHandler {
        private int sIndex = 0;
        private int eIndex = 0;

        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            // TODO Auto-generated method stub
            if (tag.toLowerCase().equals("img")) {
                if (opening) {
                    sIndex = output.length();
                } else {
                    eIndex = output.length();
                    output.setSpan(new MSpan(), sIndex, eIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        private class MSpan extends ClickableSpan implements View.OnClickListener {


            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "sdfdsfsdfdsf", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                  final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                //Do something with URL here.
                String url = urlSpan.getURL();
                Toast.makeText(TextViewActivity.this, "链接：" + url, Toast.LENGTH_SHORT).show();
            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    private CharSequence getClickableHtml(Spanned spannedHtml) {
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
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
