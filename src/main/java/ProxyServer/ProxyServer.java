package ProxyServer;

import ProxyServer.cache.cacheImplementations.*;
import ProxyServer.config.SysConfig;
import ProxyServer.stats.StatsCollector;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.*;
import java.io.*;

@Component
public class ProxyServer implements RemoteProxyServer {

    private Cache cacheImpl;

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        ProxyServer p = context.getBean(ProxyServer.class);
        p.startProxy(args);

    }

    private void startProxy(String[] args) throws IOException {
        Logger log = Logger.getLogger(ProxyServer.class);
        ServerSocket serverSocket = null;
        boolean listening = true;
        StatsCollector statsCollector = new StatsCollector();

        SysConfig.setServerHost(args[1]);
        SysConfig.setCacheSize(Integer.parseInt(args[2]));
        SysConfig.setPort(Integer.parseInt(args[3]));
        SysConfig.setTimeToLiveParam(Integer.parseInt(args[4]));


        if(args[0].equals("FAKE")) cacheImpl = FakeCache.getInstance(SysConfig.CacheSize);
        else if(args[0].equals("TTL")) cacheImpl = TTLCacheImpl.getInstance(SysConfig.CacheSize);
        else if(args[0].equals("LRU")) cacheImpl = LRUCacheImpl.getInstance(SysConfig.CacheSize);
        else if(args[0].equals("CFM")) cacheImpl = CFMCacheImpl.getInstance(SysConfig.CacheSize);


        try {
            serverSocket = new ServerSocket(SysConfig.port);
            log.info("------------------");
            log.info("PROXY SERVER START");
            log.info("Started on: " + serverSocket.getInetAddress().getHostAddress() + ":"+ SysConfig.port);
            log.info("Cache : " + cacheImpl.getClass().getSimpleName());
            log.info("OrginServer : " + SysConfig.serverHost);
            log.info("TTL : " + SysConfig.timeToLiveParam);
            log.info("CacheSize : " + SysConfig.CacheSize);
        } catch (IOException e) {
            log.error("Could not listen on port: " + SysConfig.port);
            System.exit(-1);
        }

        while (listening) {
            new ProxyThread(serverSocket.accept(),cacheImpl,statsCollector).start();
        }
        serverSocket.close();
    }

    public void clearCache() {
        System.out.println("CLEAR CACHE MESSAGE");
        cacheImpl.clear();
    }
}
