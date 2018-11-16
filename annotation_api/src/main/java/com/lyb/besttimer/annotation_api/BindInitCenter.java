package com.lyb.besttimer.annotation_api;

import android.content.Context;
import android.content.pm.PackageManager;

import com.alibaba.android.arouter.utils.ClassUtils;
import com.lyb.besttimer.annotation_bean.Constants;
import com.lyb.besttimer.annotation_bean.IAppInit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class BindInitCenter {

    public static void init(Context applicationContext) {
        try {
            Set<String> routerMap = ClassUtils.getFileNameByPackageName(applicationContext, Constants.packageName_init);
            for (String className : routerMap) {
                if (className.startsWith(Constants.packageName_init)) {
                    ((IAppInit<Context>) (Class.forName(className).getConstructor().newInstance())).init(applicationContext);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
