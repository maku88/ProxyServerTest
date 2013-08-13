package ProxyServer.stats;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 28.07.13
 * Time: 14:19
 * To change this template use File | Settings | File Templates.
 */
public class StatsCollector {

    Logger log = Logger.getLogger(StatsCollector.class);

    private List<RequestStats> simulationResults = Collections.synchronizedList(new ArrayList());

    public void addStats(RequestStats stats) {
        log.info("STATS : " + stats.toString());
        simulationResults.add(stats);
    }

    public List<RequestStats> getSimulationResults() {
        return simulationResults;
    }
}
