package com.lyb.besttimer.pluginwidget.view.recyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Suspension head characteristics
 * Created by linyibiao on 2016/7/19.
 */
public abstract class HeaderFeature {

    private RecyclerView recyclerView;

    private RecyclerView.Adapter preAdapter;

    private int targetAdapterPosition = RecyclerView.NO_POSITION;

    private FrameLayout headerLayout;

    private SparseArray<RecyclerView.ViewHolder> holderSparseArray = new SparseArray<>();
    private RecyclerView.ViewHolder preActiveViewHolder = null;
    private int preActivePosition = RecyclerView.NO_POSITION;

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
    public HeaderFeature(RecyclerView recyclerView, FrameLayout header, HEADER_ORIENTION header_oriention) {
        this.recyclerView = recyclerView;
        headerLayout = header;
        this.header_oriention = header_oriention;
    }

    public void applyFeature() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                if (!recyclerView.isAnimating()) {
                    updateHeader();
                }
            }
        });
    }

    private void updateHeader() {
        int headerPosition = findHeaderPosition();
        if (headerPosition != RecyclerView.NO_POSITION) {
            checkAdapter();
            if (getTargetAdapterPosition() != headerPosition) {
                setupHeader(headerPosition);
            }
            ajustHeader();
            headerLayout.setVisibility(View.VISIBLE);
        } else {
            setTargetAdapterPosition(headerPosition);
            headerLayout.setVisibility(View.GONE);
        }
    }

    private void ajustHeader() {
        int headerPosition = getTargetAdapterPosition();
        for (int index = 0; index < recyclerView.getChildCount(); index++) {
            View nextChild = recyclerView.getChildAt(index);
            int position = recyclerView.getChildAdapterPosition(nextChild);
            if (position != RecyclerView.NO_POSITION && position != headerPosition && isHeader(recyclerView, position)) {
                int scrollX = 0;
                int scrollY = 0;
                if (header_oriention == HEADER_ORIENTION.HORIZONTAL) {
                    scrollX = headerLayout.getChildAt(0).getMeasuredWidth() - (nextChild.getLeft() - recyclerView.getPaddingLeft());
                } else if (header_oriention == HEADER_ORIENTION.VERTICAL) {
                    scrollY = headerLayout.getChildAt(0).getMeasuredHeight() - (nextChild.getTop() - recyclerView.getPaddingTop());
                }
                headerLayout.scrollTo(scrollX > 0 ? scrollX : 0, scrollY > 0 ? scrollY : 0);
                return;
            }
        }
        headerLayout.scrollTo(0, 0);
    }

    private int getTargetAdapterPosition() {
        return targetAdapterPosition;
    }

    private void setTargetAdapterPosition(int targetAdapterPosition) {
        this.targetAdapterPosition = targetAdapterPosition;
    }

    private void checkAdapter() {
        if (preAdapter != null && !preAdapter.equals(recyclerView.getAdapter())) {
            resetStatus();
        }
    }

    private void resetStatus() {
        preAdapter = recyclerView.getAdapter();
        targetAdapterPosition = RecyclerView.NO_POSITION;
        holderSparseArray.clear();
        preActiveViewHolder = null;
        preActivePosition = RecyclerView.NO_POSITION;
    }

    private void setupHeader(int headerPosition) {
        int viewType = recyclerView.getAdapter().getItemViewType(headerPosition);

        if ((preActivePosition >= 0 && preActivePosition < recyclerView.getAdapter().getItemCount())
                && viewType == recyclerView.getAdapter().getItemViewType(preActivePosition)) {
            recyclerView.getAdapter().bindViewHolder(preActiveViewHolder, headerPosition);
        } else {
            headerLayout.removeAllViews();
            RecyclerView.ViewHolder viewHolder = holderSparseArray.get(viewType);

            if (viewHolder == null) {

                viewHolder = recyclerView.getAdapter().createViewHolder(recyclerView, recyclerView.getAdapter().getItemViewType(headerPosition));
                holderSparseArray.put(viewType, viewHolder);

                View view = viewHolder.itemView;
                ViewGroup.LayoutParams params = view.getLayoutParams();
                int widthSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.AT_MOST), recyclerView.getPaddingLeft() + recyclerView.getPaddingRight(), params.width);
                int heightSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), View.MeasureSpec.AT_MOST), recyclerView.getPaddingTop() + recyclerView.getPaddingBottom(), params.height);
                view.measure(widthSpec, heightSpec);
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            }

            headerLayout.addView(viewHolder.itemView);
            recyclerView.getAdapter().bindViewHolder(viewHolder, headerPosition);
            preActiveViewHolder = viewHolder;
        }

        preActivePosition = headerPosition;

        targetAdapterPosition = headerPosition;

    }

    private int findHeaderPosition() {
        int headerPosition = RecyclerView.NO_POSITION;
        View firstView = recyclerView.getChildAt(0);
        int position = recyclerView.getChildAdapterPosition(firstView);
        for (int currPos = position; currPos >= 0; currPos--) {
            if (isHeader(recyclerView, currPos)) {
                headerPosition = currPos;
                if (currPos == position) {
                    if ((currPos + 1 < recyclerView.getAdapter().getItemCount() && isHeader(recyclerView, currPos + 1)) || canHideHeader(firstView)) {
                        headerPosition = RecyclerView.NO_POSITION;
                    }
                }
                break;
            }
        }
        return headerPosition;
    }

    private boolean canHideHeader(View firstView) {
        if (header_oriention == HEADER_ORIENTION.HORIZONTAL) {
            return firstView.getLeft() == recyclerView.getPaddingLeft();
        } else if (header_oriention == HEADER_ORIENTION.VERTICAL) {
            return firstView.getTop() == recyclerView.getPaddingTop();
        }
        return false;
    }

    /**
     * Is set to a suspended head
     *
     * @param recyclerView container
     * @param position     adapter position
     * @return true if is header
     */
    public abstract boolean isHeader(RecyclerView recyclerView, int position);

}
