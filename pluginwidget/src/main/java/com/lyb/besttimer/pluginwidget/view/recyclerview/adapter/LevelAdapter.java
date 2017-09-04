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

    public void firstShowSetting() {
        LevelData firstChildData = null;
        for (LevelData levelData : levelDatas) {
            boolean hasSelect = hasSelect(levelData);
            if (hasSelect) {
                if (firstChildData == null) {
                    firstChildData = levelData;
                }
            }
        }
        if (firstChildData != null) {
            for (LevelData levelData : levelDatas) {
                levelData.setChecked(levelData == firstChildData);
            }
        }
    }

    private boolean hasSelect(LevelData levelData) {
        if (levelData.isLastLevel()) {
            if (levelData.isChecked()) {
                return true;
            } else {
                return false;
            }
        } else {
            LevelData firstChildData = null;
            for (LevelData childData : levelData.getNextLevelDatas()) {
                if (hasSelect(childData)) {
                    if (firstChildData == null) {
                        firstChildData = childData;
                    }
                }
            }
            if (firstChildData != null) {
                firstChildData.setChecked(true);
                return true;
            }
            return false;
        }
    }

    public List<LevelData> getSelectData() {
        return getSelectData(levelDatas);
    }

    private List<LevelData> getSelectData(List<LevelData> levelDatas) {
        List<LevelData> selectDatas = new ArrayList<>();
        LevelData allLevelData = getAllLevelData(levelDatas);
        if (allLevelData != null) {
            List<LevelData> notAllLevelDatas = getNotAllLevelDatas(levelDatas);
            selectDatas.addAll(getSelectData(notAllLevelDatas));
        } else {
            for (LevelData currData : levelDatas) {
                if (!currData.isLastLevel() || currData.isChecked()) {
                    List<LevelData> nextDatas = getSelectData(currData.getNextLevelDatas());
                    if (nextDatas.size() > 0 || currData.getNextLevelDatas().size() == 0) {
                        LevelData copyData = currData.simpleCopy();
                        copyData.getNextLevelDatas().addAll(nextDatas);
                        selectDatas.add(copyData);
                    }
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
