package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.ActivityService;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActivityEndPointTest extends AbstractEndPointTest {


    @Mock
    ActivityService mockActivityService;

    ActivityEndPoint activityEndPoint;

    @Before
    public void prepare() {
        super.setup();
        activityEndPoint = new ActivityEndPoint();
        TestUtils.setPrivateAttribute(activityEndPoint, mockActivityService, "activityService");
        TestUtils.setPrivateAttribute(activityEndPoint, AbstractEndPoint.class, mockLogger, "LOGGER");
    }


    @Test
    public void testGetActivities() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(mockActivityService.findActivities()).thenReturn(activityList);
        Response response = activityEndPoint.getActivities();

        checkResponseOK(response);

        List<Activity> activityListReturned = (List<Activity>) response.getEntity();

        assertThat(activityListReturned).isEqualTo(activityList);
        verify(mockActivityService).findActivities();
    }


    @Test
    public void testGetActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.find(eq(randomId))).thenReturn(activity);

        Response response = activityEndPoint.getActivity(randomId);
        checkResponseOK(response);

        Activity activityReturned = (Activity) response.getEntity();

        assertThat(activityReturned).isEqualTo(activity);
        verify(mockActivityService).find(eq(randomId));
    }

    @Test
    public void testCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.persist(eq(activity))).thenReturn(activity);
        Response response = activityEndPoint.createActivity(activity);

        checkResponseCreated(response);
        verify(mockActivityService).persist(eq(activity));

    }


    @Test
    public void testCreateActivity_existing() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.persist(eq(activity))).thenThrow(new EntityExistsException());

        Response response = activityEndPoint.createActivity(activity);

        checkResponseNotAcceptable(response);
        verify(mockActivityService).persist(eq(activity));

    }

    @Test
    public void testUpdateActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        assertThat(activity.getId()).isNull();

        Response response = activityEndPoint.updateActivity(randomId, activity);
        checkResponseOK(response);

        assertThat(activity.getId()).isEqualTo(randomId);
        verify(mockActivityService).merge(eq(activity));
    }

    @Test
    public void testDeleteActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.find(randomId)).thenReturn(activity);
        Response response = activityEndPoint.deleteActivity(randomId);
        checkResponseNoContent(response);


        verify(mockActivityService).remove(eq(activity));
    }

    @Test
    public void testDeleteActivity_notFound() throws Exception {

        Response response = activityEndPoint.deleteActivity(randomId);
        checkResponseNotFound(response);


        verify(mockActivityService).find(eq(randomId));
        verify(mockActivityService, never()).remove(any(Activity.class));
    }

}