package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.ActivityService;
import fr.oltruong.teamag.service.BusinessCaseService;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BusinessEndPointTest extends AbstractEndPointTest {


    @Mock
    ActivityService mockActivityService;

    @Mock
    BusinessCaseService mockBusinessCaseService;

    BusinessEndPoint businessEndPoint;


    @Before
    public void prepare() {
        super.setup();
        businessEndPoint = new BusinessEndPoint();
        TestUtils.setPrivateAttribute(businessEndPoint, mockBusinessCaseService, "businessCaseService");
        TestUtils.setPrivateAttribute(businessEndPoint, mockActivityService, "activityService");

    }

    @Test
    public void testGetBC() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(mockBusinessCaseService.findAll()).thenReturn(businessCaseList);
        Response response = businessEndPoint.getBC();

        checkResponseOK(response);

        List<BusinessCase> businessCasesReturned = (List<BusinessCase>) response.getEntity();

        assertThat(businessCasesReturned).isEqualTo(businessCaseList);
        verify(mockBusinessCaseService).findAll();
    }

    @Test
    public void testGetBC_byId() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.find(eq(randomId))).thenReturn(businessCase);

        Response response = businessEndPoint.getBC(randomId);
        checkResponseOK(response);

        BusinessCase businessCaseReturned = (BusinessCase) response.getEntity();

        assertThat(businessCaseReturned).isEqualTo(businessCase);
        verify(mockBusinessCaseService).find(eq(randomId));

    }

    @Test
    public void testGetActivities() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(mockActivityService.findActivities()).thenReturn(activityList);
        Response response = businessEndPoint.getActivities();

        checkResponseOK(response);

        List<Activity> activityListReturned = (List<Activity>) response.getEntity();

        assertThat(activityListReturned).isEqualTo(activityList);
        verify(mockActivityService).findActivities();
    }


    @Test
    public void testGetActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.findActivity(eq(randomId))).thenReturn(activity);

        Response response = businessEndPoint.getActivity(randomId);
        checkResponseOK(response);

        Activity activityReturned = (Activity) response.getEntity();

        assertThat(activityReturned).isEqualTo(activity);
        verify(mockActivityService).findActivity(eq(randomId));
    }

    @Test
    public void testCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.createActivity(eq(activity))).thenReturn(activity);
        Response response = businessEndPoint.createActivity(activity);

        checkResponseCreated(response);
        verify(mockActivityService).createActivity(eq(activity));

    }


    @Test
    public void testCreateActivity_existing() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.createActivity(eq(activity))).thenThrow(new ExistingDataException());

        Response response = businessEndPoint.createActivity(activity);

        checkResponseNotAcceptable(response);
        verify(mockActivityService).createActivity(eq(activity));

    }

    @Test
    public void testUpdateActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        assertThat(activity.getId()).isNull();

        Response response = businessEndPoint.updateActivity(randomId, activity);
        checkResponseOK(response);

        assertThat(activity.getId()).isEqualTo(randomId);
        verify(mockActivityService).updateActivity(eq(activity));
    }

    @Test
    public void testDeleteActivity() throws Exception {

        Response response = businessEndPoint.deleteActivity(randomId);
        checkResponseOK(response);


        verify(mockActivityService).deleteActivity(eq(randomId));
    }

    @Test
    public void testCreateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.create(eq(businessCase))).thenReturn(businessCase);
        Response response = businessEndPoint.createBC(businessCase);

        checkResponseCreated(response);
        verify(mockBusinessCaseService).create(eq(businessCase));

    }


    @Test
    public void testCreateBC_existing() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.create(eq(businessCase))).thenThrow(new ExistingDataException());
        Response response = businessEndPoint.createBC(businessCase);

        checkResponseNotAcceptable(response);
        verify(mockBusinessCaseService).create(eq(businessCase));

    }

    @Test
    public void testUpdateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        assertThat(businessCase.getId()).isNull();

        Response response = businessEndPoint.updateBC(randomId, businessCase);
        checkResponseOK(response);

        assertThat(businessCase.getId()).isEqualTo(randomId);
        verify(mockBusinessCaseService).update(eq(businessCase));
    }

    @Test
    public void testDeleteBC() throws Exception {
        Response response = businessEndPoint.deleteBC(randomId);
        checkResponseOK(response);


        verify(mockBusinessCaseService).delete(eq(randomId));
    }
}