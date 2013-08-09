package ProxyServer.cache.cacheImplementations;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 22:34
 * To change this template use File | Settings | File Templates.
 */
public interface Cache  {

     void addToCache(String tagID, String returnObject);
     String getCachedValue(String tagID);
        void clear();

}
