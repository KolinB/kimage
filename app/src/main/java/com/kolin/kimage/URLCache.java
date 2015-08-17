package com.kolin.kimage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kolin on 25/05/15.
 */
public class URLCache {

    private static URLCache mInstance;
    private Map<String, String> cache = new HashMap<String, String>();

    public static synchronized URLCache getInstance() {
        if (mInstance == null) {
            mInstance = new URLCache();
        }
        return mInstance;
    }

    public URLCache() {

    }

    public void addToCache(String key, String value) {
        cache.put(key, value);
    }

    public boolean isInCache(String key) {
        return cache.containsKey(key);
    }

    public String get(String key) {
        return cache.get(key);
    }

}
