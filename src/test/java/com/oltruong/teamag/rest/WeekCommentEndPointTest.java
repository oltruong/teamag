package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.WeekCommentService;
import com.oltruong.teamag.utils.TestUtils;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WeekCommentEndPointTest extends AbstractEndPointTest {

    private WeekCommentEndPoint weekCommentEndPoint;


    @Mock
    private WeekCommentService mockWeekCommentService;

    @Before
    public void prepare() {

        super.setup();
        weekCommentEndPoint = new WeekCommentEndPoint();

        TestUtils.setPrivateAttribute(weekCommentEndPoint, mockWeekCommentService, "weekCommentService");
    }

    private void get(int weekNumber, int weekArgument) {

        int year = DateTime.now().getYear();
        int month = DateTime.now().getMonthOfYear();

        WeekComment weekComment = EntityFactory.createWeekComment();
        when(mockWeekCommentService.findWeekComment(eq(randomId), eq(weekNumber), eq(month), eq(year))).thenReturn(weekComment);

        Response response = weekCommentEndPoint.get(randomId, randomId, weekArgument, month, year);

        checkResponseOK(response);

        WeekComment weekCommentReturned = (WeekComment) response.getEntity();

        assertThat(weekCommentReturned).isEqualTo(weekComment);
        verify(mockWeekCommentService).findWeekComment(eq(randomId), eq(weekNumber), eq(month), eq(year));
    }


    @Test
    public void getNull() {
        int weekNumber = 33;
        int year = DateTime.now().getYear();
        int month = DateTime.now().getMonthOfYear();

        Response response = weekCommentEndPoint.get(randomId, null, weekNumber, month, year);

        checkResponseNoContent(response);

        verify(mockWeekCommentService).findWeekComment(eq(randomId), eq(weekNumber), eq(month), eq(year));
    }

    @Test
    public void get() {
        int weekNumber = 33;
        get(weekNumber, weekNumber);
    }

    @Test
    public void delete() throws Exception {
        WeekComment weekComment = EntityFactory.createWeekComment();
        final Member member = EntityFactory.createMember();
        member.setId(randomId);
        weekComment.setMember(member);
        when(mockWeekCommentService.find(randomId)).thenReturn(weekComment);
        final Response response = weekCommentEndPoint.delete(randomId, randomId);
        checkResponseNoContent(response);
        verify(mockWeekCommentService).remove(randomId);
    }

    @Test
    public void deleteNotFound() throws Exception {
        final Response response = weekCommentEndPoint.delete(randomId, randomId);
        checkResponseNotFound(response);
        verify(mockWeekCommentService).find(randomId);
    }

    @Test
    public void deleteForbidden() throws Exception {
        WeekComment weekComment = EntityFactory.createWeekComment();
        final Member member = EntityFactory.createMember();
        member.setId(randomId + 1);
        weekComment.setMember(member);
        when(mockWeekCommentService.find(randomId)).thenReturn(weekComment);
        final Response response = weekCommentEndPoint.delete(randomId, randomId);
        checkResponseForbidden(response);
        verify(mockWeekCommentService).find(randomId);
    }

    @Test
    public void patch() throws Exception {
        WeekComment weekComment = EntityFactory.createWeekComment();
        final Member member = EntityFactory.createMember();
        member.setId(randomId);
        weekComment.setMember(member);
        when(mockWeekCommentService.find(randomId)).thenReturn(weekComment);

        WeekComment weekCommentArgument = EntityFactory.createWeekComment();
        weekCommentArgument.setComment("comment");
        final Response response = weekCommentEndPoint.patch(randomId, randomId, weekCommentArgument);
        checkResponseOK(response);
        verify(mockWeekCommentService).merge(weekComment);
        assertThat(weekCommentArgument.getComment()).isEqualTo("comment");
    }


    @Test
    public void patchNotFound() throws Exception {
        WeekComment weekComment = EntityFactory.createWeekComment();
        final Response response = weekCommentEndPoint.patch(randomId, randomId, weekComment);
        checkResponseNotFound(response);
        verify(mockWeekCommentService).find(randomId);

    }

    @Test
    public void patchForbidden() throws Exception {
        WeekComment weekComment = EntityFactory.createWeekComment();
        final Member member = EntityFactory.createMember();
        member.setId(randomId + 1);
        weekComment.setMember(member);
        when(mockWeekCommentService.find(randomId)).thenReturn(weekComment);
        final Response response = weekCommentEndPoint.patch(randomId, randomId, weekComment);
        checkResponseForbidden(response);
        verify(mockWeekCommentService).find(randomId);

    }


}
