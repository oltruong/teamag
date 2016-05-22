package com.oltruong.teamag.service;

import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceTest {

    protected Long idTest = Long.valueOf(123l);

    @Mock
    protected EntityManager mockEntityManager;

    @Mock
    private Logger mockLogger;


    @Mock
    protected Query mockNamedQuery;
    @Mock
    protected TypedQuery mockTypedQuery;

    protected Long randomLong;

    AbstractService service;

    @Before
    public void setup() {
        randomLong = EntityFactory.createRandomLong();
        when(mockEntityManager.createNamedQuery(isA(String.class))).thenReturn(mockNamedQuery);
        when(mockEntityManager.createNamedQuery(isA(String.class), any())).thenReturn(mockTypedQuery);
        when(mockTypedQuery.setParameter(anyString(), any())).thenReturn(mockTypedQuery);

    }

    protected void prepareService(AbstractService service) {
        this.service = service;
        TestUtils.setPrivateAttribute(service, AbstractService.class, mockEntityManager, "entityManager");
        TestUtils.setPrivateAttribute(service, AbstractService.class, getMockLogger(), "logger");
    }

    protected void checkCreateTypedQuery(String query) {
        verify(mockEntityManager).createNamedQuery(eq(query), any());
    }

    protected void checkParameter(String parameter, Object value) {
        verify(mockTypedQuery).setParameter(eq(parameter), eq(value));
    }

    protected Logger getMockLogger() {
        return mockLogger;
    }


}
