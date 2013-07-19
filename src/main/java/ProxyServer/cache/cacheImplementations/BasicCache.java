package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.*;

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
abstract class BasicCache  {

    private Map<String, CachedObject> cache = new HashMap<String, CachedObject>();

    protected Map<String, CachedObject> getCache() {
        return cache;
    }
}
