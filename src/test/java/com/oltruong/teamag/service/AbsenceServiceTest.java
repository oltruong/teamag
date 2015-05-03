package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.transformer.AbsenceDayTransformer;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class AbsenceServiceTest extends AbstractServiceTest {

    @Mock
    private WorkService mockWorkService;

    @Mock
    private EmailService mockEmailService;

    @Mock
    private AbsenceDayService mockAbsenceDayService;

    private AbsenceService absenceService;
    private List<Absence> absenceList;

    @Before
    public void prepare() {
        absenceService = new AbsenceService();
        absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(mockTypedQuery.getResultList()).thenReturn(absenceList);


        prepareService(absenceService);
        TestUtils.setPrivateAttribute(absenceService, mockEmailService, "emailService");
        TestUtils.setPrivateAttribute(absenceService, mockWorkService, "workService");
        TestUtils.setPrivateAttribute(absenceService, mockAbsenceDayService, "absenceDayService");

    }


    @Test
    public void testFindAll() {
        List<Absence> allAbsenceList = absenceService.findAll();
        assertThat(allAbsenceList).isEqualTo(absenceList);
        checkCreateTypedQuery("findAll");
    }

    @Test
    public void testFind() {
        Absence absence = EntityFactory.createAbsence();

        when(mockEntityManager.find(any(), any())).thenReturn(absence);
        Absence absenceReturned = absenceService.find(randomLong);

        assertThat(absenceReturned).isEqualTo(absence);
        verify(mockEntityManager).find(eq(Absence.class), eq(randomLong));
    }


    @Test
    public void testDeleteAbsence() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        when(mockEntityManager.find(eq(Absence.class), anyLong())).thenReturn(absence);

        Long absenceId = randomLong;
        absence.setId(absenceId);
        absenceService.remove(absence);
        verify(mockEntityManager).remove(eq(absence));
    }


    @Test
    public void testAddAbsence_noFormat() throws Exception {
        Absence absence = EntityFactory.createAbsence();
        testAddAbsence(absence);
    }

    @Test
    public void testAddAbsence_formatBegin() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        absence.setBeginDate(null);
        absence.setBeginType(null);
        testAddAbsence(absence);
        checkDate(absence);
    }

    private void checkDate(Absence absence) {
        assertThat(absence.getBeginDate()).isNotNull().isEqualTo(absence.getEndDate());
        assertThat(absence.getBeginType()).isNotNull().isEqualTo(absence.getEndType());
    }

    @Test
    public void testAddAbsence_formatEnd() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        absence.setBeginType(Absence.ALL_DAY);
        absence.setEndDate(null);
        absence.setEndType(null);
        testAddAbsence(absence);
        checkDate(absence);
    }


    private void testAddAbsence(Absence absence) throws Exception {

        MemberService memberService = new MemberService();

        Map<Long, Member> memberMap = Maps.newHashMapWithExpectedSize(1);
        memberMap.put(randomLong, EntityFactory.createMember());

        TestUtils.setPrivateAttribute(memberService, memberMap, "memberMap");

        when(mockTypedQuery.getResultList()).thenReturn(Lists.newArrayList());
        absenceService.addAbsence(absence, randomLong);
        verify(mockEntityManager).persist(eq(absence));
    }


    @Test
    public void testFindAbsencesByMemberId() {
        List<Absence> absences = absenceService.findAbsencesByMember(idTest);
        assertThat(absences).isEqualTo(absenceList);
        checkCreateTypedQuery("findAbsencesByMember");
        verify(mockTypedQuery).setParameter(eq("fmemberId"), eq(idTest));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberId_null() {
        absenceService.findAbsencesByMember(null);
    }

    @Test
    public void testRegisterAbsence() {
        Absence absence = EntityFactory.createAbsence();
        absenceService.registerAbsence(absence);

        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        absenceDayList.forEach(absenceDay -> {
            verify(mockAbsenceDayService).persist(refEq(absenceDay));
            verify(mockWorkService).updateWorkAbsence(refEq(absenceDay));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterAbsence_null() {
        absenceService.registerAbsence(null);
    }

    @Test
    public void testReloadAllAbsenceDay() {

        absenceService.reloadAllAbsenceDay();

        checkCreateTypedQuery("findAll");
        verify(mockAbsenceDayService).removeAll();
        absenceList.forEach(absence -> AbsenceDayTransformer.transformAbsence(absence).forEach(absenceDay -> {
            verify(mockAbsenceDayService).persist(Matchers.refEq(absenceDay));
            verify(mockWorkService).updateWorkAbsence(Matchers.refEq(absenceDay));
        }));

    }

    @Test
    public void testReloadAllAbsenceDay_Null() {
        Query mockTypedQueryAbsenceDay = mock(Query.class);
        Query mockTypedQueryAbsence = mock(Query.class);

        when(mockEntityManager.createNamedQuery(eq("findAllAbsenceDays"))).thenReturn(mockTypedQueryAbsenceDay);
        when(mockEntityManager.createNamedQuery(eq("findAll"))).thenReturn(mockTypedQueryAbsence);

        when(mockTypedQueryAbsenceDay.getResultList()).thenReturn(null);
        when(mockTypedQueryAbsence.getResultList()).thenReturn(null);

        absenceService.reloadAllAbsenceDay();
        verify(mockEntityManager, never()).remove(any());
        verify(mockEntityManager, never()).persist(any());
    }
}
