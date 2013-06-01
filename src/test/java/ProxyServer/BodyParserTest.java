package ProxyServer;

import ProxyServer.request.ReqestBody;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 31.05.13
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public class BodyParserTest {

    @Test
    public void parseBody() {

        String body = "{\"mTagId\":\"aaaa\",\"mNetworkType\":\"NETWORK_TYPE_GPRS\",\"mPhoneParametersBundle\":\"b4pF9Jy+sgSd/fKAd3VSl+ahvof5P/03sP5PakfKqH1SGTfE/a252WAMxIYCPOHoFU3+LwG/W3AQmlQ2HBw6kuEptN+ZLQTLAfY6X0t0iRubiGyBQG4SOQHKh1y7aIxAZmTdVCoEktsofY0CkbpmyMF9nBMhrfqoWoH1y9QamUSpxcYcaESiDdbRsBFasDyCRJpDUadmXvAkigM6vmA0mXcpTp2KSi6YSmqA/7XdeL+nENcCZFvcNMpOqxAPm8vgVfy45rxSBGelJt59WtW6pwfhR9cs/94A8Mjkr7SWajsIshFDcIlmyJOeW1EKQ3b/wdy0BA5ez4UDYdZJzwghk9ZGyrVNwPMTwVjzQe4muHJGILtz1Kibqr+Q5cFSHXflmchXtFAYz6g4rqIi5+CwTBio4YwfO0cqfJH+3DrMyzPd0lFjCxcVO+WCxg5rV4BdKwFTQEPyL44XH7suqh1j4IbNvuix3rlUH8TyhXig25LnklNlBBspYbGlZrlj7eY7ymH1HhedDZ5RWbe5YqQVi7+S35PXyLiYt6PL6EipBen6rPJcuZqxPSpdKMbfLfLe\",\"mApplicationVersion\":\"ST.1.0.01\"}";


        ReqestBody requestBody = new ReqestBody(body);

        System.out.println(requestBody);

        assertTrue(requestBody.getFullBody().equals(body));
        assertTrue(requestBody.getTagID().equals("aaaa"));
        assertTrue(requestBody.getAppVersion().equals("ST.1.0.01"));



    }

}
