package ProxyServer.methods;

import ProxyServer.request.Request;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.ParameterParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 21.05.13
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class PostRequest implements IRequest {


    @Override
    public String makeRequest(Request request) {
        try {

            HttpPost httpPost = null;
            httpPost = new HttpPost(request.getHeader().getUrl());
            StringEntity body = new StringEntity(request.getBody().getFullBody());
            httpPost.setEntity(body);
            httpPost.setHeader("Accept", request.getHeader().getAccept());
            httpPost.setHeader("Content-type", request.getHeader().getContentType());

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);

            HttpResponse response;


            System.out.println("-----------------------------------------");
            System.out.println("SENDING REQUEST : " + request.toString());

            response = httpclient.execute(httpPost);

            InputStream respone = response.getEntity().getContent();


            int br = 0;
            String line = "";

            while ((br = respone.read())  != -1 ) {
                line += (char) br;
            }

            System.out.println("RESPONSE : " + line);
            System.out.println("-----------------------------------------");

            return line;

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }
}
