package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import android.view.View;

import java.util.List;

/**
 * level holder
 * Created by linyibiao on 2017/9/1.
 */
public class LevelHolder extends BaseHolder<LevelAdapter.LevelData> implements View.OnClickListener {

    protected LevelAdapter<? extends LevelHolder> preLevelAdapter;//前面的levelAdapter
    protected LevelAdapter<? extends LevelHolder> currLevelAdapter;//当前的levelAdapter
    protected LevelAdapter<? extends LevelHolder> nextLevelAdapter;//后面的levelAdapter
    protected List<LevelAdapter.LevelData> levelDatas;
    protected int position;
    protected boolean singleCheck = true;

    private AdapterPosClick<LevelAdapter.LevelData> adapterPosClick;

    public LevelHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
    }

    protected void statusChange(boolean isChecked) {

    }

    public void fillview(LevelAdapter<? extends LevelHolder> preLevelAdapter, LevelAdapter<? extends LevelHolder> currLevelAdapter, LevelAdapter<? extends LevelHolder> nextLevelAdapter,
                         List<LevelAdapter.LevelData> levelDatas, int position, boolean singleCheck,
                         AdapterPosClick<LevelAdapter.LevelData> adapterPosClick) {
        this.fillView(levelDatas.get(position), position);
        this.preLevelAdapter = preLevelAdapter;
        this.currLevelAdapter = currLevelAdapter;
        this.nextLevelAdapter = nextLevelAdapter;
        this.levelDatas = levelDatas;
        this.position = position;
        this.singleCheck = singleCheck;
        this.adapterPosClick = adapterPosClick;

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

        //将数据变动延伸到下一个adapter
        if (nextLevelAdapter != null && currData.isChecked()) {
            nextLevelAdapter.getLevelDatas().clear();
            nextLevelAdapter.getLevelDatas().addAll(currData.getNextLevelDatas());
            nextLevelAdapter.dataCheck();
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
        boolean canChange = true;
        if (!currData.isLastLevel() && currData.isChecked()) {
            canChange = false;
        }
        if (singleCheck && currData.isLastLevel() && currData.isChecked()) {
            canChange = false;
        }
        if (canChange) {
            currData.setChecked(!currData.isChecked());
        }
        if (singleCheck) {
            if (currData.isChecked()) {
                if (currData.isLastLevel()) {
                    LevelAdapter<? extends LevelHolder> topLevelAdapter = currLevelAdapter;
                    while (topLevelAdapter.getPreLevelAdapter() != null) {
                        topLevelAdapter = topLevelAdapter.getPreLevelAdapter();
                    }
                    topLevelAdapter.clearAllLastLevelCheckedStatusExcept(currData);
                } else {
                    for (LevelAdapter.LevelData otherData : levelDatas) {
                        if (otherData != currData) {
                            otherData.setChecked(false);
                        }
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
        if (adapterPosClick != null) {
            adapterPosClick.onClick(data, position);
        }
        currLevelAdapter.notifyDataSetChanged();
    }
}
