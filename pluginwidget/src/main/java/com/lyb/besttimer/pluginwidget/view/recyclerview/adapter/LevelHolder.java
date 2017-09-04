package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import android.view.View;

import java.util.List;

/**
 * level holder
 * Created by linyibiao on 2017/9/1.
 */
public class LevelHolder extends BaseHolder<LevelAdapter.LevelData> implements View.OnClickListener {

    protected LevelAdapter<? extends LevelHolder> currLevelAdapter;
    protected LevelAdapter<? extends LevelHolder> nextLevelAdapter;
    protected List<LevelAdapter.LevelData> levelDatas;
    protected int position;
    protected boolean singleCheck = true;

    public LevelHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
    }

    protected void statusChange(boolean isChecked) {

    }

    public void fillview(LevelAdapter<? extends LevelHolder> currLevelAdapter, LevelAdapter<? extends LevelHolder> nextLevelAdapter, List<LevelAdapter.LevelData> levelDatas, int position, boolean singleCheck) {
        this.fillView(levelDatas.get(position), position);
        this.currLevelAdapter = currLevelAdapter;
        this.nextLevelAdapter = nextLevelAdapter;
        this.levelDatas = levelDatas;
        this.position = position;
        this.singleCheck = singleCheck;

        LevelAdapter.LevelData currData = levelDatas.get(position);

        LevelAdapter.LevelData allLevelData = getAllLevelData();
        boolean selectToShow;
        if (allLevelData != null) {
            if (allLevelData == currData) {
                selectToShow = currData.isChecked();
            } else {
                if (allLevelData.isChecked()) {
                    selectToShow = false;
                } else {
                    selectToShow = currData.isChecked();
                }
            }
        } else {
            selectToShow = currData.isChecked();
        }
        itemView.setSelected(selectToShow);
        statusChange(selectToShow);

        if (nextLevelAdapter != null && currData.isChecked()) {
            nextLevelAdapter.setLevelDatas(currData.getNextLevelDatas());
            nextLevelAdapter.notifyDataSetChanged();
        }
    }

    private LevelAdapter.LevelData getAllLevelData() {
        LevelAdapter.LevelData allLevelData = null;
        for (LevelAdapter.LevelData data : levelDatas) {
            if (data.isAll()) {
                allLevelData = data;
                break;
            }
        }
        return allLevelData;
    }

    @Override
    public void onClick(View view) {
        LevelAdapter.LevelData currData = levelDatas.get(position);
        currData.setChecked(!currData.isChecked());
        if (singleCheck) {
            if (currData.isChecked()) {
                for (LevelAdapter.LevelData otherData : levelDatas) {
                    if (otherData != currData) {
                        otherData.setChecked(false);
                    }
                }
            }
        } else {
            LevelAdapter.LevelData allLevelData = getAllLevelData();
            if (allLevelData != null) {
                if (allLevelData == currData) {
                    for (LevelAdapter.LevelData otherData : levelDatas) {
                        if (otherData != allLevelData) {
                            otherData.setChecked(currData.isChecked());
                        }
                    }
                } else if (allLevelData.isChecked()) {
                    allLevelData.setChecked(false);
                    for (LevelAdapter.LevelData otherData : levelDatas) {
                        if (otherData != allLevelData) {
                            otherData.setChecked(!otherData.isChecked());
                        }
                    }
                }
            }
        }
        currLevelAdapter.notifyDataSetChanged();
    }
}
