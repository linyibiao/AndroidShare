package com.lyb.besttimer.pluginwidget.view.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * common item decoration
 * 这个decoration需要调整recyclerview的padding属性，请慎用
 * Created by linyibiao on 2016/8/17.
 */
public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    private int lineSize = 2;

    private DecorateDetail decorateDetail;

    public BaseItemDecoration(int lineSize, DecorateDetail decorateDetail) {
        this.lineSize = lineSize;
        this.decorateDetail = decorateDetail;
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        parent.setPadding(0, 0, -lineSize, -lineSize);
        c.save();
        for (int index = 0; index < parent.getChildCount(); index++) {
            View childView = parent.getChildAt(index);
            int decoratedLeft = parent.getLayoutManager().getDecoratedLeft(childView);
            int left = childView.getLeft();
            int decoratedTop = parent.getLayoutManager().getDecoratedTop(childView);
            int top = childView.getTop();
            int decoratedRight = parent.getLayoutManager().getDecoratedRight(childView);
            int right = childView.getRight();
            int decoratedBottom = parent.getLayoutManager().getDecoratedBottom(childView);
            int bottom = childView.getBottom();
            if (decorateDetail != null) {
                decorateDetail.drawLeft(c, decoratedLeft, decoratedTop, left, decoratedBottom);
                decorateDetail.drawTop(c, decoratedLeft, decoratedTop, decoratedRight, top);
                decorateDetail.drawRight(c, right, decoratedTop, decoratedRight, decoratedBottom);
                decorateDetail.drawBottom(c, decoratedLeft, bottom, decoratedRight, decoratedBottom);
            }
        }
        c.restore();
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, lineSize, lineSize);
    }

}
