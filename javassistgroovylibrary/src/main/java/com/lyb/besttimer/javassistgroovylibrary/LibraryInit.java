package com.lyb.besttimer.javassistgroovylibrary;

import android.content.Context;
import android.widget.Toast;

import com.lyb.besttimer.annotation_bean.IAppInit;

public class LibraryInit implements IAppInit {
    @Override
    public void init(Context applicationContext) {
        Toast.makeText(applicationContext, "来打我呀", Toast.LENGTH_LONG).show();
    }
}
