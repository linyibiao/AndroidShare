package com.lyb.besttimer.network.gson;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * gson helper
 * Created by linyibiao on 2016/8/24.
 */
public class GsonHelper {

    /**
     * get instance
     *
     * @param json     json
     * @param classOfT T.class
     * @param <T>      T
     * @return one T
     */
    public static <T> T getInstance(String json, Class<T> classOfT) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get instances
     *
     * @param json    json
     * @param typeOfT new TypeToken<List<T>>() {}.getType()
     * @param <T>     T
     * @return list of T
     */
    public static <T> List<T> getInstances(String json, Type typeOfT) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
