package fr.oltruong.teamag.service;

import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.mockito.Mock;

/**
 * @author Olivier Truong
 */
public class WorkServiceTest extends AbstractServiceTest {


    private WorkService workService;

    @Mock
    private AbsenceDayService absenceDayService;

    @Before
    public void init() {
        super.setup();
        workService = new WorkService();

        TestUtils.setPrivateAttribute(workService, absenceDayService, "absenceDayService");
        prepareService(workService);

    }


}
