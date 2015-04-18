package com.oltruong.teamag.service;

import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityExistsException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
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


    @Test(expected = EntityExistsException.class)
    public void testCreateActivity_existingData() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(getMockQuery().getResultList()).thenReturn(activityList);

        activityService.createActivity(activityList.get(0));
    }


}
