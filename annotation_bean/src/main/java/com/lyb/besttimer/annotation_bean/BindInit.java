package com.lyb.besttimer.annotation_bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linyibiao
 * @since 2018/5/15 下午10:04
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface BindInit {
}
