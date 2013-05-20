package ProxyServer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javax.ws.rs.core.MediaType;
import java.net.*;
import java.io.*;
import java.util.*;
/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 20.05.13
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class ProxyThread extends Thread {
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;
    public ProxyThread(Socket socket) {
        super("ProxyThread");
        this.socket = socket;
    }

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user

        try {
//            DataOutputStream out =new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
            String request ="";
            int cnt = 0;
            String urlToCall = "";
            ///////////////////////////////////
            //begin get request from client
            while ((inputLine = in.readLine()) != null) {
                try {
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                } catch (Exception e) {
                    break;
                }

                request+= inputLine;



                //parse the first line of the request to find the url
                if (cnt == 0) {
                    String[] tokens = inputLine.split(" ");
                    urlToCall = tokens[1];
                    //can redirect this to output log
                    System.out.println("Request for : " + urlToCall);
                }

                cnt++;
            }

            System.out.println("----------------------------");
            System.out.println(request);
            System.out.println("----------------------------");
            //end get request from client
            ///////////////////////////////////

            BufferedReader rd = null;
            try {
                //begin send request to server, get response from server

                ClientConfig config = new DefaultClientConfig();
                Client client = Client.create(config);
                WebResource webResource = client.resource(urlToCall);

                ClientResponse response = webResource.get(ClientResponse.class);
                System.out.println("Output from Server .... \n");
                String output = response.getEntity(String.class);
                System.out.println(output);

                PrintWriter out = new PrintWriter(socket.getOutputStream());

                // Start sending our reply, using the HTTP 1.1 protocol
                out.print("HTTP/1.1 200 \r\n"); // Version & status code
                out.print("Content-Type: text/plain\r\n"); // The type of data
                out.print("Connection: close\r\n"); // Will close stream
                out.print("\r\n"); // End of headers

                // Now, read the HTTP request from the client, and send it
                // right back to the client as part of the body of our
                // response. The client doesn't disconnect, so we never get
                // an EOF. It does sends an empty line at the end of the
                // headers, though. So when we see the empty line, we stop
                // reading. This means we don't mirror the contents of POST
                // requests, for example. Note that the readLine() method
                // works with Unix, Windows, and Mac line terminators.
//                String line;
//                while ((line = in.readLine()) != null) {
//                    if (line.length() == 0)
//                        break;
//                    out.print(line + "\r\n");
//                }

                out.write(output);

                // Close socket, breaking the connection to the client, and
                // closing the input and output streams
                out.close(); // Flush and close the output stream







//                URL url = new URL(urlToCall);
//                URLConnection conn = url.openConnection();
//                conn.setDoInput(true);
//                not doing HTTP posts
//                conn.setDoOutput(true);
//                System.out.println("content length: "
//                + conn.getContentLength());
//                System.out.println("allowed user interaction: "
//                + conn.getAllowUserInteraction());
//                System.out.println("content encoding: "
//                + conn.getContentEncoding());
//                System.out.println("content type: "
//                + conn.getContentType());
//
                // Get the response
//                InputStream is = null;
//                HttpURLConnection huc = (HttpURLConnection)conn;
//
//                if (conn.getContentLength() > 0) {
//                    try {
//                        is = conn.getInputStream();
//                        rd = new BufferedReader(new InputStreamReader(is));
//                    } catch (IOException ioe) {
//                        System.out.println(
//                                "********* IO EXCEPTION **********: " + ioe);
//                    }
//                }
                //end send request to server, get response from server
                ///////////////////////////////////

                ///////////////////////////////////

//                begin send response to client
//                byte by[] = new byte[ BUFFER_SIZE ];
//                int index = is.read( by, 0, BUFFER_SIZE );
//                while ( index != -1 )
//                {
//                    out.write( by, 0, index );
//                    index = is.read( by, 0, BUFFER_SIZE );
//                }

//                socket.getOutputStream().write(output.getBytes());
//                out.write(output.getBytes());
//                System.out.println(socket.isClosed());
//
//                out.flush();


                //end send response to client
                ///////////////////////////////////
            } catch (Exception e) {
                //can redirect this to error log
                System.err.println("Encountered exception: " + e);
                e.printStackTrace();
                //encountered error - just send nothing back, so
                //processing can continue
//                out.writeBytes("");
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
//            if (out != null) {
//                out.close();
//            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}