package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.LogUtil;

public class EditTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        final EditText editText = (EditText) findViewById(R.id.et);
        editText.addTextChangedListener(new TextWatcher() {

            private boolean hadSet=false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setLineSpacing(0,0);
                if (!hadSet&&editText.getLineCount()>=3){
                    int maxHeight=0;
                    Rect bounds=new Rect();
                    editText.getLineBounds(0,bounds);
                    maxHeight+=bounds.height()+bounds.top;
                    LogUtil.logE("0000  "+bounds);
                    editText.getLineBounds(1,bounds);
                    maxHeight+=bounds.height();
                    LogUtil.logE("0001  "+bounds);
                    editText.getLineBounds(editText.getLineCount()-1,bounds);
                    maxHeight+=bounds.height()+(editText.getHeight()-bounds.bottom);
                    LogUtil.logE("0002  "+bounds);
                    editText.setMaxHeight(maxHeight);
                    hadSet=true;
                }
//                LogUtil.logE(editText.getLineCount()+"  "+editText.getLineHeight()+"  "+editText.getLineBounds(0,null)+"  "+editText.getLineSpacingMultiplier());
                if (s.toString().equals("hello")) {
                    editText.setError("what");
                }
            }
        });
    }
}
