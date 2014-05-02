package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class AbstractEJBTest {

    protected Long idTest = Long.valueOf(123l);

    @Mock
    protected EntityManager mockEntityManager;

    @Mock
    private Logger mockLogger;

    @Mock
    protected Query mockQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(getMockEntityManager().createNamedQuery(isA(String.class))).thenReturn(getMockQuery());

    }

    protected void prepareEJB(AbstractEJB ejb) {
        TestUtils.setPrivateAttribute(ejb, AbstractEJB.class, getMockEntityManager(), "entityManager");
        TestUtils.setPrivateAttribute(ejb, AbstractEJB.class, getMockLogger(), "logger");

    }

    protected void checkCreateNameQuery(String query) {
        verify(getMockEntityManager()).createNamedQuery(eq(query));
    }

    protected EntityManager getMockEntityManager() {
        return mockEntityManager;
    }

    protected Logger getMockLogger() {
        return mockLogger;
    }

    protected Query getMockQuery() {
        return mockQuery;
    }

}
