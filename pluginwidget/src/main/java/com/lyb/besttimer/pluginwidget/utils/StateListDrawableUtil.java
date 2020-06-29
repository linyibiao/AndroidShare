package com.lyb.besttimer.pluginwidget.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * state list drawble util
 * Created by linyibiao on 2016/11/28.
 */

public class StateListDrawableUtil {

    public static StateListDrawable getDrawableStateListRES(Context context, ViewState<Integer> drawableState) {
        return getDrawableStateListRES(context, new ViewState[]{drawableState});
    }

    public static StateListDrawable getDrawableStateListRES(Context context, ViewState<Integer>... drawableStates) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (ViewState<Integer> drawableState : drawableStates) {
            if (drawableState.getState() != 0) {
                map.put(drawableState.getState(), drawableState.getStateValue());
                if (drawableState.isShowReverse()) {
                    map.put(-drawableState.getState(), drawableState.getStateReverseValue());
                }
            } else {
                map.put(0, drawableState.getStateReverseValue());
            }
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getKey() == 0) {
                stateListDrawable.addState(new int[]{}, ContextCompat.getDrawable(context, entry.getValue()));
            } else {
                stateListDrawable.addState(new int[]{entry.getKey()}, ContextCompat.getDrawable(context, entry.getValue()));
            }
        }
        return stateListDrawable;
    }

    public static StateListDrawable getDrawableStateListDRAW(Context context, ViewState<Drawable> drawableState) {
        return getDrawableStateListDRAW(context, new ViewState[]{drawableState});
    }

    public static StateListDrawable getDrawableStateListDRAW(Context context, ViewState<Drawable>... drawableStates) {
        Map<Integer, Drawable> map = new LinkedHashMap<>();
        for (ViewState<Drawable> drawableState : drawableStates) {
            if (drawableState.getState() != 0) {
                map.put(drawableState.getState(), drawableState.getStateValue());
                if (drawableState.isShowReverse()) {
                    map.put(-drawableState.getState(), drawableState.getStateReverseValue());
                }
            } else {
                map.put(0, drawableState.getStateReverseValue());
            }
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (Map.Entry<Integer, Drawable> entry : map.entrySet()) {
            if (entry.getKey() == 0) {
                stateListDrawable.addState(new int[]{}, entry.getValue());
            } else {
                stateListDrawable.addState(new int[]{entry.getKey()}, entry.getValue());
            }
        }
        return stateListDrawable;
    }

}
