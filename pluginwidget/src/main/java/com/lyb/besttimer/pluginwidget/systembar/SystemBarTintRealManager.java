package com.lyb.besttimer.pluginwidget.systembar;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**system bar tint real manager
 * Created by linyibiao on 2016/11/8.
 */

public class SystemBarTintRealManager {

    private SystemBarTintRealManager(){

    }

    private static SystemBarTintRealManager systemBarTintRealManager=new SystemBarTintRealManager();

private Map<WeakReference<Activity>,SystemBarTintManager>systemBarTintManagerMap=new HashMap<>();

    public static void setup(Activity activity){
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
//        systemBarTintManagerMap
        // set a custom tint color for all system bars
//        tintManager.setTintColor(Color.parseColor("#999000FF"));
        // set a custom navigation bar resource
//        tintManager.setNavigationBarTintResource(R.mipmap.refresh_arrow);
        // set a custom status bar drawable
//        tintManager.setStatusBarTintResource(R.mipmap.ic_launcher);
    }

//    private boolean hasSetup(Activity activity){
//        for ()
//    }

}
