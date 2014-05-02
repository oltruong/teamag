package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.exception.ExistingDataException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Olivier Truong
 */
public class ActivityEJBTest extends AbstractEJBTest {


    private ActivityEJB activityEJB;

    @Before
    public void prepare() {
        activityEJB = new ActivityEJB();
        prepareEJB(activityEJB);
    }

    @Test
    public void testFindBC() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(getMockQuery().getResultList()).thenReturn(businessCaseList);

        List<BusinessCase> businessCaseFoundList = activityEJB.findBC();

        assertThat(businessCaseFoundList).isEqualTo(businessCaseList);
        verify(getMockEntityManager()).createNamedQuery(eq("findAllBC"));
        verify(getMockQuery()).getResultList();
    }

    @Test
    public void testCreateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        BusinessCase businessCaseCreated = activityEJB.createBC(businessCase);

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(getMockEntityManager()).createNamedQuery(eq("findBCByNumber"));
        verify(getMockQuery()).setParameter(eq("fidentifier"), isA(String.class));
        verify(getMockEntityManager()).persist(eq(businessCase));
    }

    @Test(expected = ExistingDataException.class)
    public void testCreateBC_existingData() throws Exception {

        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(getMockQuery().getResultList()).thenReturn(businessCaseList);

        BusinessCase businessCaseCreated = activityEJB.createBC(businessCaseList.get(0));
    }


    @Test
    public void testCreateBusinessCase_noIdentifier() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCase.setIdentifier(null);
        BusinessCase businessCaseCreated = activityEJB.createBC(businessCase);

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(getMockEntityManager(), never()).createNamedQuery(eq("findBCByNumber"));
        verify(getMockQuery(), never()).setParameter(eq("fidentifier"), isA(String.class));
        verify(getMockEntityManager()).persist(eq(businessCase));
    }

    @Test
    public void testFindActivities() throws Exception {

        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(getMockQuery().getResultList()).thenReturn(activityList);

        List<Activity> activityFoundList = activityEJB.findActivities();

        assertThat(activityFoundList).isEqualTo(activityList);
        verify(getMockEntityManager()).createNamedQuery(eq("findAllActivities"));
        verify(getMockQuery()).getResultList();
    }

    @Test
    public void testCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        Activity activityCreated = activityEJB.createActivity(activity);

        assertThat(activityCreated).isEqualTo(activity);

        verify(getMockEntityManager()).createNamedQuery(eq("findActivity"));
        verify(getMockQuery()).setParameter(eq("fname"), eq(activity.getName()));
        verify(getMockQuery()).setParameter(eq("fbc"), eq(activity.getBc()));
        verify(getMockEntityManager()).persist(eq(activity));
    }


    @Test(expected = ExistingDataException.class)
    public void testCreateActivity_existingData() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(getMockQuery().getResultList()).thenReturn(activityList);

        activityEJB.createActivity(activityList.get(0));
    }


    @Test
    public void testUpdateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        activityEJB.updateBC(businessCase);

        verify(getMockEntityManager()).merge(eq(businessCase));
    }
}
