package ProxyServer.cache.readCache;

import ProxyServer.cache.Cache;
import ProxyServer.cache.CachedObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 12.07.13
 * Time: 00:41
 * To change this template use File | Settings | File Templates.
 */
public class BasicReadPolicy implements ReadCachePolicy {
    @Override
    public String getValueFromCache(Map<String, CachedObject> cache, String tagID) {
        CachedObject response  = cache.get(tagID);
        if(response == null ) return null;
        else return response.getResponse();
    }
}
