package ProxyServer.cache.cacheImplementations;


import ProxyServer.config.SysConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 20.07.13
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
public class LRUCacheImplTest {

    private LRUCacheImpl cache;
    private String tagId = "123";
    private String script = "aaa";

    @Before
    public void initCache() {
        SysConfig.setCacheSize(3);
        this.cache = LRUCacheImpl.getInstance(SysConfig.CacheSize);
    }

    @Test
    public void testAddToCache() throws Exception {
        cache.addToCache(tagId,script);
        String ret = cache.getCachedValue(tagId);
        assertEquals(ret, script);
        
    }

    @Test
    public void testGetCachedValue() throws Exception {

        String firstElem = "1";
        String secElem = "2";
        String thirdElem = "3";
        String fourthElem = "4";

        cache.addToCache(firstElem,firstElem);
        cache.addToCache(secElem,secElem);
        cache.addToCache(thirdElem,thirdElem);

        cache.printCache();

        cache.getCachedValue(firstElem);
        cache.getCachedValue(secElem);
        cache.getCachedValue(secElem);

        cache.addToCache(fourthElem,fourthElem);

        cache.printCache();

        String fRsp = cache.getCachedValue(firstElem);
        String sRsp = cache.getCachedValue(secElem);
        String tRsp = cache.getCachedValue(thirdElem);
        String fourthRsp = cache.getCachedValue(fourthElem);

        assertEquals(fRsp,firstElem);
        assertEquals(sRsp,secElem);
        assertEquals(tRsp,null);
        assertEquals(fourthRsp,fourthElem);

    }
}
