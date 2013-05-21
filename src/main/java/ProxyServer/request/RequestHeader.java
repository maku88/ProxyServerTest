package ProxyServer.request;

import ProxyServer.ProxyServer;
import lombok.Data;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 21.05.13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
@Data
@ToString
public class RequestHeader {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    private Method method;
    private String url;
    private String httpVersion;
    private String host;
    private String accept;
    private String proxyConnection;

}