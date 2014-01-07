package fr.oltruong.teamag.entity;

import org.junit.Test;

import javax.persistence.RollbackException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BusinessCaseIT extends AbstractEntityIT {
    @Test
    public void testCreation() {
        BusinessCase businessCase = createBusinessCase(99);

        getEntityManager().persist(businessCase);
        getTransaction().commit();

        assertThat(businessCase.getNumber()).isNotNull();
    }

    @Test(expected = RollbackException.class)
    public void testException() {
        BusinessCase businessCase = createBusinessCase(567);
        businessCase.setName(null);

        getEntityManager().persist(businessCase);
        getTransaction().commit();

    }

    @Test
    public void testNamedQuery() {

        BusinessCase businessCase = createBusinessCase(1234);

        getEntityManager().persist(businessCase);

        getTransaction().commit();
        @SuppressWarnings("unchecked")
        List<BusinessCase> businessCaseList = getEntityManager().createNamedQuery("findAllBC").getResultList();

        assertThat(businessCaseList).isNotNull().isNotEmpty();

    }

    private BusinessCase createBusinessCase(int number) {
        BusinessCase businessCase = new BusinessCase();

        businessCase.setName("My BC");
        businessCase.setNumber(Integer.valueOf(number));
        businessCase.setAmount(Float.valueOf(3.5f));
        businessCase.setComment("Hello");

        return businessCase;
    }

}
