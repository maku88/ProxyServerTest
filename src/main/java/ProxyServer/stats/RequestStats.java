package ProxyServer.stats;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 28.07.13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
@Data
public class RequestStats implements Serializable {

    @Override
    public String toString() {
        return simulationID + ";" + cacheType + ";" + responseType +";" + tagID + ";" + startTimestamp + ";" + endTimestamp + ";" + duration + ";" + response ;
    }

    private int simulationID;
    private String cacheType;
    private ResponseType responseType;
    private String tagID;
    private long startTimestamp;
    private long endTimestamp;
    private long duration;
    private String response;



}
