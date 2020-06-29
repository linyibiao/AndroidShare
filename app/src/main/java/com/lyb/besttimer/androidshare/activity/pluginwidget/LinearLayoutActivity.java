package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.adapter.SimpleLinearAdapter;
import com.lyb.besttimer.pluginwidget.view.linearlayout.BaseLinearLayout;
import com.lyb.besttimer.pluginwidget.view.linearlayout.LinearAdapter;

import java.util.ArrayList;
import java.util.List;

public class LinearLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);
        BaseLinearLayout bll_table = (BaseLinearLayout) findViewById(R.id.bll_table);

        List<LinearAdapter.RowInfo> rowInfos = new ArrayList<>();

        LinearAdapter.RowInfo rowInfo1 = new LinearAdapter.RowInfo(0, 0, 0);
        rowInfo1.addItemInfo(new LinearAdapter.ItemInfo(0, 0, 1));
        rowInfo1.addItemInfo(new LinearAdapter.ItemInfo(10, 0, 1));
        rowInfo1.addItemInfo(new LinearAdapter.ItemInfo(15, 0, 2));
        rowInfo1.addItemInfo(new LinearAdapter.ItemInfo(20, 0, 1));
        rowInfo1.addItemInfo(new LinearAdapter.ItemInfo(25, 0, 3));
        rowInfos.add(rowInfo1);

        LinearAdapter.RowInfo rowInfo2 = new LinearAdapter.RowInfo(10, 0, 0);
        rowInfo2.addItemInfo(new LinearAdapter.ItemInfo(0, 0, 2));
        rowInfo2.addItemInfo(new LinearAdapter.ItemInfo(15, 0, 3));
        rowInfos.add(rowInfo2);

        List<SimpleLinearAdapter.SimpleTableData> simpleTableDatas = new ArrayList<>();
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("为何1"));
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("为何2"));
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("为何3"));
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("为何4"));
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("为何5"));
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("为何6"));
        simpleTableDatas.add(new SimpleLinearAdapter.SimpleTableData("abcdefghijklmnopq"));

        LinearAdapter linearAdapter = new SimpleLinearAdapter(new LinearAdapter.TableInfo(rowInfos, 8), simpleTableDatas);
        bll_table.setAdapter(linearAdapter);
        linearAdapter.notifyDataSetChanged();

    }
}
