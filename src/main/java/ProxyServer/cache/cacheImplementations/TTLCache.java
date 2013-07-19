package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 23:16
 * To change this template use File | Settings | File Templates.
 */
public class TTLCache extends BasicCache implements Cache {

    private static TTLCache instance ;

    private TTLCache(){}

    public static TTLCache getInstance(){
        if(instance == null ) instance = new TTLCache();
        return instance;
    }


    public void addToCache(String tagID, String returnObject) {
        System.out.println("ADDING TO CACHE : " + tagID + " : " + returnObject);
        this.getCache().put(tagID, new CachedObject(new Date().getTime(), returnObject, new Date().getTime() + SysConfig.timeToLiveParam));

    }

    @Override
    public String getCachedValue(String tagID) {

        System.out.println("Getting from cache : " +tagID);

        CachedObject cachedObject  = super.getCache().get(tagID);

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
