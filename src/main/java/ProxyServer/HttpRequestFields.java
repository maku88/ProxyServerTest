package ProxyServer;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 31.05.13
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public enum HttpRequestFields {

    ACCEPT("Accept:"),
    CONTENT_TYPE("Content-type:"),
    CONTENT_LENGTH("Content-Length:"),
    HOST("Host:"),
    PROXY_CONNECTION("Proxy-Connection:");

    private String field;

    HttpRequestFields(String s) {
        this.field = s;
    }

    public String getField() {
        return field;
    }
}
