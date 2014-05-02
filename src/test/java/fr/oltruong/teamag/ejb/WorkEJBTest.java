package fr.oltruong.teamag.ejb;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Olivier Truong
 */
public class WorkEJBTest extends AbstractEJBTest {


    private WorkEJB workEJB;


    @Before
    public void init() {
        workEJB = new WorkEJB();
        prepareEJB(workEJB);

    }

    @Test
    public void testFindAllTasks() {

        workEJB.findAllTasks();
        verify(getMockEntityManager()).createNamedQuery(eq("findAllTasks"));

    }
}
