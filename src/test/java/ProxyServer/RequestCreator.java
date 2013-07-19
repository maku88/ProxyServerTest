package ProxyServer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 17.05.13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class RequestCreator {
    private static String host = "http://morrigan.mobi-id.pl:8080/rest/api/request/logs";
    private static String pingHost ="http://morrigan.mobi-id.pl:8080/rest/open/ping";
    private static String nuadaHost = "http://smarttouch.mobi-id.pl/rest/open/ping";
    private static String login = "maku17@gmail.com";
    private static String token = "7qvmmpuv8jh4i0p259kmclc4pj";
    private static String dateTo = "2013-05-16";
    private static String dateFrom = "2013-01-01";
    private static List<String> columns = new ArrayList<String>();

    @Test
    public void getPing() {
//        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "10000");
//        System.setProperty("http.proxyType", "4");

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        client.setReadTimeout(10000);
        WebResource webResource = client.resource(nuadaHost);

        ClientResponse response = webResource.get(ClientResponse.class);

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);
    }



    @Test
    public void getPing2() {
//        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "10000");

        try{

          URL url = new URL(nuadaHost);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setDoOutput(true);
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();

    } catch (MalformedURLException e) {

        e.printStackTrace();

    } catch (IOException e) {

        e.printStackTrace();

    }
}


    public static void main(String[] args) {
        jerseyStyle();
    }

    @Test
    public static void jerseyStyle() {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "10000");
        System.setProperty("http.proxyType", "4");


       ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(host);

        MultivaluedMapImpl values = new MultivaluedMapImpl();
        values.add("login", login);
        values.add("token", token);
        values.add("dateFrom", dateFrom);
        values.add("dateTo", dateTo);

        for(String s : columns) {
            values.add("column", s);
        }

        System.out.println("1");
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, values);


        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

    }


    @Test
    public void javaStyle() {
        try {
            System.setProperty("http.proxyHost", "localhost");
            System.setProperty("http.proxyPort", "10000");

            URL url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            String colString = "";
            for(String col : columns) {
                colString += "&column="+col;
            }

            String input = "login="+login+"&token="+token+"&dateFrom="+dateFrom+"&dateTo="+dateTo+colString;

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


    @Test
    public void testCache() {

    }


}
