package ProxyServer.cache;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 01.06.13
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public interface RefreshCachePolicy {

    public void refreshCachedObject(CachedObject object);

}
