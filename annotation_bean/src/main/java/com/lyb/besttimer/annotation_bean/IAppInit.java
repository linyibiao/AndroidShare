package com.lyb.besttimer.annotation_bean;

/**
 * @author linyibiao
 * @since 2018/5/15 下午10:06
 */
public interface IAppInit<T> {

    void init(T applicationContext);

}
