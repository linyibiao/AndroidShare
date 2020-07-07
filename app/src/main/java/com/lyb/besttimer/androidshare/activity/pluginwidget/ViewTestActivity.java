package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.view.SpanResBg;

public class ViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        TextView tv_text = findViewById(R.id.tv_text);
        String str = "物或化或生";
        SpannableString sp = new SpannableString(str);
        sp.setSpan(new SpanResBg(this, R.drawable.shape_rectangle_corner_5dp_solid_ffffff, 100, 10), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.white)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new SpanResBg(this, R.drawable.shape_rectangle_corner_5dp_solid_ffffff, 0, 0), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.setText(sp);
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
