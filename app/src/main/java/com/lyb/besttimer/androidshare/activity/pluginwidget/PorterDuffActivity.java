package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.androidshare.adapter.PorterDuffAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.decoration.BaseItemDecoration;
import com.lyb.besttimer.pluginwidget.view.recyclerview.decoration.ColorDecorateDetail;

import java.util.ArrayList;
import java.util.List;

public class PorterDuffActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porter_duff);
        List<Pair<String, PorterDuff.Mode>> modes = new ArrayList<>();
        modes.add(new Pair<>("CLEAR", PorterDuff.Mode.CLEAR));
        modes.add(new Pair<>("SRC", PorterDuff.Mode.SRC));
        modes.add(new Pair<>("DST", PorterDuff.Mode.DST));
        modes.add(new Pair<>("SRC_OVER", PorterDuff.Mode.SRC_OVER));
        modes.add(new Pair<>("DST_OVER", PorterDuff.Mode.DST_OVER));
        modes.add(new Pair<>("SRC_IN", PorterDuff.Mode.SRC_IN));
        modes.add(new Pair<>("DST_IN", PorterDuff.Mode.DST_IN));
        modes.add(new Pair<>("SRC_OUT", PorterDuff.Mode.SRC_OUT));
        modes.add(new Pair<>("DST_OUT", PorterDuff.Mode.DST_OUT));
        modes.add(new Pair<>("SRC_ATOP", PorterDuff.Mode.SRC_ATOP));
        modes.add(new Pair<>("DST_ATOP", PorterDuff.Mode.DST_ATOP));
        modes.add(new Pair<>("XOR", PorterDuff.Mode.XOR));
        modes.add(new Pair<>("DARKEN", PorterDuff.Mode.DARKEN));
        modes.add(new Pair<>("LIGHTEN", PorterDuff.Mode.LIGHTEN));
        modes.add(new Pair<>("MULTIPLY", PorterDuff.Mode.MULTIPLY));
        modes.add(new Pair<>("SCREEN", PorterDuff.Mode.SCREEN));
        modes.add(new Pair<>("ADD", PorterDuff.Mode.ADD));
        modes.add(new Pair<>("OVERLAY", PorterDuff.Mode.OVERLAY));
        RecyclerView brv = ((RecyclerView) findViewById(R.id.brv));
        brv.setAdapter(new PorterDuffAdapter(modes));
        brv.addItemDecoration(new BaseItemDecoration(10, new ColorDecorateDetail(0)));
    }

}
