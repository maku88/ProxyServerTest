package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 23:16
 * To change this template use File | Settings | File Templates.
 */
public class TTLCacheImpl implements Cache {

    private Map<String, CachedObject> cache;
    private static TTLCacheImpl instance ;

    private TTLCacheImpl(int capacity){
        this.cache = new BasicCache<String, CachedObject>(capacity);
    }

    public static TTLCacheImpl getInstance(int capacity){
        if(instance == null ) instance = new TTLCacheImpl(capacity);
        return instance;
    }


    public void addToCache(String tagID, String returnObject) {
        System.out.println("ADDING TO CACHE : " + tagID + " : " + returnObject);
        this.cache.put(tagID, new CachedObject(new Date().getTime(), returnObject, new Date().getTime() + SysConfig.timeToLiveParam));

    }

    @Override
    public String getCachedValue(String tagID) {

        System.out.println("Getting from cache : " +tagID);

        CachedObject cachedObject  = this.cache.get(tagID);

        String response;


        if(cachedObject == null ) return null;

        if(new Date().getTime() < cachedObject.getTimeToLiveTimeStamp()) {
            response = cachedObject.getResponse();
        } else {
            response = null;
        }

        System.out.println("response from ttl cache : " + response);

        return response;
    }








}
