package ProxyServer.methods;

import ProxyServer.request.Request;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.ParameterParser;

import java.io.IOException;
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
            PostMethod post = new PostMethod(request.getHeader().getUrl());

            ParameterParser parameterParser = new ParameterParser();

            char[] separators = "&".toCharArray();
            List<NameValuePair> nameValuePairList = parameterParser.parse(request.getBody(),separators[0]);

            post.setRequestBody(nameValuePairList.toArray(new NameValuePair[nameValuePairList.size()]));
            byte[] in = post.getResponseBody();

            return new String(in);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }
}
