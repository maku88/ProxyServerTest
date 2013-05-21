package toCheck;


import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProxyDemo
{
    // Set to true if you want to see verbose output.
    private final static boolean bDebug = false;

    /**
     * This function makes an HTTP GET request of the specified URL using a proxy if provided.
     * If successfully, the HTTP response headers are printed out.
     * If the MIME type of the response is text/html, then the number of lines of text
     * is printed as well.
     *
     * @param strURL - A string representing the URL to request, eg, "http://bdn.borland.com/"
     * @param strProxy - A string representing either the IP address or host name of the proxy server.
     * @param iProxyPort - An integer that indicates the proxy port or -1 to indicate the default port for the protocol.
     * @return rc is true if the request succeeded and false otherwise.
     */
    static boolean doURLRequest(String strURL, String strProxy, int iProxyPort)
    {
        boolean rc = false;

        URL url = null;
        URLConnection c = null;

        try
        {
            System.out.println("\nHTTP IRequest: " + strURL);

            URL urlOriginal = new URL(strURL);

            if ((null != strProxy) && (0 < strProxy.length()))
            {
                URL urlProxy = new URL(urlOriginal.getProtocol(),
                        strProxy,
                        iProxyPort,// A value of -1 means use the default port for the specified protocol.
                        strURL);// The original URL is passed as "the file on the host".

                System.out.println("Using Proxy: " + strProxy);
                if (-1 != iProxyPort)
                {
                    System.out.println("Using Proxy Port: " + iProxyPort);
                }

                url = urlProxy;
            }
            else
            {
                url = urlOriginal;
            }

            c = url.openConnection();

            // In this example, we only consider HTTP connections.
            if (c instanceof HttpURLConnection)// instanceof returns true only if the object is not null.
            {

                HttpURLConnection h = (HttpURLConnection) c;
                h.connect();

                String strStatus = h.getResponseMessage() + " (" + h.getResponseCode() + ")";
                System.out.println("HTTP Status: " + strStatus);

                System.out.println("HTTP Response Headers: ");

                // Evidently, index 0 always returns null, so we start with index 1.
                for (int i = 1; ; i++)
                {
                    String strKey = h.getHeaderFieldKey(i);
                    if (null == strKey)
                    {
                        break;
                    }
                    System.out.println(i + ": " + strKey + ": " + h.getHeaderField(i));
                }

                // Normally at this point, one would download data from the connection.
                // For example, if the MIME type is html, then download the string and display it.
                String strContentType = h.getContentType();
                if ((null != strContentType) && (0 == strContentType.compareTo("text/html")))
                {
                    // Set boolean bDebug to true if you want verbose output.
                    // For simplicity's sake, we just count the number of lines of text.
                    if (bDebug) System.out.println("Received text/html:[");

                    int iNumLines = 0;

                    try
                    {
                        InputStream in = h.getInputStream();
                        BufferedReader data = new BufferedReader(new InputStreamReader(in));

                        String line = null;
                        while((line = data.readLine()) != null)
                        {
                            if (bDebug) System.out.println(line);

                            iNumLines++;
                        }
                    }
                    catch(Exception exc2)
                    {
                        System.out.println("**** IO failure: " + exc2.toString());
                    }
                    finally
                    {
                        if (bDebug) System.out.println("]");
                        System.out.println("Received text/html has " + iNumLines + " lines");
                    }
                }

                h.disconnect();
            }
            else
            {
                System.out.println("**** No download: connection was not HTTP");
            }

            rc = true;
        }
        // Catch all exceptions.
        catch(Exception exc)
        {
            System.out.println("**** Connection failure: " + exc.toString());
            // System.out.println("**** Connection failure: " + exc.getMessage());// Same as above line but without the exception class name.
        }
        finally
        {
            // Do cleanup here.
            // For example, the following, in theory, could make garbage collection more efficient.
            // This might be the place where you choose to put your method call to your connection's "disconnect()";
            // curiously, while every URLConnection has a connect() method, they don't necessarily have a disconnect() method.
            // HttpURLConnection has a disconnect() which is called above.
            c = null;
            url = null;

            return rc;
        }
    }

    public static void main(String[] args)
    {
        // Simple request, not using a proxy server.
        ProxyDemo.doURLRequest("http://www.borland.com/", null, -1);
        ProxyDemo.doURLRequest("http://www.borland.com", null, -1);

        // IRequest, using a proxy server.
        /**
         * @todo: Note that the proxy indicated below will fail. Change to a valid server.
         * (If you do not have a proxy server available, then search for a web site that
         * lists public HTTP proxy servers.
         * I hesitate to list any here as I'm not sure about the "legitimacy" of all these sites.)
         */
        // *** If you don't change the proxy setting to something valid in the following,
        // then you will get the following error message:
        // **** Connection failure: java.net.BindException: Cannot assign requested address: connect
        ProxyDemo.doURLRequest("http://www.borland.com/", "172.0.0.1", 10000); // **** Change this line to use a valid proxy.
    }
}
