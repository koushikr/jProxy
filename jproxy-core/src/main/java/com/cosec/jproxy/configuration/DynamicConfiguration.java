package com.cosec.jproxy.configuration;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;

/**
 * This is a dynamicConfiguration Object that will hold onto all the dynamic configurations that you have.
 *
 * Each of these values will have getters and setters and should you want to update them write a configuration
 * manager which will update the values of these attributes and expose APIs on top of 'em should you need to.
 *
 *
 */
@Singleton @Getter @Setter
public class DynamicConfiguration {

    private int shortCircuitDuration = 10000;

}
