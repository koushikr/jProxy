package com.cosec.jproxy.entities;

import com.cosec.jproxy.configuration.DynamicConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * ServerEntity defines an instance within a cluster.
 *
 * This entity will also contain the instance meta data that will help us in identifying
 * if this instance is still active, can serve requests and the health of the same.
 *
 * @author koushikr
 */
@Getter @Setter
public class ServerEntity {
    private boolean available;
    private boolean secure;
    private String host;
    private int port;
    private String serviceName;
    private String id;
    private static final DynamicConfiguration configuration = new DynamicConfiguration();

    private volatile boolean shortCircuited;
    private volatile long shortCircuitExpiration;
    private volatile long shortCircuitCount;

    /**
     * Used to init a serverEntity using a serviceName and a server.
     * Could be extended to support multiple services (by name) in the future
     * @param serviceName
     * @param server
     */
    public ServerEntity(String serviceName, String server){
        HostAndPort hostAndPort = getHostAndPort(server);
        initialize(serviceName, hostAndPort);
    }

    /**
     * Used to intialize {@link ServerEntity} using the ServiceName and hostAndport information
     * An id for the same will be generated
     *
     * @param serviceName
     * @param hostAndPort
     */
    private void initialize(String serviceName, HostAndPort hostAndPort){
        this.available = true;
        this.serviceName = serviceName;
        this.secure = hostAndPort.isSecure();
        this.host = hostAndPort.getHost();
        this.port = hostAndPort.getPort();
        this.id = hostAndPort.getHost()+":"+hostAndPort.getPort();
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

    public boolean isShortCircuited(){
        if (shortCircuited) {
            long delta = System.currentTimeMillis() - shortCircuitExpiration;
            if (delta >= 0) {
                shortCircuited = false;
            }
        }
        return shortCircuited;
    }

    /**
     * trip the circuit breaker on the server instance. if already tripped,
     * the amount of time the server will remain tripped will be increased
     * exponentially.
     */
    public void tripCircuitBreaker() {
        final long now = System.currentTimeMillis();

        // increment short circuit count for back off calculation
        if ((now - shortCircuitExpiration) > configuration.getShortCircuitDuration()) {
            shortCircuitCount = 0;
        } else {
            shortCircuitCount = Math.max(shortCircuitCount + 1, 5);
        }

        // set time out using exponential back off
        long timeout = (long) (Math.pow(1.5, shortCircuitCount) * configuration.getShortCircuitDuration());
        shortCircuitExpiration = now + timeout;
        shortCircuited = true;
    }

    /**
     * Get the number of seconds until the server instance's circuit breaker is
     * no longer tripped.
     * @return remaining time
     */
    public double getCircuitBreakerRemainingTime() {
        if (isShortCircuited()) {
            long delta = shortCircuitExpiration - System.currentTimeMillis();
            if (delta > 0) {
                return delta / 1000.0;
            }
        }
        return 0.0;
    }


    @AllArgsConstructor
    @Getter
    static class HostAndPort{
        private final String host;
        private final int port;
        private final boolean secure;
    }
}
