package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 23:17
 * To change this template use File | Settings | File Templates.
 */
public class LRUCacheImpl implements Cache {


    private class LRUCache < K, V > extends LinkedHashMap< K, V > {

        private int capacity; // Maximum number of items in the cache.

        public LRUCache(int capacity) {
            super(capacity+1, 1.0f, true); // Pass 'true' for accessOrder.
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry entry) {
            return (size() > this.capacity);
        }
    }

    private Map<String, CachedObject> cache ;
    private static LRUCacheImpl instance ;
    private Logger log = Logger.getLogger("LRU");

    private LRUCacheImpl(int capacity){
        this.cache = new LRUCache<String, CachedObject>(capacity);
    }

    public static LRUCacheImpl getInstance(int capacity){
        if(instance == null ) instance = new LRUCacheImpl(capacity);
        return instance;
    }

    @Override
    public void addToCache(String tagID, String returnObject) {
        log.info("ADDING : " + tagID + " : " + returnObject);
        this.cache.put(tagID, new CachedObject(new Date().getTime(), returnObject, SysConfig.timeToLiveParam));
        log.info("CACHE SIZE : " + this.cache.size());
    }

    @Override
    public String getCachedValue(String tagID) {
        CachedObject obj = this.cache.get(tagID);
        if(obj == null ) return null;
        else return obj.getResponse();
    }

    public void printCache() {
        for(Map.Entry<String , CachedObject> entry : this.cache.entrySet()) {
            log.info(entry.getKey() + " " + entry.getValue());
        }
    }
    public void clear() {
        cache.clear();
    }

}
