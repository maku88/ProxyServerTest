package ProxyServer;

import ProxyServer.cache.Cache;
import ProxyServer.methods.GetRequest;
import ProxyServer.methods.PostRequest;
import ProxyServer.methods.IRequest;
import ProxyServer.request.ReqestBody;
import ProxyServer.request.Request;
import ProxyServer.request.RequestHeader;
import com.google.common.io.CharStreams;
import com.sun.org.apache.xpath.internal.functions.FuncSystemProperty;
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
    private Cache cache = Cache.getInstance();

    public ProxyThread(Socket socket) {
        super("ProxyThread");
        this.socket = socket;
    }



    public void run() {
        try {



            System.out.println("id : " +this.getId());

            Map<String, String> headerMap = new HashMap<String, String>();
            PrintWriter out = new PrintWriter(socket.getOutputStream());

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

                String output = "";
                if(requestFromClient.getHeader().getUrl().endsWith("/actions")) {
                    String valueFromCache = cache.getCachedValue(requestFromClient.getBody().getTagID());
                    System.out.println("GOT FROM CACHE " + valueFromCache);

                    if(valueFromCache == null ) {
                        output = proceedRequest((requestFromClient));

                        cache.addToCache(requestFromClient.getBody().getTagID(),output);

                    }else {
                        output = valueFromCache;
                    }
                }else {
                    output = proceedRequest(requestFromClient);
                }

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

    private String proceedRequest(Request requestFromClient) {
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

        return requestHandelr.makeRequest(requestFromClient);
    }


    private Request readRequest(InputStream in) throws IOException {
        Request requestFromClient = new Request();
        RequestHeader header =  new RequestHeader();

//        System.out.println(new String(chars));
//
//        byte[] buffer = new byte[8192];
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        int bytesRead;
//        while ((bytesRead = in.read(buffer)) != -1)
//        {
//            output.write(buffer, 0, bytesRead);
//        }
//
//        System.out.println(output.toString());

//        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        while (((inputLine = input.readLine()) != null) && (!(inputLine.equals("")))) {
//            try {
//                StringTokenizer tok = new StringTokenizer(inputLine);
//                tok.nextToken();
//            } catch (Exception e) {
//                break;
//            }
//
//            System.out.println(inputLine);
//            parse the first line of the request to find the url
//            String[] tokens = inputLine.split(" ");
//            if (cnt == 0) {
//
//                urlToCall = tokens[1];
//                can redirect this to output log
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
//            if(inputLine.contains("Content-Length:") ) {
//                header.setContentLength(Integer.parseInt(tokens[1]));
//            }
//
//
//            cnt++;
//        }

//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String line;
//        while (!(line = br.readLine()).equals(""))
//            System.out.println(line);
//        br.close();
//


            int br = 0;
            String line = "";

            while ((br = in.read())  != -1 ) {
                line += (char) br;

                String s = new String(new char[] {(char) br});
                if(s.equals("}")) break;

            }


        String[] lines = line.split(System.getProperty("line.separator"));

        int i = 0;
        for(String singleLine : lines)  {
            String[] tokens = singleLine.split(" ");
            if (i == 0) {
                header.setMethod(RequestHeader.Method.valueOf(tokens[0]));
                header.setUrl(tokens[1]);
                header.setHttpVersion(tokens[2]);
            }else if(singleLine.contains(HttpRequestFields.ACCEPT.getField())) {
                header.setAccept(tokens[1]);
            }else if(singleLine.contains(HttpRequestFields.CONTENT_TYPE.getField())) {
                header.setContentType(tokens[1]);
            }else if(singleLine.contains(HttpRequestFields.CONTENT_LENGTH.getField())) {
                header.setContentLength(Integer.parseInt(tokens[1]));
            }else if(singleLine.contains(HttpRequestFields.HOST.getField())) {
                header.setHost(tokens[1]);
            }else if(singleLine.contains(HttpRequestFields.PROXY_CONNECTION.getField())) {
                header.setProxyConnection(tokens[1]);
            }else if(singleLine.equals("")) {

            }else {
               requestFromClient.setBody(new ReqestBody(tokens[0]));
            }

            i++;
        }

        requestFromClient.setHeader(header);

        return requestFromClient;
    }
}