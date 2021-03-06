package com.ivyiot.appsdk;

import android.app.Application;
import java.util.HashMap;
import java.util.Map;

public class IvyApplication extends Application {
    private String TAG = "IvyApplication";

    /**
     * 全局暂存
     */
    private Map<String, Object> cache;
    /**
     * application 实例
     */
    private static IvyApplication instance;

    public static IvyApplication getInstance() {
        if (null == instance) {
            instance = new IvyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cache = new HashMap<>();


    }

    /**
     * 全局变量存储
     *
     * @param key   key
     * @param value value
     */
    public void putCache(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * 取出对象，并从cache中remove
     *
     * @param key 对象对应的key
     * @return Object
     */
    public Object getCache(String key) {
        return cache.get(key);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        for(String key : cache.keySet()){
            cache.remove(key);
        }
    }
}
