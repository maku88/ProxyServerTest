package ProxyServer;

import ProxyServer.cache.cacheImplementations.*;
import ProxyServer.config.SysConfig;
import ProxyServer.stats.RequestStats;
import ProxyServer.stats.StatsCollector;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.*;
import java.io.*;
import java.util.List;

@Component
public class ProxyServer implements RemoteProxyServer {

    private Cache cacheImpl;
    Logger log = Logger.getLogger(ProxyServer.class);
    private boolean proxyListeningFlag = true;
    private ServerSocket serverSocket = null;
    private StatsCollector statsCollector = new StatsCollector();
    private int simulationID;

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        ProxyServer p = context.getBean(ProxyServer.class);
        p.startProxy(args);

    }

    private void startProxy(String[] args) throws IOException {


        SysConfig.setServerHost(args[0]);
        SysConfig.setPort(Integer.parseInt(args[1]));

        serverSocket = new ServerSocket(SysConfig.port);
        log.info("------------------");
        log.info("PROXY SERVER START");

        log.info("OrginServer : " + SysConfig.serverHost);
        log.info("WAITING FOR SETUP MESSAGE");
        start();
    }


    private void start() {
        try {
            log.info("START");
            log.info("Started on: " + serverSocket.getInetAddress().getHostAddress() + ":"+ SysConfig.port);
            //TODO zbieranie statystyk

            while (proxyListeningFlag) {
                new ProxyThread(serverSocket.accept(),cacheImpl,statsCollector, simulationID).start();
            }
            serverSocket.close();
        } catch (IOException e) {
            log.error("Could not listen on port: " + SysConfig.port);
            System.exit(-1);
        }
    }


    public void reloadCache(String cacheType, int ttl, int capacity, int simulationID) {
            log.info(">> SETUP MESSAGE <<");
            proxyListeningFlag =false;
            cacheImpl = getCorrectCache(cacheType,capacity,ttl);
            this.simulationID = simulationID;

            log.info("Cache : " + cacheImpl.getClass().getSimpleName());
            log.info("TTL : " + ttl);
            log.info("CacheSize : " + capacity);
            log.info("SimulationID : " + simulationID);

            proxyListeningFlag=true;
    }

    private Cache getCorrectCache(String cacheType, int cacheSize, int ttl) {
        if(cacheType.equals("FAKE")) return new FakeCache(cacheSize);
        else if(cacheType.equals("TTL")) return  new TTLCacheImpl(cacheSize,ttl);
        else if(cacheType.equals("LRU")) return  new LRUCacheImpl(cacheSize);
        else if(cacheType.equals("CFM")) return  new CFMCacheImpl(cacheSize);
        else return null;
    }

    public List<RequestStats> getStats() {
        return statsCollector.getSimulationResults();
    }

}
