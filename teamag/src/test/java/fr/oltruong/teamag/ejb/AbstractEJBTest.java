package fr.oltruong.teamag.ejb;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

public abstract class AbstractEJBTest {

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private Logger mockLogger;

    @Mock
    private Query mockQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(getMockEntityManager().createNamedQuery(isA(String.class))).thenReturn(getMockQuery());
    }

    public EntityManager getMockEntityManager() {
        return mockEntityManager;
    }

    public Logger getMockLogger() {
        return mockLogger;
    }

    public Query getMockQuery() {
        return mockQuery;
    }

}
