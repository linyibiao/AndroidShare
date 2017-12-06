package com.lyb.besttimer.androidshare.view;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.Touch;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 图片截取方法
 *
 * @author linyibiao
 * @since 2017/12/6 15:12
 */
public class ImageMovementMethod extends LinkMovementMethod {

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new ImageMovementMethod();

        return sInstance;
    }

    private static ImageMovementMethod sInstance;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ImageSpan[] links = buffer.getSpans(off, off, ImageSpan.class);

            if (links.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    ImageSpan imageSpan = links[0];
                    Toast.makeText(widget.getContext(), "截取图片：" + imageSpan.getSource(), Toast.LENGTH_SHORT).show();
                } else if (action == MotionEvent.ACTION_DOWN) {
//                    Selection.setSelection(buffer,
//                            buffer.getSpanStart(links[0]),
//                            buffer.getSpanEnd(links[0]));
                }
                return true;
            } else {
//                Selection.removeSelection(buffer);
            }
        }

        return Touch.onTouchEvent(widget, buffer, event);
    }

}
