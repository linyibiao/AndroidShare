package com.lyb.besttimer.annotation_api;

import com.lyb.besttimer.annotation_bean.Constants;
import com.lyb.besttimer.annotation_bean.IGetClass;

import java.util.HashMap;
import java.util.Map;

public class BindClassCenter {

    private static Map<String, Class<?>> bindMap = new HashMap<>();

    public static Class<?> findClass(String path) {
        Class<?> aClass = bindMap.get(path);
        if (aClass == null) {
            String simpleName = "BindClass" + path.replaceAll("/", "\\$\\$");
            try {
                IGetClass iGetClass = (IGetClass) Class.forName(Constants.packageName_clazz + "." + simpleName).newInstance();
                bindMap.put(path, aClass = iGetClass.getTargetClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return aClass;
    }

    public static <T> Class<T> findClass_T(String path) {
        return (Class<T>) findClass(path);
    }

}
