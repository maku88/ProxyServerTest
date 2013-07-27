package ProxyServer.config;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class SysConfig {

    public static final String serverHost = "http://localhost:8080";

    public static long timeToLiveParam = 100;
    public static int CacheSize = 10;

    public static void setCacheSize(int size) {
        CacheSize = size;
    }

}
