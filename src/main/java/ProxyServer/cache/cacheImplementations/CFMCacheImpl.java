package ProxyServer.cache.cacheImplementations;


import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;
import ProxyServer.methods.GetRequest;
import ProxyServer.request.Request;
import ProxyServer.request.RequestHeader;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 22:15
 *
 *
 *
 * Cache sprawdzający przy każdym odczycie czy zostały zmienione dane na znaczniku
 * jeżeli otrzyma od serwera odpowiedź 303 (NOT MODIFIED) to zwraca dane z cache jeżeli nie to zwraca to co otrzymał z serwera
 * chyba że dostał null wtedy zwraca też to co ma w cache
 *
 */
public class CFMCacheImpl implements Cache{

    private Map<String, CachedObject> cache;
    private Logger log = Logger.getLogger("CFM");


    public CFMCacheImpl(int capacity){
        this.cache = new BasicCache<String, CachedObject>(capacity);
    }


    public void addToCache(String tagID, String returnObject) {
        log.info("ADDING : " + tagID + " : " + returnObject);
        this.cache.put(tagID,new CachedObject(new Date().getTime(),returnObject,SysConfig.timeToLiveParam));
        log.info("CACHE SIZE : " + this.cache.size());

    }


    public String getCachedValue(String tagID) {

        log.info("Getting from cache : " +tagID);

        CachedObject cachedObject  = this.cache.get(tagID);

        if(cachedObject == null ) return null;


        String rsp = checkForModification(tagID,cachedObject.getCacheTimeStamp());

        if("303".equals(rsp)) {
            return cachedObject.getResponse();
        }else if(rsp == null) {
            return cachedObject.getResponse();
        }else return rsp;
    }

    private String checkForModification(String tagID, long timestamp) {
        Request req = new Request();

        log.info("Checking for modification " + tagID + " " + timestamp);

        RequestHeader header = new RequestHeader();
        header.setUrl(SysConfig.serverHost+"/rest/cache/tag/"+tagID +"/"+timestamp);

        req.setHeader(header);

        GetRequest get = new GetRequest();
        String s = get.makeRequest(req);

        log.info("Checking for modification result " + s);

        return s;

    }

    public void clear() {
        cache.clear();
    }

}
