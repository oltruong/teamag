package com.oltruong.teamag.service;

import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class AbstractServiceTest {

    protected Long idTest = Long.valueOf(123l);

    @Mock
    protected EntityManager mockEntityManager;

    @Mock
    private Logger mockLogger;

    @Mock
    protected Query mockQuery;

    @Mock
    protected TypedQuery mockTypedQuery;

    protected Long randomLong;

    @Before
    public void setup() {
        randomLong = EntityFactory.createRandomLong();
        MockitoAnnotations.initMocks(this);
        when(mockEntityManager.createNamedQuery(isA(String.class))).thenReturn(getMockQuery());
        when(mockEntityManager.createNamedQuery(isA(String.class), any())).thenReturn(mockTypedQuery);
        when(mockTypedQuery.setParameter(anyString(), any())).thenReturn(mockTypedQuery);

    }

    protected void prepareService(AbstractService service) {
        TestUtils.setPrivateAttribute(service, AbstractService.class, mockEntityManager, "entityManager");
        TestUtils.setPrivateAttribute(service, AbstractService.class, getMockLogger(), "logger");

    }

    protected void checkCreateNameQuery(String query) {
        verify(mockEntityManager).createNamedQuery(eq(query));
    }

    protected void checkParameter(String parameter, Object value) {
        verify(mockQuery).setParameter(eq(parameter), eq(value));
    }

    protected Logger getMockLogger() {
        return mockLogger;
    }

    protected Query getMockQuery() {
        return mockQuery;
    }

}
