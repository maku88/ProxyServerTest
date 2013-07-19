package ProxyServer.cache.cacheImplementations;


import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;
import ProxyServer.methods.GetRequest;
import ProxyServer.request.Request;
import ProxyServer.request.RequestHeader;

import java.util.Date;

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
 * chyba że dostał null wtedy zwraca też to co ma w cahce
 *
 */
public class CFMCache extends BasicCache implements Cache{

    private static CFMCache instance ;

    private CFMCache(){}

    public static CFMCache getInstance(){
        if(instance == null ) instance = new CFMCache();
        return instance;
    }

    public void addToCache(String tagID, String returnObject) {
        System.out.println("ADDING TO CACHE : " + tagID + " : " + returnObject);
        this.getCache().put(tagID,new CachedObject(new Date().getTime(),returnObject,0));

    }


    public String getCachedValue(String tagID) {

        System.out.println("Getting from cache : " +tagID);

        CachedObject cachedObject  = super.getCache().get(tagID);

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

        System.out.println("Checking for modification " + tagID + " " + timestamp);

        RequestHeader header = new RequestHeader();
        header.setUrl(SysConfig.serverHost+"/rest/cache/tag/"+tagID +"/"+timestamp);

        req.setHeader(header);

        GetRequest get = new GetRequest();
        String s = get.makeRequest(req);

        System.out.println("Checking for modification result " + s);

        return s;

    }

}
