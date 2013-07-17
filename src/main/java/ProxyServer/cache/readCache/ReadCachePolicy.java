package ProxyServer.cache.readCache;

import ProxyServer.cache.Cache;
import ProxyServer.cache.CachedObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 12.07.13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public interface ReadCachePolicy {

    public String getValueFromCache(Map<String, CachedObject> cache, String tagID);

}
