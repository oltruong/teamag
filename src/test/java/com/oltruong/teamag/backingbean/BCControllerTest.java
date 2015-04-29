package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.ActivityService;
import com.oltruong.teamag.service.BusinessCaseService;
import com.oltruong.teamag.utils.MessageManager;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.primefaces.event.RowEditEvent;

import javax.persistence.EntityExistsException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class BCControllerTest extends ControllerTest {

    @Mock
    private ActivityService mockActivityService;

    @Mock
    private BusinessCaseService mockBusinessCaseService;

    @Mock
    private RowEditEvent mockRowEditEvent;


    private BCController bcController;


    @Before
    public void setup() {
        super.setup();
        bcController = new BCController();
        bcController.setBc(new BusinessCase());
        bcController.setActivity(new Activity());
        TestUtils.setPrivateAttribute(bcController, mockActivityService, "activityService");
        TestUtils.setPrivateAttribute(bcController, mockBusinessCaseService, "businessCaseService");
        TestUtils.setPrivateAttribute(bcController, Controller.class, mockMessageManager, "messageManager");
        TestUtils.setPrivateAttribute(bcController, Controller.class, mockLogger, "logger");

    }

    @Test
    public void testInit() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        List<BusinessCase> bcList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(mockActivityService.findActivities()).thenReturn(activityList);
        when(mockBusinessCaseService.findAll()).thenReturn(bcList);

        String view = bcController.init();

        assertThat(bcController.getActivityList()).isEqualTo(activityList);
        assertThat(bcController.getBcList()).isEqualTo(bcList);

        checkInitView(view);

        assertThat(bcController.getTotal()).isEqualTo(getSumBc(bcList));
    }


    private Double getSumBc(List<BusinessCase> bcList) {
        Double result = 0d;
        for (BusinessCase businessCase : bcList) {
            result += businessCase.getAmount();
        }
        return result;
    }

    private void checkInitView(String view) {
        verify(mockActivityService).findActivities();
        verify(mockBusinessCaseService).findAll();
        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(bcController, "VIEWNAME"));
    }

    @Test
    public void testDoCreateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        bcController.setBc(businessCase);
        assertThat(bcController.getBc()).isEqualTo(businessCase);

        String view = bcController.doCreateBC();

        verify(mockBusinessCaseService).persist(eq(businessCase));
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.INFORMATION), anyString(), anyString(), anyString(), anyString());

        assertThat(bcController.getBc()).isNotEqualTo(businessCase);

        checkInitView(view);
    }

    @Test
    public void testDoCreateBC_empty() throws Exception {
        String view = bcController.doCreateBC();

        verify(mockBusinessCaseService, never()).persist(isA(BusinessCase.class));
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString());

        checkInitView(view);
    }

    @Test
    public void testDoCreateBC_existing() throws Exception {
        when(mockBusinessCaseService.persist(isA(BusinessCase.class))).thenThrow(new EntityExistsException());
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        bcController.setBc(businessCase);
        String view = bcController.doCreateBC();

        verify(mockBusinessCaseService).persist(eq(businessCase));
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString(), anyString());
        checkInitView(view);


    }

    @Test
    public void testDoCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        activity.getBusinessCase().setId(Long.valueOf(3l));

        bcController.setActivity(activity);
        assertThat(bcController.getActivity()).isEqualTo(activity);

        String view = bcController.doCreateActivity();

        verify(mockActivityService).persist(eq(activity));
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.INFORMATION), anyString(), anyString());
        assertThat(bcController.getActivity()).isNotEqualTo(activity);
        checkInitView(view);
    }

    @Test
    public void testDoCreateActivity_empty() throws Exception {
        String view = bcController.doCreateActivity();

        verify(mockActivityService, never()).persist(isA(Activity.class));
        verify(mockActivityService).findActivities();
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString());

        checkInitView(view);
    }

    @Test
    public void testDoCreateActivity_existing() throws Exception {
        when(mockActivityService.persist(isA(Activity.class))).thenThrow(new EntityExistsException());
        Activity activity = EntityFactory.createActivity();
        activity.getBusinessCase().setId(Long.valueOf(3l));
        bcController.setActivity(activity);
        String view = bcController.doCreateActivity();

        verify(mockActivityService).persist(eq(activity));

        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString());

        checkInitView(view);
    }

    @Test
    public void testTabIndex() throws Exception {
        int tabIndex = 365;
        bcController.setTabIndex(tabIndex);
        assertThat(bcController.getTabIndex()).isEqualTo(tabIndex);
    }


    @Test
    public void testOnEditBC() {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockRowEditEvent.getObject()).thenReturn(businessCase);

        bcController.onEditBC(mockRowEditEvent);

        verify(mockBusinessCaseService).merge(eq(businessCase));
        verify(mockMessageManager).displayMessage(eq(MessageManager.INFORMATION), anyString(), anyString());
    }

    @Test
    public void testOnCancel() {
        bcController.onCancelBC(mockRowEditEvent);
        verifyZeroInteractions(mockActivityService);
    }
}
