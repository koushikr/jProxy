package com.cosec.jproxy.discovery;

import com.cosec.jproxy.entities.ServerEntity;

import java.util.List;

/**
 * Service list is the strategy for discovering a list of service instances in a single cluster
 *
 * Should you wish to implement your own servicelist discovery mechanism, you'll need to override
 * the getServiceInstance implementation
 * @author koushikr
 */
public interface ServiceList {

    /**
     * Gets the name of the cluster that this service is pertaining to.
     * @return the name of the service
     */
    String getServiceName();

    /**
     * Gets the available service Entites in the said the cluster
     * @return list of {@link ServerEntity}
     */
    List<ServerEntity> getServiceEntities();
}
