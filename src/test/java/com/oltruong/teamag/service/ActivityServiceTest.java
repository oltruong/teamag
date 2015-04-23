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
        when(mockTypedQuery.getResultList()).thenReturn(activityList);

        List<Activity> activityFoundList = activityService.findActivities();

        assertThat(activityFoundList).isEqualTo(activityList);
        checkCreateTypedQuery("findAllActivities");
        verify(mockTypedQuery).getResultList();
    }

    @Test
    public void testFindActivity() {


        Activity activity = EntityFactory.createActivity();

        when(mockEntityManager.find(eq(Activity.class), anyLong())).thenReturn(activity);

        Activity activityFound = activityService.find(randomLong);

        assertThat(activityFound).isEqualToComparingFieldByField(activity);

        verify(mockEntityManager).find(eq(Activity.class), eq(randomLong));
    }


    @Test
    public void testDeleteActivity() {


        Activity activity = EntityFactory.createActivity();

        when(mockEntityManager.find(eq(Activity.class), anyLong())).thenReturn(activity);

        activityService.remove(randomLong);

        verify(mockEntityManager).find(eq(Activity.class), eq(randomLong));
        verify(mockEntityManager).remove(eq(activity));
    }

    @Test
    public void testUpdateActivity() {
        Activity activity = EntityFactory.createActivity();

        activityService.merge(activity);
        verify(mockEntityManager).merge(eq(activity));
    }

    @Test
    public void testCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        Activity activityCreated = activityService.persist(activity);

        assertThat(activityCreated).isEqualTo(activity);

        checkCreateTypedQuery("findActivity");
        verify(mockTypedQuery).setParameter(eq("fname"), eq(activity.getName()));
        verify(mockTypedQuery).setParameter(eq("fbc"), eq(activity.getBc()));
        verify(mockEntityManager).persist(eq(activity));
    }


    @Test(expected = EntityExistsException.class)
    public void testCreateActivity_existingData() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(mockTypedQuery.getResultList()).thenReturn(activityList);

        activityService.persist(activityList.get(0));
    }


}
