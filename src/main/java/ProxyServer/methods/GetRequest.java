package ProxyServer.methods;
import ProxyServer.request.Request;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 21.05.13
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class GetRequest implements IRequest {


    @Override
    public String makeRequest(Request request) {

        System.out.println("MAKING GET REQUEST ");
        HttpClient client = new HttpClient();
        // Create a method instance.
        GetMethod method = new GetMethod(request.getHeader().getUrl());

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            String response  = new String(responseBody);
            System.out.println(response);

            return response;

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return "";
    }
}
