package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.utils.MessageManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Olivier Truong
 */
public abstract class ControllerTest {

    @Mock
    protected MessageManager mockMessageManager;


    protected void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
