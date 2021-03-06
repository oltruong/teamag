package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.ActivityService;
import com.oltruong.teamag.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
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
        TestUtils.setPrivateAttribute(activityEndPoint, AbstractEndPoint.class, mockUriInfo, "uriInfo");

        assertThat(activityEndPoint.getService()).isEqualTo(mockActivityService);
    }


    @Test
    public void getAll() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(mockActivityService.findAll()).thenReturn(activityList);
        Response response = activityEndPoint.getAll();

        checkResponseOK(response);

        List<Activity> activityListReturned = (List<Activity>) response.getEntity();

        assertThat(activityListReturned).isEqualTo(activityList);
        verify(mockActivityService).findAll();
    }


    @Test
    public void getActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.find(eq(randomId))).thenReturn(activity);

        Response response = activityEndPoint.getSingle(randomId);
        checkResponseOK(response);

        Activity activityReturned = (Activity) response.getEntity();

        assertThat(activityReturned).isEqualTo(activity);
        verify(mockActivityService).find(eq(randomId));
    }

    @Test
    public void create() throws Exception {
        Activity activity = EntityFactory.createActivity();
        activity.setId(randomId);
        when(mockActivityService.persist(eq(activity))).thenReturn(activity);
        Response response = activityEndPoint.create(activity);

        checkResponseCreated(response);
        verify(mockActivityService).persist(eq(activity));

    }


    @Test
    public void createExisting() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.persist(eq(activity))).thenThrow(new EntityExistsException());

        Response response = activityEndPoint.create(activity);

        checkResponseNotAcceptable(response);
        verify(mockActivityService).persist(eq(activity));

    }

    @Test
    public void updateActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        assertThat(activity.getId()).isNull();

        Response response = activityEndPoint.updateActivity(randomId, activity);
        checkResponseOK(response);

        assertThat(activity.getId()).isEqualTo(randomId);
        verify(mockActivityService).merge(eq(activity));
    }

    @Test
    public void delete() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.find(randomId)).thenReturn(activity);
        Response response = activityEndPoint.delete(randomId);
        checkResponseNoContent(response);


        verify(mockActivityService).remove(eq(randomId));
    }

    @Test
    public void deleteActivityNotFound() throws Exception {


        doThrow(new EntityNotFoundException()).when(mockActivityService).remove(eq(randomId));
        Response response = activityEndPoint.delete(randomId);
        checkResponseNotFound(response);
    }

}