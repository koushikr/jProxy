package com.cosec.jproxy.discovery;

import com.cosec.jproxy.entities.ServerEntity;

import java.util.List;

/**
 * This is the defaultServiceList implementation for the strategy {@link ServiceList}
 *
 * This doesn't include any discovery and just works on the list of servers that are provided during startup.
 * This also doesn't perform any eviction but just keeps all servers active
 */
public class DefaultServiceList implements ServiceList {

    private String serviceName;
    private List<ServerEntity> servers;

    public DefaultServiceList(String serviceName, String... servers){

    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public List<ServerEntity> getServiceEntities() {
        return null;
    }
}
