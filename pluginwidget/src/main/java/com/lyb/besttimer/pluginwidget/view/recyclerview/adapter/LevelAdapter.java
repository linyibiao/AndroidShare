package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * level adapter
 * Created by linyibiao on 2017/9/1.
 */
public abstract class LevelAdapter<H extends LevelHolder> extends BaseAdapter<H> {

    private LevelAdapter<? extends LevelHolder> preLevelAdapter;//前面的levelAdapter
    private LevelAdapter<? extends LevelHolder> nextLevelAdapter;//后面的levelAdapter
    private List<LevelData> levelDatas = new ArrayList<>();
    private boolean singleCheck = true;//只有在最后一个等级，singleCheck = false;才有效

    private AdapterPosClick<LevelData> adapterPosClick;//元素点击回调
    private AdapterDataCheck<LevelData> adapterDataCheck;//上层或以上的adapter的数据变动时反馈到当前adapter的数据

    public LevelAdapter(LevelAdapter<? extends LevelHolder> nextLevelAdapter, List<LevelData> levelDatas, boolean singleCheck) {
        this(nextLevelAdapter, levelDatas, singleCheck, null, null);
    }

    /**
     * 层级adapter的构造信息
     *
     * @param nextLevelAdapter 指定下一层级adapter
     * @param levelDatas       当前层级的数据列表
     * @param singleCheck      是不是单选，如果为false，也只能在最后一个层级中有效。具体是不是最后一个层级，由{@link #levelDatas}中的元素的属性{@link LevelData#lastLevel}决定
     * @param adapterPosClick  当前adapter中元素的点击回调
     * @param adapterDataCheck 父级或以上的adapter中元素的点击回调
     */
    public LevelAdapter(LevelAdapter<? extends LevelHolder> nextLevelAdapter, List<LevelData> levelDatas, boolean singleCheck, AdapterPosClick<LevelData> adapterPosClick, AdapterDataCheck<LevelData> adapterDataCheck) {
        this.nextLevelAdapter = nextLevelAdapter;
        if (nextLevelAdapter != null) {
            nextLevelAdapter.setPreLevelAdapter(this);
        }
        this.levelDatas = levelDatas;
        this.singleCheck = singleCheck;
        this.adapterPosClick = adapterPosClick;
        this.adapterDataCheck = adapterDataCheck;
    }

    public LevelAdapter<? extends LevelHolder> getPreLevelAdapter() {
        return preLevelAdapter;
    }

    private void setPreLevelAdapter(LevelAdapter<? extends LevelHolder> preLevelAdapter) {
        this.preLevelAdapter = preLevelAdapter;
    }

    public boolean isSingleCheck() {
        return singleCheck;
    }

    public List<LevelData> getLevelDatas() {
        return levelDatas;
    }

    public void dataCheck() {
        if (adapterDataCheck != null) {
            adapterDataCheck.dataCheck(levelDatas);
        }
    }

    public void setLevelDatas(List<LevelData> levelDatas) {
        this.levelDatas = levelDatas;
        if (preLevelAdapter != null && preLevelAdapter.isSingleCheck()) {
            List<LevelData> levelDataList = preLevelAdapter.getCurrSelectDataList();
            if (levelDataList.size() == 1 && levelDataList.get(0).getNextLevelDatas() != levelDatas) {
                levelDataList.get(0).getNextLevelDatas().clear();
                levelDataList.get(0).getNextLevelDatas().addAll(levelDatas);
            }
        }
    }

    /**
     * 获取当前选中的数据列表
     */
    public List<LevelData> getCurrSelectDataList() {
        List<LevelData> levelDataList = new ArrayList<>();
        for (LevelData levelData : levelDatas) {
            if (levelData.isChecked()) {
                levelDataList.add(levelData);
            }
        }
        return levelDataList;
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        holder.fillview(preLevelAdapter, this, nextLevelAdapter, levelDatas, position, singleCheck, adapterPosClick);
    }

    @Override
    public int getItemCount() {
        return levelDatas.size();
    }

    /**
     * 取消最后一个等级的选中状态，除了参数中的元素
     *
     * @param exceptLevelData 不会被取消选中的元素
     */
    public void clearAllLastLevelCheckedStatusExcept(LevelData exceptLevelData) {
        clearAllLastLevelCheckedStatusExcept(levelDatas, exceptLevelData);
    }

    public static void clearAllLastLevelCheckedStatusExcept(List<LevelData> levelDatas, LevelData exceptLevelData) {
        for (LevelData levelData : levelDatas) {
            if (levelData.isLastLevel()) {
                if (levelData != exceptLevelData) {
                    levelData.setChecked(false);
                }
            } else {
                clearAllLastLevelCheckedStatusExcept(levelData.getNextLevelDatas(), exceptLevelData);
            }
        }
    }

    /**
     * 是否选择并且标记，用于初始化那些已经选择的数据
     *
     * @return whether selected
     */
    public boolean hasSelectAndMark() {
        return hasSelectAndMark(levelDatas);
    }

    /**
     * 是否有选中的元素，对于不是最后一个等级的元素列表，总是选中最前面被选中的元素，其他的元素都是未选中
     *
     * @param levelDatas 待检查的元素列表
     * @return 是否有在最后一个等级选中的元素
     */
    public static boolean hasSelectAndMark(List<LevelData> levelDatas) {
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

    /**
     * 获取选中状态的层级列表
     *
     * @param levelDatas 检查选中的层级列表
     * @return 选中状态的层级列表
     */
    public static List<LevelData> getSelectDataList(List<LevelData> levelDatas) {
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

    private static List<LevelData> getNotAllLevelDatas(List<LevelData> levelDatas) {
        List<LevelData> notAllLevelDatas = new ArrayList<>();
        for (LevelData data : levelDatas) {
            if (!data.isAll()) {
                notAllLevelDatas.add(data);
            }
        }
        return notAllLevelDatas;
    }

    private static LevelData getAllLevelData(List<LevelData> levelDatas) {
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

        /**
         * 级别数据
         *
         * @param data      包含的内容
         * @param checked   是否选中
         * @param isAll     是否具有选中全部的属性
         * @param lastLevel 是不是最后一个级别的数据（这个条件用户自己定，如果是最后一个等级的adapter，那么就是true，其他都是false）
         */
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
