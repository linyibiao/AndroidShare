package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.adapter.SimpleTableAdapter;
import com.lyb.besttimer.pluginwidget.view.tablelayout.BaseTableLayout;
import com.lyb.besttimer.pluginwidget.view.tablelayout.TableAdapter;

import java.util.ArrayList;
import java.util.List;

public class TableLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_layout);
        BaseTableLayout btl = (BaseTableLayout) findViewById(R.id.btl);

        List<TableAdapter.TableRowInfo> tableRowInfos = new ArrayList<>();

        TableAdapter.TableRowInfo tableRowInfo1 = new TableAdapter.TableRowInfo(0, 0, 1);
        tableRowInfo1.addTableItemInfo(new TableAdapter.TableItemInfo(0, 0, 1));
        tableRowInfo1.addTableItemInfo(new TableAdapter.TableItemInfo(10, 0, 1));
        tableRowInfo1.addTableItemInfo(new TableAdapter.TableItemInfo(15, 0, 2));
        tableRowInfo1.addTableItemInfo(new TableAdapter.TableItemInfo(20, 0, 1));
        tableRowInfo1.addTableItemInfo(new TableAdapter.TableItemInfo(25, 0, 3));
        tableRowInfos.add(tableRowInfo1);

        TableAdapter.TableRowInfo tableRowInfo2 = new TableAdapter.TableRowInfo(10, 0, 1);
        tableRowInfo2.addTableItemInfo(new TableAdapter.TableItemInfo(0, 0, 2));
        tableRowInfo2.addTableItemInfo(new TableAdapter.TableItemInfo(15, 0, 3));
        tableRowInfos.add(tableRowInfo2);

        List<SimpleTableAdapter.SimpleTableData> simpleTableDatas = new ArrayList<>();
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何1"));
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何2"));
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何3"));
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何4"));
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何5"));
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何6"));
        simpleTableDatas.add(new SimpleTableAdapter.SimpleTableData("为何7"));

        btl.setTableAdapter(new SimpleTableAdapter(new TableAdapter.TableInfo(tableRowInfos, 8), simpleTableDatas));

    }
}
