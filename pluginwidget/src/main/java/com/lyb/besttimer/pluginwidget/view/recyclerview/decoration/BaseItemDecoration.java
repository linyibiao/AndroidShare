package com.lyb.besttimer.pluginwidget.view.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * common item decoration
 * Created by linyibiao on 2016/8/17.
 */
public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * draw orientation
     */
    public enum DRAWORIENTATION {
        HORIZONTAL, VERTICAL, BOTH
    }

    private int numPerForm = 1;
    private int lineSize = 2;
    private boolean hasRound = false;
    private DRAWORIENTATION drawOrientation;

    private DecorateDetail decorateDetail;

    public BaseItemDecoration(int numPerForm, int lineSize, boolean hasRound, DRAWORIENTATION drawOrientation, DecorateDetail decorateDetail) {
        this.numPerForm = numPerForm;
        this.lineSize = lineSize;
        this.hasRound = hasRound;
        this.drawOrientation = drawOrientation;
        this.decorateDetail = decorateDetail;
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

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
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int adapterPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        boolean canScrollHorizontally = parent.getLayoutManager().canScrollHorizontally();

        int left = getLeft(canScrollHorizontally, drawOrientation, parent, adapterPosition);
        int top = getTop(canScrollHorizontally, drawOrientation, parent, adapterPosition);
        int right = getRight(canScrollHorizontally, drawOrientation, parent, adapterPosition);
        int bottom = getBottom(canScrollHorizontally, drawOrientation, parent, adapterPosition);

        outRect.set(left, top, right, bottom);
    }

    private int getLeft(boolean canScrollHorizontally, DRAWORIENTATION drawOrientation, RecyclerView parent, int adapterPosition) {
        int left = 0;
        boolean isFirst = (canScrollHorizontally && adapterPosition < numPerForm) || (!canScrollHorizontally && adapterPosition % numPerForm == 0);
//        boolean isFirst = (canScrollHorizontally && (adapterPosition - numPerForm) < 0) || (!canScrollHorizontally && adapterPosition % numPerForm == 0);
        if (hasRound) {
            if (isFirst) {
                left = lineSize;
            }
        }
        return left;
    }

    private int getTop(boolean canScrollHorizontally, DRAWORIENTATION drawOrientation, RecyclerView parent, int adapterPosition) {
        int top = 0;
        boolean isFirst = (canScrollHorizontally && adapterPosition % numPerForm == 0) || (!canScrollHorizontally && adapterPosition < numPerForm);
//        boolean isFirst = (canScrollHorizontally && adapterPosition % numPerForm == 0) || (!canScrollHorizontally && (adapterPosition - numPerForm) < 0);
        if (hasRound) {
            if (isFirst) {
                top = lineSize;
            }
        }
        return top;
    }

    private int getRight(boolean canScrollHorizontally, DRAWORIENTATION drawOrientation, RecyclerView parent, int adapterPosition) {

        int itemCount = parent.getAdapter().getItemCount();
        int formCount = (itemCount - 1) / numPerForm + 1;

        boolean isLast = (canScrollHorizontally && adapterPosition / numPerForm == formCount - 1) || (!canScrollHorizontally && (adapterPosition + 1) % numPerForm == 0);
//        boolean isLast = (canScrollHorizontally && (adapterPosition + numPerForm) > itemCount - 1) || (!canScrollHorizontally && (adapterPosition + 1) % numPerForm == 0);
        if (drawOrientation == DRAWORIENTATION.HORIZONTAL) {
            int right = 0;
            if (hasRound) {
                if (isLast) {
                    right = lineSize;
                }
            }
            return right;
        } else {
            int right = lineSize;
            if (!hasRound) {
                if (isLast) {
                    right = 0;
                }
            }
            return right;
        }
    }

    private int getBottom(boolean canScrollHorizontally, DRAWORIENTATION drawOrientation, RecyclerView parent, int adapterPosition) {

        int itemCount = parent.getAdapter().getItemCount();
        int formCount = (itemCount - 1) / numPerForm + 1;

        boolean isLast = (canScrollHorizontally && (adapterPosition + 1) % numPerForm == 0) || (!canScrollHorizontally && adapterPosition / numPerForm == formCount - 1);
//        boolean isLast = (canScrollHorizontally && (adapterPosition + 1) % numPerForm == 0) || (!canScrollHorizontally && (adapterPosition + numPerForm) > itemCount - 1);
        if (drawOrientation == DRAWORIENTATION.VERTICAL) {
            int bottom = 0;
            if (hasRound) {
                if (isLast) {
                    bottom = lineSize;
                }
            }
            return bottom;
        } else {
            int bottom = lineSize;
            if (!hasRound) {
                if (isLast) {
                    bottom = 0;
                }
            }
            return bottom;
        }
    }

}
