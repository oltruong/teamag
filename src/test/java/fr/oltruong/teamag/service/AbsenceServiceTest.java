package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.Member;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class AbsenceServiceTest extends AbstractServiceTest {


    private AbsenceService absenceService;

    private List<Absence> absenceList;

    @Before
    public void prepare() {
        absenceService = new AbsenceService();
        absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(getMockQuery().getResultList()).thenReturn(absenceList);

        prepareService(absenceService);
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

        Long absenceId = Long.valueOf(325l);

        absenceService.deleteAbsence(absenceId);

        verify(mockEntityManager).find(eq(Absence.class), eq(absenceId));
        verify(mockEntityManager).remove(eq(absence));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberNull() throws Exception {
        absenceService.findAbsencesByMember(null);
    }

    @Test
    public void testAddAbsence() throws Exception {
        Absence absence = EntityFactory.createAbsence();
        absenceService.addAbsence(absence);

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
}
