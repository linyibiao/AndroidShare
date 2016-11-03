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

        //state color
        private int stateColor;
        //state reverse color
        private int stateReverseColor;
        //whether show reverse
        private boolean showReverse;
        //state,0 for default
        private int state;

        //set for default state
        public ColorState(int stateReverseColor) {
            this(0, stateReverseColor, true, 0);
        }

        //set for checked state
        public ColorState(int stateColor, int state) {
            this(stateColor, 0, false, state);
        }

        //set for checked and unchecked states
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
