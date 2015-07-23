package com.cosec.jproxy.distribution;
import com.cosec.jproxy.entities.ServerStatistics;

import java.util.List;

/**
 * Distributor is the strategy that you will need to implement to decide the kind of loadbalancing that
 * you'll need.
 *
 * Distributor is going to use the {@link ServerStatistics} to be able to figure out how distribution
 * should happen and what are the various strategies to use
 */
public interface Distributor {

    /**
     * Choose a viable server from the list of Server Statistics. The list will contain all the available
     * server statistics and the implementation of this distributor is responsible for filtering the ones
     * that aren't needed
     * @param serverStatisticses
     * @return the available {@link ServerStatistics} or null otherwise
     */
    ServerStatistics selectServer(List<ServerStatistics> serverStatisticses);

}
