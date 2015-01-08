package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.model.builder.EntityFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class WorkServiceTest extends AbstractServiceTest {


    private WorkService workService;
    private List<Work> workList;
    DateTime month;

    @Before
    public void init() {
        super.setup();
        workService = new WorkService();
        prepareService(workService);
        workList = EntityFactory.createList(EntityFactory::createWork);
        month = DateTime.now().withDayOfMonth(1);
        when(mockQuery.getResultList()).thenReturn(workList);
    }


    @Test
    public void testCreateWork() {

        Member member = EntityFactory.createMember();
        Task task = EntityFactory.createTask();
        DateTime day = DateTime.now();

        Work workCreated = workService.createWork(member, month, task, day);

        verify(mockEntityManager).persist(eq(workCreated));
        assertThat(workCreated.getDay()).isEqualTo(day);
        assertThat(workCreated.getMember()).isEqualToComparingFieldByField(member);
        assertThat(workCreated.getTask()).isEqualToComparingFieldByField(task);
        assertThat(workCreated.getMonth()).isEqualTo(month);
        assertThat(workCreated.getTotal()).isEqualTo(Double.valueOf(0d));
    }

    @Test
    public void testUpdateWorks() {

        final Double totalEdit = Double.valueOf(2.2d);
        workList.forEach(work -> work.setTotalEdit(totalEdit));

        workService.updateWorks(workList);

        workList.forEach(work -> {
            assertThat(work.getTotal()).isEqualTo(totalEdit);
            verify(mockEntityManager).merge(eq(work));

        });
    }

    @Test
    public void testGetWorksMonth() {

        List<Work> workListReturned = workService.getWorksMonth(month);

        assertThat(workListReturned).isEqualTo(workList);
        checkCreateNameQuery("findWorksMonth");
        verify(mockQuery).setParameter(eq("fmonth"), eq(month));
    }


    @Test
    public void testFindWorkDays() {
        DateTime month = DateTime.now();
        Member member = EntityFactory.createMember();
        member.setId(randomLong);

        DateTime beginMonth = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();
        Double sum = EntityFactory.createRandomDouble();

        Object[] result = {beginMonth, sum};

        List<Object[]> resultList = Lists.newArrayListWithExpectedSize(1);
        resultList.add(result);

        when(mockQuery.getResultList()).thenReturn(resultList);

        Map<DateTime, Double> workDaysMap = workService.findWorkDays(member, month);
        checkCreateNameQuery("findWorkDaysByMemberMonth");
        verify(mockQuery).setParameter(eq("fmemberId"), eq(randomLong));
        verify(mockQuery).setParameter(eq("fmonth"), eq(month));

        assertThat(workDaysMap).hasSize(1);
        workDaysMap.forEach((key, value) -> {
            assertThat(key).isEqualTo(beginMonth);
            assertThat(value).isEqualTo(sum);
        });
    }

    @Test
    public void testFindWorkByTask() {
        List<Work> workListFound = workService.findWorkByTask(randomLong);

        assertThat(workListFound).isEqualTo(workList);
        checkCreateNameQuery("findWorksByTask");
        verify(mockQuery).setParameter(eq("fTaskId"), eq(randomLong));
    }


    @Test
    public void testGetSumWorks() {

        Integer randomInt = EntityFactory.createRandomInteger();

        when(mockQuery.getSingleResult()).thenReturn(randomInt);

        DateTime month = DateTime.now();
        Member member = EntityFactory.createMember();
        member.setId(randomLong);

        int sum = workService.getSumWorks(member, month);

        assertThat(sum).isEqualTo(randomInt.intValue());
        checkCreateNameQuery("countWorksMemberMonth");
        verify(mockQuery).setParameter(eq("fmemberId"), eq(randomLong));
        verify(mockQuery).setParameter(eq("fmonth"), eq(month));
    }

}
