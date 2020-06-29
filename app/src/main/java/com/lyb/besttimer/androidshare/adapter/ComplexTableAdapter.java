package com.lyb.besttimer.androidshare.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.ColorStateListUtil;
import com.lyb.besttimer.pluginwidget.utils.StateListDrawableUtil;
import com.lyb.besttimer.pluginwidget.utils.ViewState;
import com.lyb.besttimer.pluginwidget.view.tablelayout.TableAdapter;
import com.lyb.besttimer.pluginwidget.view.tablelayout.adapter.BaseTableHolder;
import com.lyb.besttimer.pluginwidget.view.textview.BaseTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比分投注adapter
 * Created by linyibiao on 2017/1/10.
 */

public class ComplexTableAdapter extends TableAdapter {

    private List<String> spScoreValues = new ArrayList<>();
    private Map<Integer, Boolean> spScoreValuesChoose = new HashMap<>();
    private boolean showSP = true;

    private String[] scoreKey = new String[]{"1:0", "2:0", "2:1", "3:0", "3:1", "3:2", "4:0", "4:1", "4:2", "5:0", "5:1", "5:2", "胜其他", "0:0", "1:1", "2:2", "3:3", "平其他", "0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4", "2:4", "0:5", "1:5", "2:5", "负其他"};

    private static TableInfo scoreTableInfo;

    static {

        List<TableRowInfo> tableRowInfos = new ArrayList<>();

        int rowNum = 5;
        int columnNum = 7;
        int gapValue = 10;
        for (int rowIndex = 0; rowIndex < rowNum; rowIndex++) {
            TableRowInfo tableRowInfo = new TableRowInfo(rowIndex == 0 ? 0 : gapValue, 0, 1);
            tableRowInfos.add(tableRowInfo);

            switch (rowIndex) {
                case 0:
                case 3:
                    for (int columnIndex = 0; columnIndex < columnNum; columnIndex++) {
                        tableRowInfo.addTableItemInfo(new TableItemInfo(columnIndex == 0 ? 0 : gapValue, 0, 1));
                    }
                    break;
                case 1:
                case 4:
                    for (int columnIndex = 0; columnIndex < columnNum - 1; columnIndex++) {
                        if (columnIndex == columnNum - 2) {
                            tableRowInfo.addTableItemInfo(new TableItemInfo(columnIndex == 0 ? 0 : gapValue, 0, 2));
                        } else {
                            tableRowInfo.addTableItemInfo(new TableItemInfo(columnIndex == 0 ? 0 : gapValue, 0, 1));
                        }
                    }
                    break;
                case 2:
                    for (int columnIndex = 0; columnIndex < columnNum - 2; columnIndex++) {
                        if (columnIndex == columnNum - 3) {
                            tableRowInfo.addTableItemInfo(new TableItemInfo(columnIndex == 0 ? 0 : gapValue, 0, 3));
                        } else {
                            tableRowInfo.addTableItemInfo(new TableItemInfo(columnIndex == 0 ? 0 : gapValue, 0, 1));
                        }
                    }
                    break;
            }
        }

        scoreTableInfo = new TableInfo(tableRowInfos, columnNum);

    }

    public ComplexTableAdapter(TableInfo tableInfo) {
        super(tableInfo);
    }

    public ComplexTableAdapter() {
        this(scoreTableInfo);
    }

    public void setSpScoreValues(List<String> spScoreValues) {
        this.spScoreValues = spScoreValues;
    }

    public Map<Integer, Boolean> getSpScoreValuesChoose() {
        return spScoreValuesChoose;
    }

    public void setSpScoreValuesChoose(Map<Integer, Boolean> spScoreValuesChoose) {
        this.spScoreValuesChoose = spScoreValuesChoose;
    }

    public void setShowSP(boolean showSP) {
        this.showSP = showSP;
    }

    protected String[] getTitleKey() {
        return scoreKey;
    }

    private class ScoreHolder extends BaseTableHolder<String> implements View.OnClickListener {

        private BaseTextView btv_title;
        private BaseTextView btv_detail;

        public ScoreHolder(View itemView) {
            super(itemView);
            btv_title = (BaseTextView) itemView.findViewById(R.id.btv_title);
            btv_detail = (BaseTextView) itemView.findViewById(R.id.btv_detail);

            Context context = itemView.getContext();
            itemView.setBackgroundDrawable(StateListDrawableUtil.getDrawableStateListDRAW(context, new ViewState<Drawable>(new ColorDrawable(0xffcb2207), new ColorDrawable(0xffebefdd), android.R.attr.state_selected)));

            btv_title.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(0xffffffff, 0xff464646, android.R.attr.state_selected)));
            btv_detail.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(0xffffffff, 0xff848583, android.R.attr.state_selected)));

            if (!showSP) {
                btv_detail.setVisibility(View.GONE);
                itemView.setPadding(0, 40, 0, 40);
            }

        }

        private int position = RecyclerView.NO_POSITION;

        @Override
        public void fillView(String data, int position) {
            btv_title.setText(getTitleKey()[position]);
            btv_detail.setText(data);

            itemView.setSelected(spScoreValuesChoose.get(position) != null && spScoreValuesChoose.get(position));
            itemView.setOnClickListener(this);
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.getId()) {
                if (position != RecyclerView.NO_POSITION) {
                    boolean newState = !(spScoreValuesChoose.get(position) != null && spScoreValuesChoose.get(position));
                    itemView.setSelected(newState);
                    spScoreValuesChoose.put(position, newState);
                }
            }
        }

    }

    @Override
    public BaseTableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_complex, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseTableHolder holder, int position) {
        holder.fillView(spScoreValues.get(position), position);
    }

    @Override
    public int getItemCount() {
        return spScoreValues.size();
    }
}
