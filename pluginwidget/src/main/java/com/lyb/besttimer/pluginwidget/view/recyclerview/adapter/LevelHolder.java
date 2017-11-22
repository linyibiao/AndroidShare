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
            nextLevelAdapter.setLevelDatas(currData.getNextLevelDatas());
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
        if (currData.isLastLevel()) {//最后一个等级
            if (singleCheck) {
                //如果是最后一个等级，单选模式，并且点击的元素未选中，那么选中他，并且取消其他处于最后一个等级的元素的选中状态
                if (!currData.isChecked()) {
                    currData.setChecked(true);
                    LevelAdapter<? extends LevelHolder> topLevelAdapter = currLevelAdapter;
                    while (topLevelAdapter.getPreLevelAdapter() != null) {
                        topLevelAdapter = topLevelAdapter.getPreLevelAdapter();
                    }
                    topLevelAdapter.clearAllLastLevelCheckedStatusExcept(currData);
                }
            } else {
                LevelAdapter.LevelData allLevelData = getAllLevelData();
                if (allLevelData == null) {
                    //如果是最后一个等级，多选模式，并且没有全部元素，那么切换他的选中状态
                    currData.setChecked(!currData.isChecked());
                } else {
                    if (allLevelData == currData) {
                        //如果是最后一个等级，多选模式，并且点击的是全部元素，那么切换全部的选中状态，并且更新和全部同等级的元素为同状态
                        currData.setChecked(!currData.isChecked());
                        for (LevelAdapter.LevelData otherData : levelDatas) {
                            if (otherData != allLevelData) {
                                otherData.setChecked(currData.isChecked());
                            }
                        }
                    } else if (allLevelData.isChecked()) {
                        //如果是最后一个等级，多选模式，并且点击的不是全部元素，但是全部元素为选中状态，那么选中他，并且更新和他同等级的元素状态为false，并且检查此等级的元素是不是已经全选了，如果是那就选中全部元素
                        allLevelData.setChecked(false);
                        currData.setChecked(true);
                        for (LevelAdapter.LevelData otherData : levelDatas) {
                            if (otherData != allLevelData) {
                                otherData.setChecked(otherData == currData);
                            }
                        }
                        boolean allChecked = true;
                        for (LevelAdapter.LevelData otherData : levelDatas) {
                            if (otherData != allLevelData) {
                                if (!otherData.isChecked()) {
                                    allChecked = false;
                                    break;
                                }
                            }
                        }
                        if (allChecked) {
                            allLevelData.setChecked(true);
                        }
                    } else {
                        //如果是最后一个等级，多选模式，并且点击的不是全部元素，但是全部元素为不选中状态，那么切换他的选中状态，并且检查此等级的元素是不是已经全选了，如果是那就选中全部元素
                        currData.setChecked(!currData.isChecked());
                        boolean allChecked = true;
                        for (LevelAdapter.LevelData otherData : levelDatas) {
                            if (otherData != allLevelData) {
                                if (!otherData.isChecked()) {
                                    allChecked = false;
                                    break;
                                }
                            }
                        }
                        if (allChecked) {
                            allLevelData.setChecked(true);
                        }
                    }
                }
            }
        } else {//不是最后一个等级
            //如果不是最后一个等级，并且点击的元素未选中，那么选中他，并且取消和他同等级的元素的选中状态
            if (!currData.isChecked()) {
                currData.setChecked(!currData.isChecked());
                for (LevelAdapter.LevelData otherData : levelDatas) {
                    if (otherData != currData) {
                        otherData.setChecked(false);
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
