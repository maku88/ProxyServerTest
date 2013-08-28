package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;
import org.apache.log4j.Logger;

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
    private Logger log = Logger.getLogger("TTL");
    private long ttl;

    public TTLCacheImpl(int capacity, long ttl){
        System.out.println("TTL init");
        this.cache = new BasicCache<String, CachedObject>(capacity);
        this.ttl = ttl;
    }

    public void addToCache(String tagID, String returnObject) {
        log.info("ADDING : " + tagID + " : " + returnObject);
        long timestamp = new Date().getTime();
        this.cache.put(tagID, new CachedObject(timestamp, returnObject, timestamp + ttl));
        log.info("CACHE SIZE : " + this.cache.size());
    }

    @Override
    public String getCachedValue(String tagID) {

        log.info("Getting from cache : " +tagID);

        CachedObject cachedObject  = this.cache.get(tagID);

        String response;


        if(cachedObject == null ) return null;

        if(new Date().getTime() < cachedObject.getTimeToLiveTimeStamp()) {
            response = cachedObject.getResponse();
        } else {
            response = null;
        }

        log.info("response from ttl cache : " + response);

        return response;
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
