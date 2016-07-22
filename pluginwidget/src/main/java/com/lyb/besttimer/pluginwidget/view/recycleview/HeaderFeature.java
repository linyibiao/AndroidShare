package com.lyb.besttimer.pluginwidget.view.recycleview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Suspension head characteristics
 * Created by linyibiao on 2016/7/19.
 */
public abstract class HeaderFeature extends RecyclerView.OnScrollListener {

    private RecyclerView recyclerView;

    private int targetPosition = RecyclerView.NO_POSITION;

    private FrameLayout headerLayout;

    private SparseArray<RecyclerView.ViewHolder> holderSparseArray = new SparseArray<>();

    private HEADER_ORIENTION header_oriention = HEADER_ORIENTION.HORIZONTAL;

    public enum HEADER_ORIENTION {
        HORIZONTAL, VERTICAL,
    }

    /**
     * Unique constructor
     *
     * @param recyclerView     Target view
     * @param header           The header view
     * @param header_oriention Head view display direction
     */
    public HeaderFeature(RecyclerView recyclerView, View header, HEADER_ORIENTION header_oriention) {
        this.recyclerView = recyclerView;
        headerLayout = (FrameLayout) header;
        this.header_oriention = header_oriention;
    }

    public void applyFeature() {
        recyclerView.addOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        updateHeader();
    }

    private Runnable postUpdateHeader = new Runnable() {
        @Override
        public void run() {
            updateHeader();
        }
    };

    private void updateHeader() {
        if (recyclerView.isAnimating()) {
            // TODO: 2016/7/22 I have to use recyclerView.isAnimating(),because the return of recyclerView.getChildAdapterPosition is not normal,what can I do?
            recyclerView.removeCallbacks(postUpdateHeader);
            recyclerView.post(postUpdateHeader);
            return;
        }
        int headerPosition = showHeaderPosition();
        if (headerPosition != RecyclerView.NO_POSITION) {
            if (getTargetAdapterPosition() != headerPosition) {
                releaseHeader();
                setupHeader(headerPosition);
            }
            ajustHeader();
            headerLayout.setVisibility(View.VISIBLE);
        } else {
            releaseHeader();
            headerLayout.setVisibility(View.GONE);
        }
    }

    private void ajustHeader() {
        int headerPosition = getTargetAdapterPosition();
        for (int index = 0; index < recyclerView.getChildCount(); index++) {
            View nextChild = recyclerView.getChildAt(index);
            int position = recyclerView.getChildAdapterPosition(nextChild);
            if (position < 0 || position >= recyclerView.getAdapter().getItemCount()) {
                continue;
            }
            if (isHeader(recyclerView, position)) {
                if (position != headerPosition) {
                    int ScrollX = 0;
                    int ScrollY = 0;
                    if (header_oriention == HEADER_ORIENTION.HORIZONTAL) {
                        ScrollX = headerLayout.getChildAt(0).getMeasuredWidth() - (nextChild.getLeft() - recyclerView.getPaddingLeft());
                    } else if (header_oriention == HEADER_ORIENTION.VERTICAL) {
                        ScrollY = headerLayout.getChildAt(0).getMeasuredHeight() - (nextChild.getTop() - recyclerView.getPaddingTop());
                    }
                    headerLayout.scrollTo(ScrollX > 0 ? ScrollX : 0, ScrollY > 0 ? ScrollY : 0);
                    return;
                }
            }
        }
        headerLayout.scrollTo(0, 0);
    }

    private int getTargetAdapterPosition() {
        return targetPosition;
    }

    private void setupHeader(int headerPosition) {
        int viewType = recyclerView.getAdapter().getItemViewType(headerPosition);
        RecyclerView.ViewHolder viewHolder = holderSparseArray.get(viewType);
        if (viewHolder == null) {
            viewHolder = recyclerView.getAdapter().createViewHolder(recyclerView, recyclerView.getAdapter().getItemViewType(headerPosition));
            holderSparseArray.put(viewType, viewHolder);
        }
        recyclerView.getAdapter().bindViewHolder(viewHolder, headerPosition);

        View view = viewHolder.itemView;

        ViewGroup.LayoutParams params = view.getLayoutParams();

        int widthSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY), recyclerView.getPaddingLeft() + recyclerView.getPaddingRight(), params.width);
        int heightSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), View.MeasureSpec.EXACTLY), recyclerView.getPaddingTop() + recyclerView.getPaddingBottom(), params.height);

        view.measure(widthSpec, heightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        headerLayout.addView(view, params);
        targetPosition = headerPosition;
    }

    private void releaseHeader() {

        View view = headerLayout.getChildAt(0);
        headerLayout.removeView(view);
        headerLayout.scrollTo(0, 0);

        targetPosition = RecyclerView.NO_POSITION;

    }

    private int showHeaderPosition() {
        View firstView = recyclerView.getChildAt(0);
        int position = recyclerView.getChildAdapterPosition(firstView);
        for (int currPos = position; currPos >= 0; currPos--) {
            if (currPos < 0 || currPos >= recyclerView.getAdapter().getItemCount()) {
                continue;
            }
            if (isHeader(recyclerView, currPos)) {
                return currPos;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * Is set to a suspended head
     *
     * @param recyclerView
     * @param position
     * @return
     */
    public abstract boolean isHeader(RecyclerView recyclerView, int position);

}
