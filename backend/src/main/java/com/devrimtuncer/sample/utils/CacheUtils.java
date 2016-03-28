package com.devrimtuncer.sample.utils;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.util.logging.Level;

/**
 * Created by devrimtuncer on 28/03/16.
 */
public abstract class CacheUtils {

    public static void putIntoCache(String key, Object value) {
        System.out.println("invoked: putIntoCache (" + key + ")");
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        syncCache.put(key, value); // Populate cache.
    }

    public static Object getFromCache(String key) {
        System.out.println("invoked: getFromCache (" + key + ")");
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        return syncCache.get(key); // Read from cache.
    }
}
