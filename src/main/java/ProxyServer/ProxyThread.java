package ProxyServer;

import ProxyServer.methods.GetRequest;
import ProxyServer.methods.PostRequest;
import ProxyServer.methods.IRequest;
import ProxyServer.request.Request;
import ProxyServer.request.RequestHeader;
import org.apache.commons.io.IOUtils;

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
        try {

            Map<String, String> headerMap = new HashMap<String, String>();
            PrintWriter out = new PrintWriter(socket.getOutputStream());
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream in = socket.getInputStream();

            ///////////////////////////////////
            //begin get request from client

//            Header[] header = HttpParser.parseHeaders(socket.getInputStream(),"UTF-8");
//            for(Header h : header ) {
//                System.out.println(h.getName() + " : " + h.getValue());
//            }



            Request requestFromClient = readRequest(in);
            System.out.println("REQUEST FROM CLIENT : " + requestFromClient.toString());
            try {
                //begin send request to server, get response from server
//                ClientConfig config = new DefaultClientConfig();
//                Client client = Client.create(config);
//                WebResource webResource = client.resource(urlToCall);
//
//                ClientResponse response = webResource.get(ClientResponse.class);
//                System.out.println("Output from Server .... \n");
//                String output = response.getEntity(String.class);
//                System.out.println(output);

                IRequest requestHandelr;

                switch (requestFromClient.getHeader().getMethod()) {
                    case GET:
                        requestHandelr = new GetRequest();
                        break;
                    case POST:
                        requestHandelr = new PostRequest();
                        break;
                    default:
                        requestHandelr = new GetRequest();
                }

                String output = requestHandelr.makeRequest(requestFromClient);

                // Start sending our reply, using the HTTP 1.1 protocol
                out.print("HTTP/1.1 200 \r\n"); // Version & status code
                out.print("Content-Type: text/plain\r\n"); // The type of data
                out.print("Connection: close\r\n"); // Will close stream
                out.print("\r\n"); // End of headers
                out.write(output);

                out.close(); // Flush and close the output stream

            } catch (Exception e) {
                System.err.println("Encountered exception: " + e);
                e.printStackTrace();
            }


            if (out != null) {
                out.close();
            }
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


    private Request readRequest(InputStream in) throws IOException {
        Request requestFromClient = new Request();
        String inputLine = "", outputLine;
        int cnt = 0;
        String urlToCall = "";
        RequestHeader header =  new RequestHeader();



//        System.out.println(new String(chars));
//
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }

        System.out.println(output.toString());

//        while (((inputLine = in.readLine()) != null) && (!(inputLine.equals("")))) {
//            try {
//                StringTokenizer tok = new StringTokenizer(inputLine);
//                tok.nextToken();
//            } catch (Exception e) {
//                break;
//            }
//
//            System.out.println(inputLine);
//
////            parse the first line of the request to find the url
//            String[] tokens = inputLine.split(" ");
//            if (cnt == 0) {
//
//                urlToCall = tokens[1];
////                can redirect this to output log
//                System.out.println("IRequest for : " + urlToCall);
//                header.setMethod(RequestHeader.Method.valueOf(tokens[0]));
//                header.setUrl(tokens[1]);
//                header.setHttpVersion(tokens[2]);
//            }else if(cnt == 1 ) {
//
//
//            }else if(cnt == 2) {
//                header.setHost(tokens[1]);
//            }
//
//            cnt++;
//        }
        requestFromClient.setHeader(header);
        requestFromClient.setBody("");

        return requestFromClient;

    }

}