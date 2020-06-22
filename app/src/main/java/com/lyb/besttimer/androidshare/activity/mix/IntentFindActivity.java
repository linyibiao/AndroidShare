package com.lyb.besttimer.androidshare.activity.mix;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.databinding.ActivityIntentFindBinding;

import java.util.List;

public class IntentFindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityIntentFindBinding activityIntentFindBinding = DataBindingUtil.setContentView(this, R.layout.activity_intent_find);
        activityIntentFindBinding.tvIsExist.setText(isExist() ? "有啊" : "没有");
    }

    private boolean isExist() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("bkdx://www.baokaodaxue.com/app"));
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

}
