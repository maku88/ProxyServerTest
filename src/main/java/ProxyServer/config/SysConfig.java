package ProxyServer.config;

import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 19.07.13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class SysConfig {

    public static String serverHost = "http://localhost:8080";
    public static int port = 10000;
    public static long timeToLiveParam = 100;
    public static int CacheSize = 10;


    public static void setServerHost(String serverHost) {
        SysConfig.serverHost = "http://"+serverHost;
    }

    public static void setPort(int port) {
        SysConfig.port = port;
    }

    public static void setTimeToLiveParam(long timeToLiveParam) {
        SysConfig.timeToLiveParam = timeToLiveParam;
    }

    public static void setCacheSize(int cacheSize) {
        CacheSize = cacheSize;
    }


}
