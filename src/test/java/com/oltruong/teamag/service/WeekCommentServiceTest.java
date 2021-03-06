package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.model.builder.EntityFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeekCommentServiceTest extends AbstractServiceTest {

    private WeekCommentService weekCommentService;

    private WeekComment weekComment;
    private int year = DateTime.now().getYear();
    private int month = DateTime.now().getMonthOfYear();
    private int week = DateTime.now().getWeekOfWeekyear();


    @Before
    public void init() {
        super.setup();
        weekCommentService = new WeekCommentService();
        prepareService(weekCommentService);
        weekComment = EntityFactory.createWeekComment();

    }


    @Test
    public void findWeekComment() throws Exception {

        List<WeekComment> weekCommentList = Lists.newArrayListWithExpectedSize(1);
        weekCommentList.add(weekComment);
        when(mockTypedQuery.getResultList()).thenReturn(weekCommentList);


        WeekComment weekCommentReturned = weekCommentService.findWeekComment(randomLong, week, month, year);

        assertThat(weekCommentReturned).isEqualTo(weekComment);
        checkCallFind();
    }

    @Test
    public void findWeekComment_empty() throws Exception {
        assertThat(weekCommentService.findWeekComment(randomLong, week, month, year)).isNull();
        checkCallFind();
    }

    private void checkCallFind() {
        verify(mockEntityManager).createNamedQuery(eq("WeekComment.FIND_BY_MEMBER_WEEK_MONTH_YEAR"), eq(WeekComment.class));

        checkParameter("fmemberId", randomLong);
        checkParameter("fweekYear", week);
        checkParameter("fyear", year);
    }

    @Test
    public void create() throws Exception {
        WeekComment weekCommentCreated = weekCommentService.persist(weekComment);

        assertThat(weekCommentCreated).isEqualTo(weekComment);
        verify(mockEntityManager).persist(eq(weekComment));
    }

    @Test
    public void testUpdate() throws Exception {
        weekCommentService.merge(weekComment);
        verify(mockEntityManager).merge(eq(weekComment));
    }

    @Test
    public void testRemove() throws Exception {

        WeekComment newWeekComment = EntityFactory.createWeekComment();

        weekCommentService.remove(newWeekComment);
        verify(mockEntityManager).remove(eq(newWeekComment));

    }
}