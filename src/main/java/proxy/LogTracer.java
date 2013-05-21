//package proxy;
//
//import java.net.ServerSocket;
//import proxy.CacheCleaner;
//import proxy.ServersAccessibleWOProxy;
//
//
////Interface which declares cache performance tracing during
////the runtime
//public interface LogTracer
//{
//    public void updatePieChart(int hits, int misses);
//
//    public void updateCacheBar(long p_highWM, long p_lowWM, long p_currentWM);
//
//    public void updateLog(String str);
//
//    public void updateErrorList(String str);
//
//    public void setServerSocket(ServerSocket p_serverSocket);
//
//    public void setServersAccessibleWOProxy(ServersAccessibleWOProxy sawop);
//
//    public void setCache(CacheCleaner p_cache);
//}