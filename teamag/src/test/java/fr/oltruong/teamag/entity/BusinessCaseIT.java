package fr.oltruong.teamag.entity;

import org.junit.Test;

import javax.persistence.RollbackException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BusinessCaseIT extends AbstractEntityIT {
    @Test
    public void testCreation() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();

        getEntityManager().persist(businessCase);
        getTransaction().commit();

        assertThat(businessCase.getId()).isNotNull();
    }

    @Test(expected = RollbackException.class)
    public void testException() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCase.setName(null);

        getEntityManager().persist(businessCase);
        getTransaction().commit();

    }

    @Test
    public void testNamedQuery() {

        BusinessCase businessCase = EntityFactory.createBusinessCase();

        getEntityManager().persist(businessCase);

        getTransaction().commit();
        @SuppressWarnings("unchecked")
        List<BusinessCase> businessCaseList = getEntityManager().createNamedQuery("findAllBC").getResultList();

        assertThat(businessCaseList).isNotNull().isNotEmpty();

    }


}
