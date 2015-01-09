package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.WeekComment;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeekCommentServiceTest extends AbstractServiceTest {

    private WeekCommentService weekCommentService;

    private WeekComment weekComment;
    private int year = DateTime.now().getYear();
    private int week = DateTime.now().getWeekOfWeekyear();


    @Before
    public void init() {
        super.setup();
        weekCommentService = new WeekCommentService();
        prepareService(weekCommentService);
        weekComment = EntityFactory.createWeekComment();

    }


    @Test
    public void testFindWeekComment() throws Exception {

        List<WeekComment> weekCommentList = Lists.newArrayListWithExpectedSize(1);
        weekCommentList.add(weekComment);
        when(mockQuery.getResultList()).thenReturn(weekCommentList);


        WeekComment weekCommentReturned = weekCommentService.findWeekComment(randomLong, week, year);

        assertThat(weekCommentReturned).isEqualTo(weekComment);
        checkCallFind();
    }

    @Test
    public void testFindWeekComment_empty() throws Exception {
        assertThat(weekCommentService.findWeekComment(randomLong, week, year)).isNull();
        checkCallFind();
    }

    private void checkCallFind() {
        checkCreateNameQuery("findWeekComment");
        checkParameter("fmemberId", randomLong);
        checkParameter("fweekYear", week);
        checkParameter("fyear", year);
    }

    @Test
    public void testCreate() throws Exception {
        WeekComment weekCommentCreated = weekCommentService.create(weekComment);

        assertThat(weekCommentCreated).isEqualTo(weekComment);
        verify(mockEntityManager).persist(eq(weekComment));
    }

    @Test
    public void testUpdate() throws Exception {
        weekCommentService.update(weekComment);
        verify(mockEntityManager).merge(eq(weekComment));
    }

    @Test
    public void testRemoveWeekComment() throws Exception {

        WeekComment newWeekComment = EntityFactory.createWeekComment();
        TestUtils.setPrivateAttribute(newWeekComment, randomLong, "id");
        when(mockEntityManager.find(eq(WeekComment.class), any())).thenReturn(weekComment);

        weekCommentService.removeWeekComment(newWeekComment);
        verify(mockEntityManager).find(eq(WeekComment.class), eq(randomLong));
        verify(mockEntityManager).remove(eq(weekComment));

    }
}