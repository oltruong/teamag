package fr.oltruong.teamag.producer;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
	return LoggerFactory.getLogger(injectionPoint.getMember()
		.getDeclaringClass().getName());
    }
}
