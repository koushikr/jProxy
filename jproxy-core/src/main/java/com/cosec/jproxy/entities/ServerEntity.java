package com.cosec.jproxy.entities;

import lombok.AllArgsConstructor;

/**
 * ServerEntity defines an instance within a cluster.
 *
 * This entity will also contain the instance meta data that will help us in identifying
 * if this instance is still active, can serve requests and the health of the same.
 *
 * @author koushikr
 */
public class ServerEntity {
    private boolean available;
    private boolean secure;
    private String host;
    private int port;
    private String serviceName;
    private String id;

    /**
     * Used to init a serverEntity using a serviceName and a server.
     * Could be extended to support multiple services (by name) in the future
     * @param serviceName
     * @param server
     */
    public ServerEntity(String serviceName, String server){

    }

    private static HostAndPort getHostAndPort(String url){
        boolean secure = false;
        if (url.toLowerCase().startsWith("http://")) {
            secure = false;
            url = url.substring(7);
        } else if (url.toLowerCase().startsWith("https://")) {
            secure = true;
            url = url.substring(8);
        }

        if (url.contains("/")) {
            int slashIdx = url.indexOf('/');
            url = url.substring(0, slashIdx);
        }

        String host;
        int port;
        int colonIdx = url.indexOf(':');
        if (colonIdx == -1) {
            host = url; // default
            port = 80;
        } else {
            host = url.substring(0, colonIdx);
            port = Integer.valueOf(url.substring(colonIdx + 1));
        }
        return new HostAndPort(host, port, secure);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + serviceName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServerEntity that = (ServerEntity) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!serviceName.equals(that.serviceName)) {
            return false;
        }

        return true;
    }


    @AllArgsConstructor
    static class HostAndPort{
        private final String host;
        private final int port;
        private final boolean secure;
    }
}
