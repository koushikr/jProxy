package com.cosec.jproxy.discovery;

import com.cosec.jproxy.configuration.DynamicConfiguration;
import com.cosec.jproxy.entities.ServerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the defaultServiceList implementation for the strategy {@link ServiceList}
 *
 * This doesn't include any discovery and just works on the list of servers that are provided during startup.
 * This also doesn't perform any eviction but just keeps all servers active
 */
public class DefaultServiceList implements ServiceList {

    private String serviceName;
    private List<ServerEntity> serverEntities = new ArrayList();

    public DefaultServiceList(String serviceName, String... servers){
        this.serviceName = serviceName;
        serverEntities = new ArrayList();
        for(String server : servers){
            if(Objects.isNull(server)) continue;
            serverEntities.add(new ServerEntity(serviceName, server));
        }
    }

    /**
     * Gets the name of the service
     * @return the service name
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Gets the list of serviceEntites associated with the strategy
     * @return
     */
    @Override
    public List<ServerEntity> getServiceEntities() {
        return serverEntities;
    }
}
