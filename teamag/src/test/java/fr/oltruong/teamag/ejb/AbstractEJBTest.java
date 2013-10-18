package fr.oltruong.teamag.ejb;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.slf4j.Logger;

public abstract class AbstractEJBTest {

    private EntityManager mockEntityManager;

    private Logger mockLogger;

    private Query mockQuery;

    @Before
    public void setup() {
        mockEntityManager = mock(EntityManager.class);
        mockLogger = mock(Logger.class);
        mockQuery = mock(Query.class);

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
