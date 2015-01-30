package fr.oltruong.teamag.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author Olivier Truong
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class})
public class LoggingProducerTest {

    private String testMember;


    @Test
    public void testProduceLogger() throws Exception {

        LoggingProducer loggingProducer = new LoggingProducer();

        Class<?> myClass = this.getClass();
        Field chap = myClass.getDeclaredField("testMember");
        mockStatic(LoggerFactory.class);

        Logger mockLogger = mock(Logger.class);
        when(LoggerFactory.getLogger(anyString())).thenReturn(mockLogger);

        InjectionPoint mockInjectionPoint = mock(InjectionPoint.class);

        when(mockInjectionPoint.getMember()).thenReturn(chap);
        Logger logger = loggingProducer.produceLogger(mockInjectionPoint);

        assertThat(logger).isEqualTo(mockLogger);
        verify(LoggerFactory.getLogger(eq("String")));

    }
}