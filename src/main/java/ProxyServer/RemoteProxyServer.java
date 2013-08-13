package ProxyServer;

import ProxyServer.stats.RequestStats;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 09.08.13
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public interface RemoteProxyServer {

    public void reloadCache(String cacehType, int ttl,int capacity, int simulationID) ;

    public List<RequestStats> getStats();
}
