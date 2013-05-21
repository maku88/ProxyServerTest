package toCheck;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 20.05.13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class Proxy2 {

    public static final String usageArgs =" <localport> <host> <port> <timeout_ms>";

    static int clientCount;

    public static synchronized void print(int i) {
        System.out.print((char) i);
    }

    public static synchronized void println(String s) {
        System.out.println(s);
    }

    public static synchronized void display(String s) {
        System.err.println(s);
    }

    public static synchronized void quit(long t) {
        display("...quit after waiting "+t+" ms");
        clientCount--;
    }


    public void run(int localport, String host, int port,long timeout) {
        try {
            ServerSocket sSocket = new ServerSocket(localport);
            while(true) {
                Socket cSocket=null;
                try {
                    display("listening...");
                    cSocket = sSocket.accept();
                    if(cSocket!=null) {
                        clientCount++;
                        display("accepted as #"+clientCount+":"+cSocket);
                        ProxyConnection c = new ProxyConnection(cSocket,host,port,timeout);
                        c.run();
                    }
                } catch(Exception e) {
                    e.printStackTrace(System.err);
                }
                try {
                    cSocket.close();
                } catch(Exception e) {
                    //fall thru
                }
            }
        } catch(Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public static void main(String[] argv) {
        Proxy2 self = new Proxy2();

        if(argv.length>=3) {
            int localport = Integer.parseInt(argv[0]);
            String url = argv[1];
            int port = Integer.parseInt(argv[2]);
            int timeout = 30000;
            try {
                timeout=Integer.parseInt(argv[3]);
            } catch(Exception e) {}
            self.run(localport,url,port,timeout);
        } else {
            System.err.println("usage: java " + self.getClass().getName() + usageArgs);
        }
    }

}