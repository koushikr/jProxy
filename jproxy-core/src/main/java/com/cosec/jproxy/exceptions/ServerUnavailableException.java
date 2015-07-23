package com.cosec.jproxy.exceptions;

/**
 * Entity by : ramachandra.as.
 * on 23/07/15.
 */
public class ServerUnavailableException extends JoxyException {

    public ServerUnavailableException(String serviceName) {
        super("There are no available servers for the service ["+ serviceName +"]. Deal with it.");
    }
}
