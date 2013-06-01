package ProxyServer.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 31.05.13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class Cache {

    private Map<String, CachedObject> cache = new HashMap<String, CachedObject>();
    private static Cache instance ;

    private Cache() {

    }

    public static Cache getInstance(){
        if(instance == null ) instance = new Cache();
        return instance;
    }

    public void addToCache(String tagID, String returnObject) {
        System.out.println("ADDING TO CACHE : " + tagID + " : " + returnObject);
        this.cache.put(tagID,new CachedObject(new Date(),returnObject));

    }

    public String getCachedValue(String tagID) {
        CachedObject response  = this.cache.get(tagID);
        if(response == null ) return null;
        else return response.getResponse();
    }

}
