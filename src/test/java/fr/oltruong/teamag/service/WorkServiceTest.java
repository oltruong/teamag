package fr.oltruong.teamag.service;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Olivier Truong
 */
public class WorkServiceTest extends AbstractServiceTest {


    private WorkService workEJB;


    @Before
    public void init() {
        workEJB = new WorkService();
        prepareService(workEJB);

    }

    @Test
    public void testFindAllTasks() {

        workEJB.findAllTasks();
        verify(getMockEntityManager()).createNamedQuery(eq("findAllTasks"));

    }
}
