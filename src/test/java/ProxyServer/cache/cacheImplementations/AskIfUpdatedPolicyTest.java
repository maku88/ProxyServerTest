package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 12.07.13
 * Time: 00:47
 * To change this template use File | Settings | File Templates.
 */
public class AskIfUpdatedPolicyTest {

    Map<String, CachedObject> cache = new HashMap<String, CachedObject>();
    String tagID = "FB453A64";

    @Before
    public void before() {

        cache.put(tagID,new CachedObject(new Date().getTime(),"aaa",0));

    }

    @Test
    public void testGetValueFromCache() throws Exception {

//        AskIfUpdatedPolicy policy = new AskIfUpdatedPolicy();
//
//        Date start = new Date();
//
//        System.out.println(policy.getValueFromCache(cache, tagID));
//
//        Date end = new Date();
//
//        System.out.println(end.getTime() - start.getTime());

    }
}
