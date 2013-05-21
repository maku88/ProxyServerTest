package toCheck;
import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyThread2 extends Thread {
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;
    public ProxyThread2(Socket socket) throws SocketException {
        super("ProxyThread2");
        this.socket = socket;
        this.socket.setKeepAlive(true);

    }

    public void run() {
        try {
            DataOutputStream out =new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
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

//                System.out.println(inputLine);

                //parse the first line of the request to find the url
                if (cnt == 0) {
                    String[] tokens = inputLine.split(" ");
                    urlToCall = tokens[1];
                    //can redirect this to output log
                    System.out.println("IRequest for : " + urlToCall);
                }

                cnt++;
            }
            //end get request from client
            ///////////////////////////////////


            BufferedReader rd = null;
            try {
                //System.out.println("sending request
                //to real server for url: "
                //        + urlToCall);
                ///////////////////////////////////
                //begin send request to server, get response from server
//                URL url = new URL(urlToCall);
//                URLConnection conn = url.openConnection();
//                conn.setDoInput(true);
//                not doing HTTP posts
//                conn.setDoOutput(false);


                URL url = new URL(urlToCall);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
//                Output os = conn.getOutputStream();
//                os.write(input.getBytes());
//                getBytes.flush();

                InputStream is = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader((is)));
//
//                String output;
//                System.out.println("Output from Server .... \n");
//                while ((output = br.readLine()) != null) {
//                    System.out.println(output);
//                    out.write(output.getBytes());
//                }
//
//                conn.disconnect();


                //System.out.println("Type is: "
                //+ conn.getContentType());
                //System.out.println("content length: "
                //+ conn.getContentLength());
                //System.out.println("allowed user interaction: "
                //+ conn.getAllowUserInteraction());
                //System.out.println("content encoding: "
                //+ conn.getContentEncoding());
                //System.out.println("content type: "
                //+ conn.getContentType());

                // Get the response
//                InputStream is = null;
//                HttpURLConnection huc = (HttpURLConnection)conn;
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
                //begin send response to client
                byte by[] = new byte[ BUFFER_SIZE ];
                int index = is.read( by, 0, BUFFER_SIZE );
                while ( index != -1 )
                {
                    out.write( by, 0, index );
                    index = is.read( by, 0, BUFFER_SIZE );
                }
                out.flush();

                //end send response to client
                ///////////////////////////////////
            } catch (Exception e) {
                System.err.println("Encountered exception: " + e);
                e.printStackTrace();
                out.writeBytes("");
            }

            if (rd != null) {
                rd.close();
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
}
