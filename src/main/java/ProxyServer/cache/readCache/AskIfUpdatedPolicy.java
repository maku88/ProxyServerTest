package ProxyServer.cache.readCache;

import ProxyServer.cache.CachedObject;
import ProxyServer.methods.GetRequest;
import ProxyServer.request.Request;
import ProxyServer.request.RequestHeader;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 12.07.13
 * Time: 00:43
 * To change this template use File | Settings | File Templates.
 */
public class AskIfUpdatedPolicy implements ReadCachePolicy {
    @Override
    public String getValueFromCache(Map<String, CachedObject> cache, String tagID) {

        Request req = new Request();

        RequestHeader header = new RequestHeader();
        header.setUrl("http://morrigan.mobi-id.pl:8080/rest/cache/tag/"+tagID);

        req.setHeader(header);

        GetRequest get = new GetRequest();
        String s = get.makeRequest(req);

        System.out.println(s);


        return s;  //To change body of implemented methods use File | Settings | File Templates.
    }



}
