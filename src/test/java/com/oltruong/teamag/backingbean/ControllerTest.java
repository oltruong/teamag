package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.utils.MessageManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

/**
 * @author Olivier Truong
 */
public abstract class ControllerTest {

    @Mock
    protected MessageManager mockMessageManager;

    @Mock
    protected Logger mockLogger;


    protected void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
