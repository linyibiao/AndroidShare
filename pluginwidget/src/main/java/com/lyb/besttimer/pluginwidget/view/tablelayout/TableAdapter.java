package com.lyb.besttimer.pluginwidget.view.tablelayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;
import com.lyb.besttimer.pluginwidget.view.tablerow.BaseTableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * table adapter
 * Created by linyibiao on 2017/1/9.
 */

public abstract class TableAdapter extends BaseAdapter {

    private TableInfo tableInfo;

    public TableAdapter(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    private List<List<BaseHolder>> saveHolderLists = new ArrayList<>();

    private boolean hasBuildView() {
        return saveHolderLists.size() > 0;
    }

    public void notifyTableDataSetChanged() {

        if (tableLayout == null) {
            return;
        }

        Context context = tableLayout.getContext();

        if (!hasBuildView()) {
            tableLayout.setStretchAllColumns(true);

            BaseTableRow firstBaseTableRow = new BaseTableRow(context);
            tableLayout.addView(firstBaseTableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            View firstRowItemView = new View(context);
            TableRow.LayoutParams firstRowItemParams = new TableRow.LayoutParams(0, 0);
            firstRowItemParams.span = tableInfo.getMaxRowSpans();
            firstBaseTableRow.addView(firstRowItemView, firstRowItemParams);

            int position = 0;
            for (TableRowInfo tableRowInfo : tableInfo.getTableRowInfoList()) {
                BaseTableRow baseTableRow = new BaseTableRow(context);
                TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, tableRowInfo.getWeight());
                tableLayoutParams.topMargin = tableRowInfo.getTopMargin();
                tableLayoutParams.bottomMargin = tableRowInfo.getBottomMargin();
                tableLayout.addView(baseTableRow, tableLayoutParams);

                List<BaseHolder> saveHolderList = new ArrayList<>();
                saveHolderLists.add(saveHolderList);
                int currRowSpans = 0;
                for (int columnIndex = 0; columnIndex < tableRowInfo.getTableItemInfos().size(); columnIndex++) {

                    TableItemInfo tableItemInfo = tableRowInfo.getTableItemInfos().get(columnIndex);
                    BaseHolder baseHolder = onCreateViewHolder(baseTableRow, getItemViewType(position));
                    saveHolderList.add(baseHolder);

                    currRowSpans += tableItemInfo.getSpan();

                    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    tableRowParams.leftMargin = tableItemInfo.getLeftMargin();
                    tableRowParams.rightMargin = tableItemInfo.getRightMargin();
                    if (columnIndex == tableRowInfo.getTableItemInfos().size() - 1 && currRowSpans == tableInfo.getMaxRowSpans()) {
                        tableRowParams.span = 1;
                        tableRowParams.weight = 1;
                    } else {
                        tableRowParams.span = tableItemInfo.getSpan();
                        tableRowParams.weight = 0;
                    }
                    baseTableRow.addView(baseHolder.itemView, tableRowParams);

                    position++;

                }

            }
        }

        int position = 0;
        for (int tableRowInfoIndex = 0; tableRowInfoIndex < tableInfo.getTableRowInfoList().size(); tableRowInfoIndex++) {
            TableRowInfo tableRowInfo = tableInfo.getTableRowInfoList().get(tableRowInfoIndex);
            List<BaseHolder> saveHolderList = saveHolderLists.get(tableRowInfoIndex);
            for (int tableItemInfoIndex = 0; tableItemInfoIndex < tableRowInfo.getTableItemInfos().size(); tableItemInfoIndex++) {

                if (position + 1 > getItemCount()) {
                    return;
                }

                BaseHolder baseHolder = saveHolderList.get(tableItemInfoIndex);
                onBindViewHolder(baseHolder, position);

                position++;

            }
        }

    }

    public static class TableInfo {

        private List<TableRowInfo> tableRowInfoList = new ArrayList<>();
        private int maxRowSpans;

        public TableInfo(List<TableRowInfo> tableRowInfoList, int maxRowSpans) {
            this.tableRowInfoList = tableRowInfoList;
            this.maxRowSpans = maxRowSpans;
        }

        public List<TableRowInfo> getTableRowInfoList() {
            return tableRowInfoList;
        }

        public int getMaxRowSpans() {
            return maxRowSpans;
        }
    }

    public static class TableRowInfo {

        private List<TableItemInfo> tableItemInfos = new ArrayList<>();
        private int topMargin;
        private int bottomMargin;
        private float weight;

        public TableRowInfo(int topMargin, int bottomMargin, float weight) {
            this.topMargin = topMargin;
            this.bottomMargin = bottomMargin;
            this.weight = weight;
        }

        public void addTableItemInfo(TableItemInfo tableItemInfo) {
            tableItemInfos.add(tableItemInfo);
        }

        public List<TableItemInfo> getTableItemInfos() {
            return tableItemInfos;
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

    public static class TableItemInfo {

        private int leftMargin;
        private int rightMargin;
        private int span;

        public TableItemInfo(int leftMargin, int rightMargin, int span) {
            this.leftMargin = leftMargin;
            this.rightMargin = rightMargin;
            this.span = span;
        }

        public int getLeftMargin() {
            return leftMargin;
        }

        public int getRightMargin() {
            return rightMargin;
        }

        public int getSpan() {
            return span;
        }
    }

    private TableLayout tableLayout;

    public void onAttachedToTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }

    public void onDetachedFromTableLayout(TableLayout tableLayout) {
        this.tableLayout = null;
    }

}
