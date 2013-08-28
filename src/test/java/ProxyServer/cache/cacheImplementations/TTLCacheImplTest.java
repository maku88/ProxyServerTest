package ProxyServer.cache.cacheImplementations;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 26.08.13
 * Time: 22:33
 * To change this template use File | Settings | File Templates.
 */
public class TTLCacheImplTest {

    private TTLCacheImpl cache;

    @Before
    public void before() {

        cache = new TTLCacheImpl(3,3);
    }


    @Test
    public void testAddToCache() throws Exception {

        cache.addToCache("x","a");
        cache.addToCache("y","a");
        cache.addToCache("z","a");
        cache.addToCache("d","a");

        cache.printCache();

        Date now = new Date();

        System.out.println(now);
        System.out.println(now.getTime());

        Date next = new Date(now.getTime() + 100000);
        System.out.println(next);
        System.out.println(next.getTime());


    }

    @Test
    public void testGetCachedValue() throws Exception {
        //given
        long TTL = 10000;
        cache = new TTLCacheImpl(10,TTL);

        //when
        String script = "a";
        String tag = "x";
        cache.addToCache(tag,script);

        //then

        String retNotNull = cache.getCachedValue(tag);
        Thread.sleep(20000);
        String ret = cache.getCachedValue(tag);

        System.out.println(retNotNull);
        System.out.println(ret);


    }

    public void testClear() throws Exception {

    }
}
