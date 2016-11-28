package com.lyb.besttimer.pluginwidget.utils;

import android.content.res.ColorStateList;

import java.util.LinkedHashMap;
import java.util.Map;

public class ColorStateListUtil {

    public static ColorStateList getColorStateList(ViewState<Integer> colorState) {
        return getColorStateList(new ViewState[]{colorState});
    }

    public static ColorStateList getColorStateList(ViewState<Integer>... colorStates) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (ViewState<Integer> colorState : colorStates) {
            if (colorState.getState() != 0) {
                map.put(colorState.getState(), colorState.getStateValue());
                if (colorState.isShowReverse()) {
                    map.put(-colorState.getState(), colorState.getStateReverseValue());
                }
            } else {
                map.put(0, colorState.getStateReverseValue());
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

}
