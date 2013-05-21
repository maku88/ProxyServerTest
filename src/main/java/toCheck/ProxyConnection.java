package toCheck;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 20.05.13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
import java.net.*;
import java.io.*;
import java.util.*;

class ProxyConnection extends Thread {

    Socket fromClient;
    String host;
    int port;
    long timeout;


    ProxyConnection(Socket s, String host, int port, long timeout) {
        fromClient=s;
        this.host = host;
        this.port = port;
        this.timeout=timeout;
    }

    public void run() {
        InputStream clientIn = null;
        OutputStream clientOut = null;
        InputStream serverIn = null;
        OutputStream serverOut = null;
        Socket toServer = null;
        int r0=-1,r1=-1,ch=-1,i=-1;
        long time0 = new Date().getTime();
        long time1 = new Date().getTime();
        try {
            toServer = new Socket(host,port);
            Proxy2.display("open connection to:" + toServer + "(timeout=" + timeout + " ms)");
            clientIn = fromClient.getInputStream();
            clientOut = new BufferedOutputStream(fromClient.getOutputStream());
            serverIn = toServer.getInputStream();
            serverOut = new BufferedOutputStream(toServer.getOutputStream());
            while(r0!=0 || r1!=0 || (time1-time0)<=timeout) {
                while((r0=clientIn.available())>0) {
                    Proxy2.println(""); Proxy2.println("<<<" + r0 + " bytes from client");
                    Proxy2.display(""); Proxy2.display("<<<" + r0 + " bytes from client");
                    for(i=0; i<r0; i++) {
                        ch = clientIn.read();
                        if(ch!=-1) {
                            serverOut.write(ch);
                            Proxy2.print(ch);
                        } else {
                            Proxy2.display("client stream closed");
                        }
                    }
                    time0=new Date().getTime();
                    serverOut.flush();
                }
                while((r1=serverIn.available())>0) {
                    Proxy2.println(""); Proxy2.println(">>>" + r1 + " bytes from server");
                    Proxy2.display(""); Proxy2.display(">>>" + r1 + " bytes from server");
                    for(i=0; i<r1; i++) {
                        ch = serverIn.read();
                        if(ch!=-1) {
                            clientOut.write(ch);
                            Proxy2.print(ch);
                        } else {
                            Proxy2.display("server stream closed");
                        }
                    }
                    time0=new Date().getTime();
                    clientOut.flush();
                }
                if(r0==0 && r1==0) {
                    time1 = new Date().getTime();
                    Thread.sleep(100);
                    //Proxy2.display("waiting:"+(time1-time0)+" ms");
                }
            }
        } catch(Throwable t) {
            Proxy2.display("i=" + i + " ch=" + ch);
            t.printStackTrace(System.err);
        } finally {
            try {
                clientIn.close();
                clientOut.close();
                serverIn.close();
                serverOut.close();
                fromClient.close();
                toServer.close();
                Proxy2.quit(time1 - time0);
            } catch(Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}

