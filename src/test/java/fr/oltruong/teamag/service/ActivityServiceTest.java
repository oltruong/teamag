package fr.oltruong.teamag.service;

import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class ActivityServiceTest extends AbstractServiceTest {


    private ActivityService activityService;


    @Before
    public void prepare() {
        activityService = new ActivityService();
        prepareService(activityService);


    }

    @Test
    public void testFindBC() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(getMockQuery().getResultList()).thenReturn(businessCaseList);

        List<BusinessCase> businessCaseFoundList = activityService.findBC();

        assertThat(businessCaseFoundList).isEqualTo(businessCaseList);
        verify(mockEntityManager).createNamedQuery(eq("findAllBC"));
        verify(getMockQuery()).getResultList();
    }

    @Test
    public void testCreateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        BusinessCase businessCaseCreated = activityService.createBC(businessCase);


        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);

        MemberService memberService = new MemberService();

        TestUtils.setPrivateAttribute(memberService, memberList, "memberList");

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(mockEntityManager).createNamedQuery(eq("findBCByNumber"));
        verify(getMockQuery()).setParameter(eq("fidentifier"), isA(String.class));

        verify(mockEntityManager).persist(eq(businessCase));

        verify(mockEntityManager).persist(any(WorkLoad.class));


    }

    @Test(expected = ExistingDataException.class)
    public void testCreateBC_existingData() throws Exception {

        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(getMockQuery().getResultList()).thenReturn(businessCaseList);

        BusinessCase businessCaseCreated = activityService.createBC(businessCaseList.get(0));
    }


    @Test
    public void testCreateBusinessCase_noIdentifier() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        businessCase.setIdentifier(null);
        BusinessCase businessCaseCreated = activityService.createBC(businessCase);

        assertThat(businessCaseCreated).isEqualTo(businessCase);

        verify(mockEntityManager, never()).createNamedQuery(eq("findBCByNumber"));
        verify(getMockQuery(), never()).setParameter(eq("fidentifier"), isA(String.class));
        verify(mockEntityManager).persist(eq(businessCase));
    }

    @Test
    public void testFindActivities() throws Exception {

        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(getMockQuery().getResultList()).thenReturn(activityList);

        List<Activity> activityFoundList = activityService.findActivities();

        assertThat(activityFoundList).isEqualTo(activityList);
        verify(mockEntityManager).createNamedQuery(eq("findAllActivities"));
        verify(getMockQuery()).getResultList();
    }

    @Test
    public void testFindActivity() {


        Activity activity = EntityFactory.createActivity();

        when(mockEntityManager.find(eq(Activity.class), anyLong())).thenReturn(activity);

        Activity activityFound = activityService.findActivity(randomLong);

        assertThat(activityFound).isEqualToComparingFieldByField(activity);

        verify(mockEntityManager).find(eq(Activity.class), eq(randomLong));
    }


    @Test
    public void testDeleteActivity() {


        Activity activity = EntityFactory.createActivity();

        when(mockEntityManager.find(eq(Activity.class), anyLong())).thenReturn(activity);

        activityService.deleteActivity(randomLong);

        verify(mockEntityManager).find(eq(Activity.class), eq(randomLong));
        verify(mockEntityManager).remove(eq(activity));
    }

    @Test
    public void testUpdateActivity() {
        Activity activity = EntityFactory.createActivity();

        activityService.updateActivity(activity);
        verify(mockEntityManager).merge(eq(activity));
    }

    @Test
    public void testCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        Activity activityCreated = activityService.createActivity(activity);

        assertThat(activityCreated).isEqualTo(activity);

        verify(mockEntityManager).createNamedQuery(eq("findActivity"));
        verify(getMockQuery()).setParameter(eq("fname"), eq(activity.getName()));
        verify(getMockQuery()).setParameter(eq("fbc"), eq(activity.getBc()));
        verify(mockEntityManager).persist(eq(activity));
    }


    @Test(expected = ExistingDataException.class)
    public void testCreateActivity_existingData() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(getMockQuery().getResultList()).thenReturn(activityList);

        activityService.createActivity(activityList.get(0));
    }


    @Test
    public void testUpdateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        activityService.updateBC(businessCase);

        verify(mockEntityManager).merge(eq(businessCase));
    }


    @Test
    public void testDeleteBC() {


        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockEntityManager.find(eq(BusinessCase.class), anyLong())).thenReturn(businessCase);
        activityService.deleteBC(randomLong);

        verify(mockEntityManager).find(eq(BusinessCase.class), eq(randomLong));
        verify(mockEntityManager).remove(eq(businessCase));
    }

    @Test
    public void testFindSingleBC() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockEntityManager.find(eq(BusinessCase.class), anyLong())).thenReturn(businessCase);

        BusinessCase businessCaseFound = activityService.findBC(randomLong);

        assertThat(businessCaseFound).isEqualToComparingFieldByField(businessCase);

        verify(mockEntityManager).find(eq(BusinessCase.class), eq(randomLong));

    }
}
