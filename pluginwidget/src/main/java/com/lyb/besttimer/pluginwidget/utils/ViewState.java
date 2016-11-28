package com.lyb.besttimer.pluginwidget.utils;

/**
 * view state
 * Created by linyibiao on 2016/11/28.
 */

public class ViewState<T> {

    //state value
    private T stateValue;
    //state reverse value
    private T stateReverseValue;
    //whether show reverse
    private boolean showReverse;
    //state,0 for default
    private int state;

    //set for default state
    public ViewState(T stateReverseValue) {
        this(null, stateReverseValue, true, 0);
    }

    //set for checked state
    public ViewState(T stateValue, int state) {
        this(stateValue, null, false, state);
    }

    //set for checked and unchecked states
    public ViewState(T stateValue, T stateReverseValue, int state) {
        this(stateValue, stateReverseValue, true, state);
    }

    private ViewState(T stateValue, T stateReverseValue, boolean showReverse, int state) {
        this.stateValue = stateValue;
        this.stateReverseValue = stateReverseValue;
        this.showReverse = showReverse;
        this.state = state;
    }

    public T getStateValue() {
        return stateValue;
    }

    public T getStateReverseValue() {
        return stateReverseValue;
    }

    public boolean isShowReverse() {
        return showReverse;
    }

    public int getState() {
        return state;
    }

}
