package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.model.WeekComment;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.WeekCommentService;
import fr.oltruong.teamag.service.WorkService;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.WorkWebBean;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckWorkEndPointTest extends AbstractEndPointTest {

    @Mock
    private WorkService mockWorkService;

    @Mock
    private WeekCommentService mockWeekCommentService;

    private CheckWorkEndPoint checkWorkEndPoint;

    private int currentWeekNumber;

    @Before
    public void prepare() {

        super.setup();
        checkWorkEndPoint = new CheckWorkEndPoint();

        TestUtils.setPrivateAttribute(checkWorkEndPoint, mockWorkService, "workService");
        TestUtils.setPrivateAttribute(checkWorkEndPoint, mockWeekCommentService, "weekCommentService");
        currentWeekNumber = CalendarUtils.getCurrentWeekNumber();
    }


    @Test
    public void testGetWeekComment() {
        int weekNumber = 33;
        testGetWeekComment(weekNumber, weekNumber);
    }


    @Test
    public void testGetWeekComment_noWeekNumber() {
        testGetWeekComment(currentWeekNumber, -1);
    }


    private void testGetWeekComment(int weekNumber, int weekArgument) {

        int year = DateTime.now().getYear();

        WeekComment weekComment = EntityFactory.createWeekComment();
        when(mockWeekCommentService.findWeekComment(eq(randomId), eq(weekNumber), eq(year))).thenReturn(weekComment);

        Response response = checkWorkEndPoint.getWeekComment(randomId, weekArgument);

        checkResponseOK(response);

        WeekComment weekCommentReturned = (WeekComment) response.getEntity();

        assertThat(weekCommentReturned).isEqualTo(weekComment);
        verify(mockWeekCommentService).findWeekComment(eq(randomId), eq(weekNumber), eq(year));
    }


    @Test
    public void testGetWeekComment_null() {
        int weekNumber = 33;
        int year = DateTime.now().getYear();


        Response response = checkWorkEndPoint.getWeekComment(randomId, weekNumber);

        checkResponseOK(response);

        WeekComment weekCommentReturned = (WeekComment) response.getEntity();

        assertThat(weekCommentReturned).isNotNull();

        assertThat(weekCommentReturned.getWeekYear()).isEqualTo(weekNumber);
        assertThat(weekCommentReturned.getComment()).isNullOrEmpty();

        verify(mockWeekCommentService).findWeekComment(eq(randomId), eq(weekNumber), eq(year));
    }


    @Test
    public void testGetWeekInformation() {
        int weekNumber = 33;
        List<Work> workList = EntityFactory.createList(EntityFactory::createWork);
        workList.forEach(work -> {
            work.getTask().setTask(EntityFactory.createTask());
            work.getTask().setActivity(EntityFactory.createActivity());
        });
        List<Work> newWorkList = Lists.newArrayList(workList);
        newWorkList.addAll(workList);

        when(mockWorkService.findWorksNotNullByWeek(eq(randomId), anyInt())).thenReturn(newWorkList);

        Response response = checkWorkEndPoint.getWeekInformation(randomId, weekNumber, true);

        checkResponseOK(response);

        List<WorkWebBean> workWebBeanList = (List<WorkWebBean>) response.getEntity();
        assertThat(workWebBeanList).hasSameSizeAs(workList);
    }

}