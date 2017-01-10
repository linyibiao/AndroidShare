package com.lyb.besttimer.pluginwidget.view.tablerow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;

/**
 * base tablerow
 * Created by linyibiao on 2017/1/6.
 */

public class BaseTableRow extends TableRow {

    public BaseTableRow(Context context) {
        super(context);
        init(null);
    }

    public BaseTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

    }

}
