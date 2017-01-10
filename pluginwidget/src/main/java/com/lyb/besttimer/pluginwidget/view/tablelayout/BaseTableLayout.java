package com.lyb.besttimer.pluginwidget.view.tablelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

/**
 * base tablelayout
 * Created by Administrator on 2017/1/6.
 */

public class BaseTableLayout extends TableLayout {

    public BaseTableLayout(Context context) {
        super(context);
        init(null);
    }

    public BaseTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private TableAdapter tableAdapter;

    private void init(AttributeSet attrs) {

    }

    public TableAdapter getTableAdapter() {
        return tableAdapter;
    }

    public void setTableAdapter(TableAdapter tableAdapter) {
        this.tableAdapter = tableAdapter;
        if (tableAdapter != null) {
            tableAdapter.onAttachedToTableLayout(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (tableAdapter != null) {
            tableAdapter.onAttachedToTableLayout(this);
            tableAdapter.notifyTableDataSetChanged();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (tableAdapter != null) {
            tableAdapter.onDetachedFromTableLayout(this);
        }
    }
}
