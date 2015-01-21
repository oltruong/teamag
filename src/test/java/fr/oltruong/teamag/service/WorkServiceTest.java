package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.AbsenceDay;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class WorkServiceTest extends AbstractServiceTest {


    private WorkService workService;
    private List<Work> workList;
    DateTime month;

    private Member member;

    @Before
    public void init() {
        super.setup();
        workService = new WorkService();
        member = EntityFactory.createMember();
        member.setId(randomLong);
        prepareService(workService);
        workList = EntityFactory.createList(EntityFactory::createWork, 10);
        workList.forEach(w -> w.setTotal(0d));
        month = DateTime.now().withDayOfMonth(1);
        when(mockQuery.getResultList()).thenReturn(workList);
        when(mockTypedQuery.getResultList()).thenReturn(workList);
    }


    @Test
    public void testFindWorksNotNullByMonth() {
        Map<Task, List<Work>> worksByTask = workService.findWorksNotNullByMonth(member, month);


        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_BY_MEMBER_MONTH_NOT_NULL"), eq(Work.class));
        verify(mockTypedQuery).setParameter(eq("fmemberId"), eq(randomLong));
        verify(mockTypedQuery).setParameter(eq("fmonth"), eq(month));
        assertThat(worksByTask).isNotEmpty().hasSameSizeAs(workList);

        worksByTask.forEach((task, list) -> {
            list.forEach(work -> {
                assertThat(workList.contains(work));
                assertThat(work.getTask()).isEqualToComparingFieldByField(task);
            });
        });
    }

    @Test
    public void testFindWorksNotNullByWeek() {
        int currentWeek = DateTime.now().getWeekOfWeekyear();

        List<Work> workListFound = workService.findWorksNotNullByWeek(randomLong, currentWeek);
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_BY_MEMBER_MONTH"), eq(Work.class));
        verify(mockTypedQuery).setParameter(eq("fmemberId"), eq(randomLong));
        verify(mockTypedQuery).setParameter(eq("fmonth"), eq(DateTime.now().withWeekOfWeekyear(currentWeek).withDayOfMonth(1)));
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
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_BY_MONTH"), eq(Work.class));
        verify(mockTypedQuery).setParameter(eq("fmonth"), eq(month));
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

        when(mockTypedQuery.getResultList()).thenReturn(resultList);

        Map<DateTime, Double> workDaysMap = workService.findWorkDays(member, month);
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_WORKDAYS_BY_MEMBER_MONTH"), eq(Object[].class));
        verify(mockTypedQuery).setParameter(eq("fmemberId"), eq(randomLong));
        verify(mockTypedQuery).setParameter(eq("fmonth"), eq(month));

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
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_BY_TASK_MEMBER"), eq(Work.class));
        verify(mockTypedQuery).setParameter(eq("fTaskId"), eq(randomLong));
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
        checkCreateNameQuery("Work.SUM_BY_MONTH_MEMBER");
        verify(mockQuery).setParameter(eq("fmemberId"), eq(randomLong));
        verify(mockQuery).setParameter(eq("fmonth"), eq(month));
    }

    @Test
    public void testUpdateWorkAbsence() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();
        absenceDay.setValue(1f);
        Work absenceWork = workList.get(0);
        absenceWork.setTotal(0d);

        workService.updateWorkAbsence(absenceDay);
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_ABSENCE_BY_MEMBER"), eq(Work.class));
        verify(mockEntityManager).merge(eq(absenceWork));
        assertThat(absenceWork.getTotal()).isEqualTo(1d);
    }

    @Test
    public void testRemoveWorkAbsence() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();
        Work absenceWork = workList.get(0);
        absenceWork.setTotal(0.5d);

        workService.removeWorkAbsence(absenceDay);
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_ABSENCE_BY_MEMBER"), eq(Work.class));
        verify(mockEntityManager).merge(eq(absenceWork));
        assertThat(absenceWork.getTotal()).isEqualTo(0d);
    }

    @Test
    public void testRemoveWorkAbsence_past() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();
        absenceDay.setDay(DateTime.now().minusMonths(1));

        workService.removeWorkAbsence(absenceDay);
        verify(mockEntityManager, never()).createNamedQuery(any(), any());
        verify(mockEntityManager, never()).merge(any());
    }

    @Test
    public void testRemoveWorkAbsence_null() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();

        workList.clear();
        workService.removeWorkAbsence(absenceDay);
        verify(mockEntityManager).createNamedQuery(eq("Work.FIND_ABSENCE_BY_MEMBER"), eq(Work.class));
        verify(mockEntityManager, never()).merge(any());
    }


}
