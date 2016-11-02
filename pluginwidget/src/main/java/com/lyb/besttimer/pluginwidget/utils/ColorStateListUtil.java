package com.lyb.besttimer.pluginwidget.utils;

import android.content.res.ColorStateList;

import java.util.LinkedHashMap;
import java.util.Map;

public class ColorStateListUtil {

    public static ColorStateList getColorStateList(ColorState colorState) {
        return getColorStateList(new ColorState[]{colorState});
    }

    public static ColorStateList getColorStateList(ColorState... colorStates) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (ColorState colorState : colorStates) {
            if (colorState.getState() != 0) {
                map.put(colorState.getState(), colorState.getStateColor());
                if (colorState.isShowReverse()) {
                    map.put(-colorState.getState(), colorState.getStateReverseColor());
                }
            } else {
                map.put(0, colorState.getStateReverseColor());
            }
        }
        int[][] states = new int[map.size()][];
        int[] colors = new int[map.size()];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            states[index] = new int[]{entry.getKey()};
            colors[index] = entry.getValue();
            index++;
        }
        ColorStateList colorStateList = new ColorStateList(states, colors);
        return colorStateList;
    }

    public static class ColorState {

        //状态颜色
        private int stateColor;
        //反状态颜色
        private int stateReverseColor;
        //是否显示反状态颜色
        private boolean showReverse;
        //状态，0代表默认状态
        private int state;

        //设置默认状态
        public ColorState(int stateReverseColor) {
            this(0, stateReverseColor, true, 0);
        }

        //设置选中状态
        public ColorState(int stateColor, int state) {
            this(stateColor, 0, false, state);
        }

        //设置选中未选中状态
        public ColorState(int stateColor, int stateReverseColor, int state) {
            this(stateColor, stateReverseColor, true, state);
        }

        private ColorState(int stateColor, int stateReverseColor, boolean showReverse, int state) {
            this.stateColor = stateColor;
            this.stateReverseColor = stateReverseColor;
            this.showReverse = showReverse;
            this.state = state;
        }

        public int getStateColor() {
            return stateColor;
        }

        public int getStateReverseColor() {
            return stateReverseColor;
        }

        public boolean isShowReverse() {
            return showReverse;
        }

        public int getState() {
            return state;
        }
    }

}
