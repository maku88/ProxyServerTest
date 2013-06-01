package ProxyServer.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 01.06.13
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
@Data
@ToString
@AllArgsConstructor
public class CachedObject {

    private Date cacheDate;
    private String response;


}
