package toCheck;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleProxyServer {
    public static final int portNumber = 10000;
    public static final int maxConnections = 100;

    public static void main( String[] args )
    {
        SimpleProxyServer proxyServer = new SimpleProxyServer();
        proxyServer.start();
    }

    public void start()
    {
        System.out.println("Starting the SimpleProxyServer ...");
        try
        {
            ServerSocket serverSocket = new ServerSocket( portNumber, maxConnections );
            byte[] buffer = new byte[10000];

            boolean run = true;
            while( run )
            {
                Socket clientSocket = serverSocket.accept();

                InputStream clientInputStream = clientSocket.getInputStream();

                // reading the request and put it into buffer
                final int readBytesCount = clientInputStream.read( buffer );
                if( readBytesCount < 0)
                    continue;

                String browserRequest = new String( buffer, 0, readBytesCount );
                System.out.println( browserRequest );

                // extract the host to connect to
                final int hostNameStart = browserRequest.indexOf( "Host: " ) + 6;
                final int hostNameEnd = browserRequest.indexOf( '\n', hostNameStart );
                final String hostName = browserRequest.substring( hostNameStart, hostNameEnd - 1 );
                System.out.println( "Connecting to host " + hostName );

                // forward the response from the proxy to the server
                Socket hostSocket = new Socket( hostName, 80 );
                OutputStream hostOutputStream = hostSocket.getOutputStream();
                System.out.println( "Forwarding request to server" );
                hostOutputStream.write( buffer, 0, readBytesCount );
                hostOutputStream.flush();

                ProxyThread thread1 = new ProxyThread( clientSocket, hostSocket );
                thread1.start();

                ProxyThread thread2 = new ProxyThread( hostSocket, clientSocket );
                thread2.start();
            }
            serverSocket.close();
        }
        catch( IOException e )
        {
            System.err.println( "IO Error: " + e.getMessage() );
            e.printStackTrace();
        }
    }

class ProxyThread extends Thread
{
    private Socket incoming, outgoing;

    ProxyThread( Socket in, Socket out )
    {
        incoming = in;
        outgoing = out;
    }

    // Overwritten run() method of thread,
    // does the data transfers
    public void run()
    {
        System.out.println( "Starting proxy thread" );
        try
        {
            OutputStream toClient = outgoing.getOutputStream();
            InputStream fromClient = incoming.getInputStream();

            int numberRead = 0;
            byte[] buffer = new byte[10000];
            do
            {
                numberRead = fromClient.read( buffer );
                System.out.println( "Read " + numberRead + " bytes" );
                System.out.println( "Buffer: " + buffer );
                if( numberRead > 0 )
                {
                    toClient.write( buffer, 0, numberRead );
                    System.out.println( "Sent " + numberRead + " bytes" );
                }
            }
            while( numberRead > 0 );

            System.out.println( "Closing all streams and sockets" );
            toClient.flush();
            incoming.close();
            outgoing.close();
        }
        catch( IOException e )
        {
            System.err.println( "IO Error: " + e.getMessage() );
        }
        catch( ArrayIndexOutOfBoundsException e )
        {
            System.err.println( "Index error: " + e.getMessage() );
        }
    }
}
}