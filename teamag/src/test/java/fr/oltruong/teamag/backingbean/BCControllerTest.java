package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.utils.MessageManager;
import fr.oltruong.teamag.utils.TestUtils;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.primefaces.event.RowEditEvent;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Olivier Truong
 */
public class BCControllerTest extends ControllerTest {

    @Mock
    private ActivityEJB mockActivityEJB;

    @Mock
    private RowEditEvent mockRowEditEvent;


    private BCController bcController;


    @Before
    public void setup() {
        super.setup();
        bcController = new BCController();
        bcController.setBc(new BusinessCase());
        bcController.setActivity(new Activity());
        TestUtils.setPrivateAttribute(bcController, mockActivityEJB, "activityEJB");
        TestUtils.setPrivateAttribute(bcController, Controller.class, mockMessageManager, "messageManager");
        TestUtils.setPrivateAttribute(bcController, Controller.class, mockLogger, "logger");

    }

    @Test
    public void testInit() throws Exception {
        List<Activity> activityList = EntityFactory.createActivityList(5);
        List<BusinessCase> bcList = EntityFactory.createBCList(5);
        when(mockActivityEJB.findActivities()).thenReturn(activityList);
        when(mockActivityEJB.findBC()).thenReturn(bcList);

        String view = bcController.init();

        assertThat(bcController.getActivityList()).isEqualTo(activityList);
        assertThat(bcController.getBcList()).isEqualTo(bcList);

        checkInitView(view);

        assertThat(bcController.getTotal()).isEqualTo(getSumBc(bcList));
    }


    private Float getSumBc(List<BusinessCase> bcList) {
        Float result = 0f;
        for (BusinessCase businessCase : bcList) {
            result += businessCase.getAmount();
        }
        return result;
    }

    private void checkInitView(String view) {
        verify(mockActivityEJB).findActivities();
        verify(mockActivityEJB).findBC();
        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(bcController, "VIEWNAME"));
    }

    @Test
    public void testDoCreateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        bcController.setBc(businessCase);
        assertThat(bcController.getBc()).isEqualTo(businessCase);

        String view = bcController.doCreateBC();

        verify(mockActivityEJB).createBC(eq(businessCase));
        verify(mockMessageManager).displayMessage(eq(MessageManager.INFORMATION), anyString(), anyString(), anyString(), anyString());

        assertThat(bcController.getBc()).isNotEqualTo(businessCase);

        checkInitView(view);
    }

    @Test
    public void testDoCreateBC_empty() throws Exception {
        String view = bcController.doCreateBC();

        verify(mockActivityEJB, never()).createBC(isA(BusinessCase.class));
        verify(mockMessageManager).displayMessage(eq(MessageManager.ERROR), anyString(), anyString());

        checkInitView(view);
    }

    @Test
    public void testDoCreateBC_existing() throws Exception {
        when(mockActivityEJB.createBC(isA(BusinessCase.class))).thenThrow(new ExistingDataException());
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        bcController.setBc(businessCase);
        String view = bcController.doCreateBC();

        verify(mockActivityEJB).createBC(eq(businessCase));
        verify(mockMessageManager).displayMessage(eq(MessageManager.ERROR), anyString(), anyString(), anyString());
        checkInitView(view);


    }

    @Test
    public void testDoCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        activity.getBc().setId(Long.valueOf(3l));

        bcController.setActivity(activity);
        assertThat(bcController.getActivity()).isEqualTo(activity);

        String view = bcController.doCreateActivity();

        verify(mockActivityEJB).createActivity(eq(activity));
        verify(mockMessageManager).displayMessage(eq(MessageManager.INFORMATION), anyString(), anyString());
        assertThat(bcController.getActivity()).isNotEqualTo(activity);
        checkInitView(view);
    }

    @Test
    public void testDoCreateActivity_empty() throws Exception {
        String view = bcController.doCreateActivity();

        verify(mockActivityEJB, never()).createActivity(isA(Activity.class));
        verify(mockActivityEJB).findActivities();
        verify(mockMessageManager).displayMessage(eq(MessageManager.ERROR), anyString(), anyString());

        checkInitView(view);
    }

    @Test
    public void testDoCreateActivity_existing() throws Exception {
        when(mockActivityEJB.createActivity(isA(Activity.class))).thenThrow(new ExistingDataException());
        Activity activity = EntityFactory.createActivity();
        activity.getBc().setId(Long.valueOf(3l));
        bcController.setActivity(activity);
        String view = bcController.doCreateActivity();

        verify(mockActivityEJB).createActivity(eq(activity));

        verify(mockMessageManager).displayMessage(eq(MessageManager.ERROR), anyString(), anyString());

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

        verify(mockActivityEJB).updateBC(eq(businessCase));
        verify(mockMessageManager).displayMessage(eq(MessageManager.INFORMATION), anyString(), anyString());
    }

    @Test
    public void testOnCancel() {
        bcController.onCancelBC(mockRowEditEvent);
        verifyZeroInteractions(mockActivityEJB);
    }
}
