package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.utils.MessageManager;
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
