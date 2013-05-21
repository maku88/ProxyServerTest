package ProxyServer.methods;

import ProxyServer.request.Request;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 21.05.13
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public interface IRequest {

    public String makeRequest(Request request);

}
