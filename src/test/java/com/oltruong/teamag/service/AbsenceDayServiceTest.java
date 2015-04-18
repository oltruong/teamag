package com.oltruong.teamag.service;

import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbsenceDayServiceTest extends AbstractServiceTest {

    @Mock
    WorkService mockWorkService;

    private AbsenceDayService absenceDayService;
    private List<AbsenceDay> absenceDayList;


    @Before
    public void prepare() {
        absenceDayService = new AbsenceDayService();
        absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);
        when(mockQuery.getResultList()).thenReturn(absenceDayList);
        TestUtils.setPrivateAttribute(absenceDayService, mockWorkService, "workService");

        prepareService(absenceDayService);
    }

    @Test
    public void testFindAll() throws Exception {
        List<AbsenceDay> absenceDayListReturned = absenceDayService.findAll();

        Assertions.assertThat(absenceDayList).isEqualTo(absenceDayListReturned);

        checkCreateNameQuery("findAllAbsenceDays");

        verify(getMockQuery()).getResultList();

    }

    @Test
    public void testRemoveAbsence() {

        Long idTest = Long.valueOf(3365l);


        List<AbsenceDay> absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);
        when(getMockQuery().getResultList()).thenReturn(absenceDayList);


        absenceDayService.remove(idTest);

        checkCreateNameQuery("findAbsenceDayByAbsenceId");

        verify(getMockQuery()).setParameter(eq("fAbsenceId"), eq(idTest));
        verify(getMockQuery()).getResultList();

        absenceDayList.forEach(absenceDay -> verify(mockWorkService).removeWorkAbsence(eq(absenceDay)));
        absenceDayList.forEach(absenceDay -> verify(mockEntityManager).remove(eq(absenceDay)));


    }

    @Test
    public void testRemoveAbsence_listNull() {
        when(getMockQuery().getResultList()).thenReturn(null);

        absenceDayService.remove(Long.valueOf(123l));
        verify(mockEntityManager, never()).remove(any());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAbsence_null() {
        absenceDayService.remove(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsenceDayList_noMemberId() throws Exception {
        absenceDayService.findAbsenceDayList(null, Integer.valueOf(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsenceDayList_noMonthId() throws Exception {
        absenceDayService.findAbsenceDayList(EntityFactory.createRandomLong(), null);
    }


    @Test
    public void testFindAbsenceDayList() throws Exception {

        Long randomLong = EntityFactory.createRandomLong();
        Integer randomInteger = EntityFactory.createRandomInteger();

        List<AbsenceDay> absenceDaysReturned = absenceDayService.findAbsenceDayList(randomLong, randomInteger);

        assertThat(absenceDaysReturned).isEqualTo(absenceDayList);

        verify(mockEntityManager).createNamedQuery(eq("findAbsenceDayByMemberAndMonth"));
        verify(mockQuery).setParameter(eq("fMemberId"), eq(randomLong));
        verify(mockQuery).setParameter(eq("fMonth"), eq(randomInteger));
    }

    @Test
    public void testDeleteAll_null() {
        when(mockQuery.getResultList()).thenReturn(null);

        absenceDayService.deleteAll();

        verify(mockEntityManager, never()).remove(any());
        verify(mockWorkService, never()).removeWorkAbsence(any());
    }

    @Test
    public void testDeleteAll() {
        absenceDayService.deleteAll();
        absenceDayList.forEach(absenceDay -> {
            verify(mockEntityManager).remove(eq(absenceDay));
            verify(mockWorkService).removeWorkAbsence(eq(absenceDay));
        });
    }

}