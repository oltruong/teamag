package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
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
        when(getMockQuery().getResultList()).thenReturn(absenceList);


        prepareService(absenceService);
        TestUtils.setPrivateAttribute(absenceService, mockEmailService, "emailService");
        TestUtils.setPrivateAttribute(absenceService, mockWorkService, "workService");
        TestUtils.setPrivateAttribute(absenceService, mockAbsenceDayService, "absenceDayService");

    }


    @Test
    public void testFindAllAbsences() {

        List<Absence> allAbsenceList = absenceService.findAllAbsences();
        assertThat(allAbsenceList).isEqualTo(absenceList);
        checkCreateNameQuery("findAllAbsences");

    }


    @Test
    public void testFindAbsencesByMember() throws Exception {

        Member member = EntityFactory.createMember();
        member.setId(Long.valueOf(327l));

        List<Absence> absenceMemberList = absenceService.findAbsencesByMember(member);

        assertThat(absenceMemberList).isNotNull().isNotEmpty().isEqualTo(absenceList);
        checkCreateNameQuery("findAbsencesByMember");

        verify(getMockQuery()).setParameter(eq("fmemberId"), eq(member.getId()));


    }

    @Test
    public void testDeleteAbsence() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        when(mockEntityManager.find(eq(Absence.class), anyLong())).thenReturn(absence);

        Long absenceId = randomLong;
        absence.setId(absenceId);
        absenceService.deleteAbsence(absence);

        verify(mockEntityManager).find(eq(Absence.class), eq(absenceId));
        verify(mockEntityManager).remove(eq(absence));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberNull() throws Exception {
        absenceService.findAbsencesByMember(null);
    }

    @Test
    public void testAddAbsence() throws Exception {

        MemberService memberService = new MemberService();

        Map<Long, Member> memberMap = Maps.newHashMapWithExpectedSize(1);
        memberMap.put(randomLong, EntityFactory.createMember());

        TestUtils.setPrivateAttribute(memberService, memberMap, "memberMap");

        Absence absence = EntityFactory.createAbsence();
        when(getMockQuery().getResultList()).thenReturn(Lists.newArrayList());
        absenceService.addAbsence(absence, randomLong);
        verify(mockEntityManager).persist(eq(absence));
    }

    @Test
    public void testFindAbsencesByMemberId() {
        List<Absence> absences = absenceService.findAbsencesByMemberId(idTest);
        assertThat(absences).isEqualTo(absenceList);
        verify(mockEntityManager).createNamedQuery(eq("findAbsencesByMember"));
        verify(mockQuery).setParameter(eq("fmemberId"), eq(idTest));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberId_null() {
        absenceService.findAbsencesByMemberId(null);
    }

    @Test
    public void testRegisterAbsence() {
        Absence absence = EntityFactory.createAbsence();
        absenceService.registerAbsence(absence);

        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        absenceDayList.forEach(absenceDay -> {
            verify(mockEntityManager).persist(refEq(absenceDay));
            verify(mockWorkService).updateWorkAbsence(refEq(absenceDay));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterAbsence_null() {
        absenceService.registerAbsence(null);
    }

    @Test
    public void testReloadAllAbsenceDay() {

        Query mockQueryAbsenceDay = mock(Query.class);
        Query mockQueryAbsence = mock(Query.class);

        when(mockEntityManager.createNamedQuery(eq("findAllAbsenceDays"))).thenReturn(mockQueryAbsenceDay);
        when(mockEntityManager.createNamedQuery(eq("findAllAbsences"))).thenReturn(mockQueryAbsence);

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(mockQueryAbsence.getResultList()).thenReturn(absenceList);

        List<AbsenceDay> absenceDayList = EntityFactory.createList(EntityFactory::createAbsenceDay);
        when(mockQueryAbsenceDay.getResultList()).thenReturn(absenceDayList);

        absenceService.reloadAllAbsenceDay();

        verify(mockEntityManager).createNamedQuery(eq("findAllAbsences"));
        verify(mockAbsenceDayService).deleteAll();
        absenceList.forEach(absence -> AbsenceDayTransformer.transformAbsence(absence).forEach(absenceDay -> verify(mockEntityManager).persist(refEq(absenceDay))));

    }

    @Test
    public void testReloadAllAbsenceDay_Null() {
        Query mockQueryAbsenceDay = mock(Query.class);
        Query mockQueryAbsence = mock(Query.class);

        when(mockEntityManager.createNamedQuery(eq("findAllAbsenceDays"))).thenReturn(mockQueryAbsenceDay);
        when(mockEntityManager.createNamedQuery(eq("findAllAbsences"))).thenReturn(mockQueryAbsence);

        when(mockQueryAbsenceDay.getResultList()).thenReturn(null);
        when(mockQueryAbsence.getResultList()).thenReturn(null);

        absenceService.reloadAllAbsenceDay();
        verify(mockEntityManager, never()).remove(any());
        verify(mockEntityManager, never()).persist(any());
    }
}
