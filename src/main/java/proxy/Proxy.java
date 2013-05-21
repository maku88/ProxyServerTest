//package proxy;
///******************************************************************
// *** File proxy.java
// ***                   http://www.cs.technion.ac.il/Courses/Computer-Networks-Lab/projects/spring98/proj1/report/source/proxy/ServerInfo.java
// ***/
//
//
//import java.net.*;
//import java.io.*;
//import java.util.*;
//import java.awt.*;
//
//import proxy.HttpReplyHdr;
//import proxy.HttpRequestHdr;
//import proxy.Configuration;
//import proxy.cache;
//import proxy.ServersAccessibleWOProxy;
//import proxy.DocumentInfo;
//import proxy.ServerInfo;
//import proxy.ClientRequestList;
//import proxy.HitCounter;
////import proxy.ICP.*;
//import proxy.LogTracer;
//
//
////An object of this class performs a client HTTP request
////processing:
//// 1. parses the HTTP request
//// 2. searches in the cache
//// 3. transmits the requested document to the client
//// 4. stores documents in the cache
//// 5. yields connection statistics
//public class Proxy extends Thread {
//    public Proxy(Socket socket,
//                 Configuration p_config,
//                 CacheCleaner p_cacheCleaner,
//                 ServersAccessibleWOProxy p_serversAccessibleWOProxy,
//                 LogTracer p_mainFrame,
//                 ClientRequestList p_clientRequestList)
//            throws java.sql.SQLException
//    {
//        client = socket;
//        config = p_config;
//        cache = new cache(p_cacheCleaner, p_mainFrame);
//        serversAccessibleWOProxy = p_serversAccessibleWOProxy;
//        mainFrame = p_mainFrame;
//        clientRequestRegister = p_clientRequestList;
//        documentInfo = new DocumentInfo(p_config);
//    }
//
//
//    /**
//     * Handle a single client request.
//     */
//    public  void run(){
//        Socket  server  = null;
//
//        String clientName ="";
//        String serverName ="";
//        URL url;
//
//        HttpRequestHdr request = new HttpRequestHdr();
//        HttpReplyHdr   reply   = new HttpReplyHdr();
//
//        boolean documentInCache = false;
//        boolean serverFilesMayBeCached = false;
//        File file = null;
//        FileOutputStream fileToCache = null;
//        InetAddress havingCache;
//
//        hitCounter = config.getHitCounter();
//        try {
//            /*
//            * Read HTTP Server IRequest from client
//            */
//            request.parse(client.getInputStream());
//            clientName = client.getInetAddress().getHostName();
//            url = new URL(request.url);
//            mainFrame.updateLog("Client '" + clientName +
//                    "' Requesting '" + request.url+"'");
//
//            /*
//               * Check if request should and can be fulfilled by cache.
//               */
//
//            synchronized(serversAccessibleWOProxy) {
//                if(!serversAccessibleWOProxy.containsKey(url.getHost())) //##
//                    serverFilesMayBeCached = true;
//            }
//
//            if(serverFilesMayBeCached &&
//                    !request.pragmaNoCache &&
//                    request.ifModifiedSince.equals(""))
//            {
//                cache.getDocumentInfo(url, documentInfo);              //##
//                documentInCache = true;
//            }
//
//
//            if (documentInCache && documentInfo.isInitialized() &&
//                    readDocumentFromCache(request, documentInfo))
//            {
//                return;
//            }
//
//            /*
//            * Contact remote server File
//            */
//
//
//            if(serverFilesMayBeCached &&
//                    !request.pragmaNoCache &&
//                    request.ifModifiedSince.equals(""))
//            {
//                hitCounter.incMisses();
//                mainFrame.updatePieChart(hitCounter.getHits(),
//                        hitCounter.getMisses());
//            }
//
//            Date	timerEnd;
//            Date timerStart = new Date();
//            if (config.alternativeProxyOn()) {
//                synchronized(config) {
//                    serverName = config.getAlternativeProxyName();
//                    server = new Socket(serverName,config.getAlternativeProxyPort());
//                }
//            }
//            else	{
//                serverName = cache.getHostName(url);
//                server = new Socket(serverName,serverPort(request.url));
//            }
//
//            timerEnd = new Date();
//            double connectionTime = timerEnd.getTime() - timerStart.getTime();
//
//            /*
//            * send request to server
//            */
//
//            mainFrame.updateLog("Passing the request for "+request.url+" to "+ serverName);
//
//            DataOutputStream srvOut =
//                    new DataOutputStream(server.getOutputStream());
//            if (!config.alternativeProxyOn())
//                request.url = serverUrl(request.url);
//            srvOut.writeBytes(request.toString(false));
//            srvOut.flush();
//
//            /*
//            * Send data to server (needed for post method and things
//            * that go bump in the net).
//            */
//            for (int i =0; i < request.contentLength; i++) {
//                server.getOutputStream().write(client.getInputStream().read());
//            }
//            server.getOutputStream().flush();
//
//            /*
//            * Echo and send the HTTP responce header from
//            * the server to the client.
//            */
//            DataInputStream Din  = new DataInputStream(server.getInputStream());
//            DataOutputStream Dout =
//                    new DataOutputStream(client.getOutputStream());
//
//            timerStart = new Date();  // start timing transmission of responce.
//
//            String statusLine = Din.readLine();
//            StringTokenizer st = new StringTokenizer(statusLine);
//
//            // Parse responce header
//            st.nextToken();     // HTTP version
//            String returnCode = st.nextToken();
//
//            if (!returnCode.equals("200") || !serverFilesMayBeCached) {//file is not cached
//                mainFrame.updateLog("Transferring "+request.url+ " to "+clientName);
//                Dout.writeBytes(statusLine + "\r\n");
//                if (statusLine.length() > 0) {
//                    while (true) {
//                        String str = Din.readLine();
//                        Dout.writeBytes(str+"\r\n");
//                        if (str.length() <= 0) break;
//                    }
//                }
//                Dout.flush();
//                /*
//                    * read responce from server.
//                    * And tunnel it to the client.
//                    */
//                InputStream  in  = server.getInputStream();
//                OutputStream out = client.getOutputStream();
//
//                byte data[] = new byte[2000];
//                int count;
//                while (-1 < ( count  = in.read(data))) {
//                    out.write(data,0,count);
//                }
//
//                try {out.flush();} catch (Exception ef1){}
//                try {in.close();} catch (Exception e){}
//                try {client.close();} catch (Exception e){}
//
//                if (serverFilesMayBeCached) {
//                    // reply did not include body, so only connection time info
//                    // is saved.
//
//                }
//                return;
//            }
//
//
//            // channel reply to client and cache it
//
//
//            mainFrame.updateLog("Transferring "+request.url+ " to "+clientName +
//                    " and saving to local file ");
//
//            statusLine += "\r\n";
//            Dout.writeBytes(statusLine);
//
//            OutputStream replyBuffer = documentInfo.getDestBufferStream();
//            long contentLength = statusLine.length();
//
//            replyBuffer.write(statusLine.getBytes(), 0, statusLine.length());
//            if (statusLine.length() > 2) {
//                while (true) {
//                    String str = Din.readLine();
//                    reply.checkHeaderInfo(str);
//                    Dout.writeBytes(str += "\r\n");
//                    replyBuffer.write(str.getBytes(), 0, str.length());
//                    contentLength+=str.length();
//                    if (str.length() <= 2) break;
//                }
//            }
//
//            Dout.flush();
//
//            /*
//               * read responce from server.
//               * And tunnel it to the client.
//               */
//            InputStream  in  = server.getInputStream();
//            OutputStream out = client.getOutputStream();
//
//            byte data[] = new byte[2000];
//            int count;
//            while (-1 < ( count  = in.read(data))) {
//                out.write(data,0,count);
//                replyBuffer.write(data, 0, count);
//                contentLength += count;
//            }
//
//            timerEnd = new Date();
//            // Estimate bandwidth [bytes/seconds]
//            double bandWidth = 1000. * contentLength
//                    /(timerEnd.getTime() - timerStart.getTime());
//
//
//            try {out.flush();} catch (Exception ef1){}
//            try {in.close();} catch (Exception e){}
//            try {client.close();} catch (Exception e){}
//
//            // Update cache.
//
//            mainFrame.updateLog("Caching "+request.url);
//            try {
//                replyBuffer.close();
//            } catch (IOException erio) {
//                System.err.println("An IO-error in caching buffer closing: ");
//                System.err.println(erio.getMessage());
//            }
////            config.getCacher().updateURL(url, contentLength,
////                    documentInfo.getFilename(), reply,
////                    cache);
//            mainFrame.updateLog("********************************************************");
//            mainFrame.updateLog("Caching done");
//
//            synchronized(config) {
//                mainFrame.updateCacheBar(config.getCacheHighWaterMark(),
//                        config.getCacheLowWaterMark(),
//                        cache.getCurrentWaterMark());
//            }
//
//            return;
//
//
//        } catch (UnknownHostException uhe)  {
//            /*
//            * Requested Server could not be located
//            */
//            mainFrame.updateLog("Server Not Found"+uhe.toString());
//
//            try  {
//                DataOutputStream out =
//                        new DataOutputStream(client.getOutputStream());
//                out.writeBytes(reply.formNotFound());
//                out.flush();
//            } catch (Exception uhe2){}
//
//        } catch (Exception e) {
//            mainFrame.updateLog("Proxy error: "+e.getMessage());
//
//            try  {
//                DataOutputStream out =
//                        new DataOutputStream(client.getOutputStream());
//                out.writeBytes(reply.formInternalError());
//                out.flush();
//
//            } catch (Exception uhe2){}
//
//
//        } finally  {
//            try {client.getOutputStream().flush();} catch (Exception ef1){}
//            try {server.close();} catch (Exception e){}
//            try {client.close();} catch (Exception e){}
//
//            /*
//            * log the request
//            */
//            mainFrame.updateLog("Done [" + clientName+"] "+ request.url);
//        }
//    }
//
//
//    public void setDocumentInfo (DocumentInfo val) {
//        documentInfo = val;
//    }
//
//
//
//    //Returns cached version of the requested document
//    //Increments reference counter of the cached URL
//    private boolean readDocumentFromCache (HttpRequestHdr request,
//                                           DocumentInfo documentInfo)
//            throws IOException
//    {
//        byte data[] = new byte[2000];
//        int count;
//        OutputStream out = client.getOutputStream();
//        InputStream in = documentInfo.getSourceStream(); //$$
//
//        if (in != null) {
//            mainFrame.updateLog("Retrieving "+ request.url + " from cache.");
//            while (-1 < ( count  = in.read(data))) {
//                out.write(data,0,count);
//                //Thread.yield(); // Remember this is multi-threaded
//            }
//
//            try {out.flush();} catch (Exception ef1){}
//            try {in.close();} catch (Exception e){}
//            try {client.close();} catch (Exception e){}
//            config.getRequestCounter().incRC(documentInfo.getId(), cache);
//            hitCounter.incHits();
//            mainFrame.updatePieChart(hitCounter.getHits(),
//                    hitCounter.getMisses());
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * Find the :PORT form http://server.ect:PORT/some/file.xxx
//     *
//     * @return Servers internet name
//     */
//    private int serverPort(String str) {
//        // chop to "server.name:x/thing"
//        int i = str.indexOf("//");
//        if (i< 0) return 80;
//        str = str.substring(i+2);
//
//        // chop to  server.name:xx
//        i = str.indexOf("/");
//        if (0 < i) str = str.substring(0,i);
//
//        // chop XX
//        i = str.indexOf(":");
//        if (0 < i)  {
//            return Integer.parseInt(str.substring(i).trim());
//        }
//
//        return 80;
//    }
//
//    /**
//     * Find the /some/file.xxxx form http://server.ect:PORT/some/file.xxx
//     *
//     * @return the deproxied url
//     */
//    private String serverUrl(String str) {
//        int i = str.indexOf("//");
//        if (i< 0) return str;
//
//        str = str.substring(i+2);
//        i = str.indexOf("/");
//        if (i< 0) return str;
//
//        return str.substring(i);
//    }
//
//
//    private Configuration config;
//    private cache cache;
//    private ServersAccessibleWOProxy serversAccessibleWOProxy;
//    private LogTracer mainFrame;
//    private DocumentInfo documentInfo;
//    private ClientRequestList clientRequestRegister;
//    private HitCounter hitCounter;
//
//
//    /*
//    * Sockets to requesting client and responding server
//    */
//    private Socket client  = null;
//
//
//
//}
