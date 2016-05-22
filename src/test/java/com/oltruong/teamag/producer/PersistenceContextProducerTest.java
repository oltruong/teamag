package com.oltruong.teamag.producer;

import com.oltruong.teamag.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceContextProducerTest {

    @Mock
    protected EntityManager mockEntityManager;

    @Test
    public void testBuild() {
        PersistenceContextProducer persistenceContextProducer = new PersistenceContextProducer();
        TestUtils.setPrivateAttribute(persistenceContextProducer, mockEntityManager, "entityManager");
    }

}