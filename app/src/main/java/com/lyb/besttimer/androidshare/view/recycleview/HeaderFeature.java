package com.lyb.besttimer.androidshare.view.recycleview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 悬浮头部特性
 * Created by 林一彪 on 2016/7/19.
 */
public abstract class HeaderFeature extends RecyclerView.OnScrollListener {

    private RecyclerView recyclerView;

    private RecyclerView.ViewHolder targetHolder = null;

    private int targetPosition = RecyclerView.NO_POSITION;

    private FrameLayout headerLayout;

    private SparseArray<Pair<Integer, Integer>> positionSizeArray = new SparseArray<>();

    private HEADER_ORIENTION HEADEROriention = HEADER_ORIENTION.HORIZONTAL;

    public enum HEADER_ORIENTION {
        HORIZONTAL, VERTICAL,
    }

    /**
     * 唯一构造函数
     *
     * @param recyclerView    目标视图
     * @param header          头视图，要求最外层加多一层
     * @param HEADEROriention 头视图摆放方向
     */
    public HeaderFeature(RecyclerView recyclerView, View header, HEADER_ORIENTION HEADEROriention) {
        this.recyclerView = recyclerView;
        headerLayout = (FrameLayout) header;
        this.HEADEROriention = HEADEROriention;
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

    private void updateHeader() {
        int headerPosition = showHeaderPosition(0);
        if (headerPosition != RecyclerView.NO_POSITION) {
            if (targetHolder == null || getTargetAdapterPosition() != headerPosition) {
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
            if (isHeader(recyclerView, position)) {
                if (position != headerPosition) {
                    int translationX = 0;
                    int translationY = 0;
                    if (HEADEROriention == HEADER_ORIENTION.HORIZONTAL) {
                        translationX = nextChild.getLeft() - recyclerView.getPaddingLeft() - headerLayout.getChildAt(0).getWidth();
                    } else if (HEADEROriention == HEADER_ORIENTION.VERTICAL) {
                        translationY = nextChild.getTop() - recyclerView.getPaddingTop() - headerLayout.getChildAt(0).getHeight();
                    }
                    ViewCompat.setTranslationX(headerLayout, translationX < 0 ? translationX : 0);
                    ViewCompat.setTranslationY(headerLayout, translationY < 0 ? translationY : 0);
                }
                break;
            }
        }
    }

    private int getTargetAdapterPosition() {
        int position = targetHolder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            return position;
        }
        return targetPosition;
    }

    private void setupHeader(int headerPosition) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(headerPosition);
        //如果为空说明还没有显示出来，那我们就创建一个
        if (viewHolder == null) {
            viewHolder = recyclerView.getAdapter().createViewHolder(recyclerView, recyclerView.getAdapter().getItemViewType(headerPosition));
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            recyclerView.getAdapter().bindViewHolder(viewHolder, headerPosition);
        }
        View view = ((ViewGroup) viewHolder.itemView).getChildAt(0);
        Pair<Integer, Integer> sizePair = positionSizeArray.get(headerPosition, new Pair<>(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (view.getWidth() != 0 && view.getHeight() != 0) {
            sizePair = new Pair<>(view.getWidth(), view.getHeight());
            positionSizeArray.put(headerPosition, sizePair);
        }
        viewHolder.itemView.getLayoutParams().width = sizePair.first;
        viewHolder.itemView.getLayoutParams().height = sizePair.second;
        targetHolder = viewHolder;
        ((ViewGroup) viewHolder.itemView).removeView(view);
        viewHolder.setIsRecyclable(false);
        headerLayout.addView(view, new FrameLayout.LayoutParams(sizePair.first, sizePair.second));
        targetPosition = headerPosition;
    }

    private void releaseHeader() {
        if (targetHolder != null) {
            View view = headerLayout.getChildAt(0);
            headerLayout.removeView(view);
            ViewCompat.setTranslationX(headerLayout, 0);
            ViewCompat.setTranslationY(headerLayout, 0);
            ((ViewGroup) targetHolder.itemView).addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            targetHolder.setIsRecyclable(true);
            targetHolder = null;
        }
        targetPosition = RecyclerView.NO_POSITION;
    }

    private int showHeaderPosition(int childIndex) {
        View firstView = recyclerView.getChildAt(childIndex);
        int position = recyclerView.getChildAdapterPosition(firstView);
        for (int currPos = position; currPos >= 0; currPos--) {
            if (isHeader(recyclerView, currPos)) {
                return currPos;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * 是否设置为悬浮头部
     *
     * @param recyclerView
     * @param position
     * @return
     */
    public abstract boolean isHeader(RecyclerView recyclerView, int position);

}
