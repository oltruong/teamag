package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbsenceDayServiceTest extends AbstractServiceTest {


    private AbsenceDayService absenceDayService;
    private List<AbsenceDay> absenceDayList;


    @Before
    public void prepare() {
        absenceDayService = new AbsenceDayService();
        absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);
        when(mockQuery.getResultList()).thenReturn(absenceDayList);

        prepareService(absenceDayService);
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
}