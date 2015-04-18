package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BusinessCaseIT extends AbstractEntityIT {


    @Test
    public void testCreateAndFind() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();

        entityManager.persist(businessCase);
        transaction.commit();

        assertThat(businessCase.getId()).isNotNull();

        BusinessCase businessCaseDB = entityManager.find(BusinessCase.class, businessCase.getId());
        assertThat(businessCaseDB).isEqualToComparingFieldByField(businessCase).isEqualTo(businessCase);
    }


    @Test(expected = PersistenceException.class)
    public void testException() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCase.setName(null);

        entityManager.persist(businessCase);
        transaction.commit();

    }

    @Test
    public void testNamedQuery() {

        BusinessCase businessCase = EntityFactory.createBusinessCase();

        entityManager.persist(businessCase);

        transaction.commit();
        @SuppressWarnings("unchecked")
        List<BusinessCase> businessCaseList = entityManager.createNamedQuery("findAllBC").getResultList();

        Assertions.assertThat(businessCaseList).isNotNull().isNotEmpty();

    }


}
