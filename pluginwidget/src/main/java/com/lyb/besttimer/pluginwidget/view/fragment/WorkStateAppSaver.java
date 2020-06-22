package com.lyb.besttimer.pluginwidget.view.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WorkStateAppSaver<KEY, RESULT, CALLBACK extends WorkStateAppSaver.Result<RESULT>> {

    private Set<KEY> requestingKeySet = new HashSet<>();//正在请求列表
    private Map<KEY, Set<CALLBACK>> KVMap = new HashMap<>(); //请求和回调map
    private Map<CALLBACK, KEY> VKMap = new HashMap<>();//回调和请求map
    private Map<Fragment, Set<CALLBACK>> FVMap = new HashMap<>();//fragment和请求map

    private Map<KEY, RESULT> resultMap = new HashMap<>();//请求和结果map

    private class WorkLifeCaller implements LifeCaller {

        private Fragment fragment;

        public WorkLifeCaller(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onDestroy() {
            Set<CALLBACK> callbacks = FVMap.remove(fragment);
            if (callbacks != null) {
                for (CALLBACK callback : callbacks) {
                    clearByValue(callback);
                }
            }
        }
    }

    public RESULT getResult(KEY key) {
        return resultMap.get(key);
    }

    private void setResult(KEY key, RESULT result) {
        if (result != null) {
            resultMap.put(key, result);
        } else {
            resultMap.remove(key);
        }
    }

    public void cleanAllRequestAndResult() {
        requestingKeySet.clear();
        resultMap.clear();
    }

    public void cleanAllResult() {
        resultMap.clear();
    }

    /**
     * @param fragmentManager fragment管理器，是否有值决定是否加入生命周期
     * @param key             键
     * @param worker          工作类
     * @param callback        回调类
     */

    public synchronized void workWithLife(FragmentManager fragmentManager, KEY key, Worker worker, CALLBACK callback) {
        workWithLife(fragmentManager, key, false, worker, callback);
    }

    /**
     * @param fragmentManager fragment管理器，是否有值决定是否加入生命周期
     * @param key             键
     * @param worker          工作类
     * @param callback        回调类
     */

    public synchronized void workWithLife(FragmentManager fragmentManager, KEY key, boolean noCache, Worker worker, CALLBACK callback) {

        if (noCache) {
            removeResult(key);
        }

        RESULT result = getResult(key);
        if (result != null) {
            callback.result(result);
        } else {

            //消除历史信息
            KEY preKey = VKMap.get(callback);
            if (preKey != null) {
                VKMap.remove(callback);
                Set<CALLBACK> preCallbacks = KVMap.get(preKey);
                if (preCallbacks != null) {
                    preCallbacks.remove(callback);
                }
            }

            //更新最新信息
            Set<CALLBACK> currCallbacks = KVMap.get(key);
            if (currCallbacks == null) {
                currCallbacks = new HashSet<>();
                KVMap.put(key, currCallbacks);
            }
            currCallbacks.add(callback);
            VKMap.put(callback, key);

            //设置具有生命周期的回调
            if (fragmentManager != null) {
                WorkStateFragment fragment = WorkStateFragment.addToManager(fragmentManager);
                fragment.setLifeCaller(new WorkLifeCaller(fragment));
                Set<CALLBACK> callbacks = FVMap.get(fragment);
                if (callbacks == null) {
                    callbacks = new HashSet<>();
                    FVMap.put(fragment, callbacks);
                }
                callbacks.add(callback);
            }

            if (!requestingKeySet.contains(key)) {
                requestingKeySet.add(key);
                worker.work();
            }

        }
    }

    public synchronized void error(KEY key) {
        error(key, null);
    }

    public synchronized void error(KEY key, Object error) {
        result(key, null, error);
    }

    /**
     * 删除指定请求
     *
     * @param key 键
     */
    public synchronized void removeRequestAndResult(KEY key) {
        requestingKeySet.remove(key);
        removeResult(key);
    }

    /**
     * 删除指定缓存
     *
     * @param key 键
     */
    public synchronized void removeResult(KEY key) {
        setResult(key, null);
    }

    /**
     * 清除所有缓存
     */
    public synchronized void cleanAllCache() {
        cleanAllResult();
    }

    /**
     * 清除所有回调
     */
    public synchronized void cleanAllCallback() {
        requestingKeySet.clear();
        KVMap.clear();
        VKMap.clear();
        FVMap.clear();
    }

    public synchronized void result(KEY key, RESULT result) {
        result(key, result, null);
    }

    /**
     * 结果回调
     *
     * @param key    键
     * @param result 值，结果
     */
    public synchronized void result(KEY key, RESULT result, Object error) {

        requestingKeySet.remove(key);
        setResult(key, result);

        Set<CALLBACK> callbacks = KVMap.get(key);
        if (callbacks != null) {
            for (CALLBACK callback : callbacks) {
                if (result != null) {
                    callback.result(result);
                } else {
                    callback.error(error);
                }
            }
        }

        clearByKey(key);

    }

    private void clearByKey(KEY key) {
        Set<CALLBACK> preCallbacks = KVMap.remove(key);
        if (preCallbacks != null) {
            for (CALLBACK callback : preCallbacks) {
                VKMap.remove(callback);
            }
        }
    }

    private void clearByValue(CALLBACK callback) {
        KEY preKey = VKMap.remove(callback);
        if (preKey != null) {
            Set<CALLBACK> callbacks = KVMap.get(preKey);
            if (callbacks != null) {
                Iterator<CALLBACK> iterator = callbacks.iterator();
                while (iterator.hasNext()) {
                    CALLBACK currCallback = iterator.next();
                    if (currCallback == callback) {
                        iterator.remove();
                    }
                }
            }
            if (callbacks == null || callbacks.size() == 0) {
                KVMap.remove(preKey);
            }
        }
    }

    public interface Worker {
        void work();
    }

    public interface Result<RESULT> {
        void result(RESULT result);

        void error(Object error);
    }

}
