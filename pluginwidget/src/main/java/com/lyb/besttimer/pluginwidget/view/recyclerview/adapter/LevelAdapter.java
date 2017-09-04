package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * level adapter
 * Created by linyibiao on 2017/9/1.
 */

public abstract class LevelAdapter<H extends LevelHolder> extends BaseAdapter<H> {

    private LevelAdapter<? extends LevelHolder> nextLevelAdapter;
    private List<LevelData> levelDatas = new ArrayList<>();
    private boolean singleCheck = true;

    public LevelAdapter(LevelAdapter<? extends LevelHolder> nextLevelAdapter, List<LevelData> levelDatas, boolean singleCheck) {
        this.nextLevelAdapter = nextLevelAdapter;
        this.levelDatas = levelDatas;
        this.singleCheck = singleCheck;
    }

    public List<LevelData> getLevelDatas() {
        return levelDatas;
    }

    public void setLevelDatas(List<LevelData> levelDatas) {
        this.levelDatas = levelDatas;
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        holder.fillview(this, nextLevelAdapter, levelDatas, position, singleCheck);
    }

    @Override
    public int getItemCount() {
        return levelDatas.size();
    }

    /**
     * mark to first show
     *
     * @return whether selected
     */
    public boolean hasSelectAndMark() {
        return hasSelectAndMark(levelDatas);
    }

    private boolean hasSelectAndMark(List<LevelData> levelDatas) {
        LevelData firstSelectData = null;
        for (LevelData levelData : levelDatas) {
            if (levelData.isLastLevel()) {
                if (levelData.isChecked()) {
                    if (firstSelectData == null) {
                        firstSelectData = levelData;
                    }
                }
            } else {
                List<LevelData> childLevelDatas = levelData.getNextLevelDatas();
                if (hasSelectAndMark(childLevelDatas)) {
                    if (firstSelectData == null) {
                        firstSelectData = levelData;
                    }
                }
            }
        }
        if (firstSelectData != null) {
            if (!firstSelectData.isLastLevel()) {
                for (LevelData levelData : levelDatas) {
                    levelData.setChecked(levelData == firstSelectData);
                }
            }
        }
        return firstSelectData != null;
    }

    public List<LevelData> getSelectDataList() {
        return getSelectDataList(levelDatas);
    }

    private List<LevelData> getSelectDataList(List<LevelData> levelDatas) {
        List<LevelData> selectDatas = new ArrayList<>();
        LevelData allLevelData = getAllLevelData(levelDatas);
        if (allLevelData != null) {
            List<LevelData> notAllLevelDatas = getNotAllLevelDatas(levelDatas);
            selectDatas.addAll(getSelectDataList(notAllLevelDatas));
        } else {
            for (LevelData currData : levelDatas) {
                if (!currData.isLastLevel()) {
                    List<LevelData> nextDatas = getSelectDataList(currData.getNextLevelDatas());
                    if (nextDatas.size() > 0) {
                        LevelData copyData = currData.simpleCopy();
                        copyData.getNextLevelDatas().addAll(nextDatas);
                        selectDatas.add(copyData);
                    }
                } else if (currData.isChecked()) {
                    selectDatas.add(currData.simpleCopy());
                }
            }
        }
        return selectDatas;
    }

    private List<LevelData> getNotAllLevelDatas(List<LevelData> levelDatas) {
        List<LevelData> notAllLevelDatas = new ArrayList<>();
        for (LevelData data : levelDatas) {
            if (!data.isAll()) {
                notAllLevelDatas.add(data);
            }
        }
        return notAllLevelDatas;
    }

    private LevelData getAllLevelData(List<LevelData> levelDatas) {
        LevelData allLevelData = null;
        for (LevelData data : levelDatas) {
            if (data.isAll()) {
                allLevelData = data;
                break;
            }
        }
        return allLevelData;
    }

    public static class LevelData {

        private List<LevelData> nextLevelDatas = new ArrayList<>();
        private Object data;
        private boolean checked = false;
        private boolean isAll = false;
        private boolean lastLevel = false;

        public LevelData(Object data, boolean checked, boolean isAll, boolean lastLevel) {
            this.data = data;
            this.checked = checked;
            this.isAll = isAll;
            this.lastLevel = lastLevel;
        }

        public LevelData simpleCopy() {
            return new LevelData(data, checked, isAll, lastLevel);
        }

        public List<LevelData> getNextLevelDatas() {
            return nextLevelDatas;
        }

        public Object getData() {
            return data;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isAll() {
            return isAll;
        }

        public void setAll(boolean all) {
            isAll = all;
        }

        public boolean isLastLevel() {
            return lastLevel;
        }

        public void setLastLevel(boolean lastLevel) {
            this.lastLevel = lastLevel;
        }
    }

}