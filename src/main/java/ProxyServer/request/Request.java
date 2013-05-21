package ProxyServer.request;

import lombok.Data;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * User: mno
 * Date: 21.05.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
@Data
@ToString
public class Request {

    private RequestHeader header;
    private String body;

}
