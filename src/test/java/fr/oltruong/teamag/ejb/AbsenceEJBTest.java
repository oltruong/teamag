package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.entity.Member;
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
public class AbsenceEJBTest extends AbstractEJBTest {


    private AbsenceEJB absenceEJB;

    private List<Absence> absenceList;

    @Before
    public void prepare() {
        absenceEJB = new AbsenceEJB();
        absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(getMockQuery().getResultList()).thenReturn(absenceList);

        prepareEJB(absenceEJB);
    }


    @Test
    public void testFindAllAbsences() {

        List<Absence> allAbsenceList = absenceEJB.findAllAbsences();
        assertThat(allAbsenceList).isEqualTo(absenceList);
        checkCreateNameQuery("findAllAbsences");

    }


    @Test
    public void testFindAbsencesByMember() throws Exception {

        Member member = EntityFactory.createMember();
        member.setId(Long.valueOf(327l));

        List<Absence> absenceMemberList = absenceEJB.findAbsencesByMember(member);

        assertThat(absenceMemberList).isNotNull().isNotEmpty().isEqualTo(absenceList);
        checkCreateNameQuery("findAbsencesByMember");

        verify(getMockQuery()).setParameter(eq("fmemberId"), eq(member.getId()));


    }

    @Test
    public void testDeleteAbsence() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        when(getMockEntityManager().find(eq(Absence.class), anyLong())).thenReturn(absence);

        Long absenceId = Long.valueOf(325l);

        absenceEJB.deleteAbsence(absenceId);

        verify(getMockEntityManager()).find(eq(Absence.class), eq(absenceId));
        verify(getMockEntityManager()).remove(eq(absence));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberNull() throws Exception {
        absenceEJB.findAbsencesByMember(null);
    }

    @Test
    public void testAddAbsence() throws Exception {
        Absence absence = EntityFactory.createAbsence();
        absenceEJB.addAbsence(absence);

        verify(getMockEntityManager()).persist(eq(absence));


    }

    @Test
    public void testFindAbsencesByMemberId() {


        List<Absence> absences = absenceEJB.findAbsencesByMemberId(idTest);

        assertThat(absences).isEqualTo(absenceList);
        verify(mockEntityManager).createNamedQuery(eq("findAbsencesByMember"));
        verify(mockQuery).setParameter(eq("fmemberId"), eq(idTest));

    }


    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberId_null() {
        absenceEJB.findAbsencesByMemberId(null);
    }
}
