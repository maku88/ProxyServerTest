package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;
import ProxyServer.config.SysConfig;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 24.07.13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class BasicCacheTest {
    @Test
    public void testPut() throws Exception {

        BasicCache<String,CachedObject> cache = new BasicCache<String, CachedObject>(2);

        cache.put("1",null);
        cache.put("2",null);
        cache.put("3",null);
        cache.put("4",null);

        for(String s : cache.keySet()) {
            System.out.println(s);
        }

    }
}
