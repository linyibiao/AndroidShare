package com.lyb.besttimer.pluginwidget.view.linearlayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * linear adapter
 * Created by linyibiao on 2017/1/9.
 */

public abstract class LinearAdapter {

    private TableInfo tableInfo;

    public LinearAdapter() {
    }

    public LinearAdapter(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    private List<List<BaseLinearHolder>> saveHolderLists = new ArrayList<>();

    public abstract void onBindViewHolder(BaseLinearHolder holder, int position);

    public abstract BaseLinearHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public int getItemViewType(int position) {
        return 0;
    }

    public abstract int getItemCount();

    private boolean hasBuildView() {
        return saveHolderLists.size() > 0;
    }

    public void notifyDataSetChanged() {

        if (linearLayout == null || tableInfo == null) {
            return;
        }

        Context context = linearLayout.getContext();

        if (!hasBuildView()) {

            linearLayout.setOrientation(LinearLayout.VERTICAL);

            int position = 0;
            for (RowInfo rowInfo : tableInfo.getRowInfos()) {
                BaseLinearLayout baseLinearLayoutRow = new BaseLinearLayout(context);
                baseLinearLayoutRow.setOrientation(LinearLayout.HORIZONTAL);
                final LinearLayout.LayoutParams rowLayoutParams;

                final boolean isMatchParent;

                if (rowInfo.getWeight() == 0) {
                    rowLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    isMatchParent = false;
                } else {
                    rowLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, rowInfo.getWeight());
                    isMatchParent = true;
                }
                rowLayoutParams.topMargin = rowInfo.getTopMargin();
                rowLayoutParams.bottomMargin = rowInfo.getBottomMargin();
                linearLayout.addView(baseLinearLayoutRow, rowLayoutParams);

                List<BaseLinearHolder> saveHolderList = new ArrayList<>();
                saveHolderLists.add(saveHolderList);
                float currRowWeights = 0;
                for (ItemInfo itemInfo : rowInfo.getItemInfos()) {

                    BaseLinearHolder baseLinearHolder = onCreateViewHolder(baseLinearLayoutRow, getItemViewType(position));
                    saveHolderList.add(baseLinearHolder);

                    final LinearLayout.LayoutParams itemLayoutParams;
                    if (itemInfo.getWeight() == 0) {
                        itemLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, isMatchParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
                        currRowWeights = -1;
                    } else {
                        itemLayoutParams = new LinearLayout.LayoutParams(0, isMatchParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, itemInfo.getWeight());
                        if (currRowWeights != -1) {
                            currRowWeights += itemInfo.getWeight();
                        }
                    }
                    FrameLayout itemParentLayout = new FrameLayout(context);
                    itemParentLayout.setPadding(itemInfo.getLeftPadding(), 0, itemInfo.getRightPadding(), 0);
                    itemParentLayout.addView(baseLinearHolder.itemView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    baseLinearLayoutRow.addView(itemParentLayout, itemLayoutParams);

                    position++;

                }

                if (currRowWeights != -1 && currRowWeights < tableInfo.getMaxRowWeights()) {
                    baseLinearLayoutRow.addView(new View(context), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, tableInfo.getMaxRowWeights() - currRowWeights));
                }

            }
        }

        int position = 0;
        for (int tableRowInfoIndex = 0; tableRowInfoIndex < tableInfo.getRowInfos().size(); tableRowInfoIndex++) {
            RowInfo rowInfo = tableInfo.getRowInfos().get(tableRowInfoIndex);
            List<BaseLinearHolder> saveHolderList = saveHolderLists.get(tableRowInfoIndex);
            for (int tableItemInfoIndex = 0; tableItemInfoIndex < rowInfo.getItemInfos().size(); tableItemInfoIndex++) {

                if (position + 1 > getItemCount()) {
                    return;
                }

                BaseLinearHolder baseLinearHolder = saveHolderList.get(tableItemInfoIndex);
                onBindViewHolder(baseLinearHolder, position);

                position++;

            }
        }

    }

    public static class TableInfo {

        private List<RowInfo> rowInfos = new ArrayList<>();
        private float maxRowWeights;

        public TableInfo(List<RowInfo> rowInfos, float maxRowWeights) {
            this.rowInfos = rowInfos;
            this.maxRowWeights = maxRowWeights;
        }

        public List<RowInfo> getRowInfos() {
            return rowInfos;
        }

        public float getMaxRowWeights() {
            return maxRowWeights;
        }
    }

    public static class RowInfo {

        private List<ItemInfo> itemInfos = new ArrayList<>();
        private int topMargin;
        private int bottomMargin;
        private float weight;

        public RowInfo(int topMargin, int bottomMargin, float weight) {
            this.topMargin = topMargin;
            this.bottomMargin = bottomMargin;
            this.weight = weight;
        }

        public void addItemInfo(ItemInfo itemInfo) {
            itemInfos.add(itemInfo);
        }

        public List<ItemInfo> getItemInfos() {
            return itemInfos;
        }

        public int getTopMargin() {
            return topMargin;
        }

        public int getBottomMargin() {
            return bottomMargin;
        }

        public float getWeight() {
            return weight;
        }
    }

    public static class ItemInfo {

        private int leftPadding;
        private int rightPadding;
        private float weight;

        public ItemInfo(int leftPadding, int rightPadding, float weight) {
            this.leftPadding = leftPadding;
            this.rightPadding = rightPadding;
            this.weight = weight;
        }

        public int getLeftPadding() {
            return leftPadding;
        }

        public int getRightPadding() {
            return rightPadding;
        }

        public float getWeight() {
            return weight;
        }
    }

    private LinearLayout linearLayout;

    public void onAttachedToLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void onDetachedFromLinearLayout(LinearLayout linearLayout) {
    }

}
