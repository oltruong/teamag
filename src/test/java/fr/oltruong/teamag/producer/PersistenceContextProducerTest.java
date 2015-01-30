package fr.oltruong.teamag.producer;

import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

public class PersistenceContextProducerTest {

    @Mock
    protected EntityManager mockEntityManager;

    @Test
    public void testBuild() {
        MockitoAnnotations.initMocks(this);
        PersistenceContextProducer persistenceContextProducer = new PersistenceContextProducer();
        TestUtils.setPrivateAttribute(persistenceContextProducer, mockEntityManager, "entityManager");
    }

}