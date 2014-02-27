package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
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

    @Before
    public void prepare() {
        absenceEJB = new AbsenceEJB();
        prepareEJB(absenceEJB);
    }


    @Test
    public void testFindAllAbsences() {
        List<Absence> absenceList = Lists.newArrayListWithExpectedSize(1);
        absenceList.add(EntityFactory.createAbsence());

        when(getMockQuery().getResultList()).thenReturn(absenceList);

        List<Absence> allAbsenceList = absenceEJB.findAllAbsences();

        assertThat(allAbsenceList).isEqualTo(absenceList);
        verify(getMockEntityManager()).createNamedQuery(eq("findAllAbsences"));
    }


    @Test
    public void testFindAbsencesByMember() throws Exception {


        List<Absence> absenceList = Lists.newArrayListWithExpectedSize(1);
        absenceList.add(EntityFactory.createAbsence());

        when(getMockQuery().getResultList()).thenReturn(absenceList);

        Member member = EntityFactory.createMember();
        member.setId(Long.valueOf(327l));

        List<Absence> absenceMemberList = absenceEJB.findAbsencesByMember(member);

        assertThat(absenceMemberList).isNotNull().isNotEmpty().containsOnly(absenceList.get(0));
        verify(getMockEntityManager()).createNamedQuery(eq("findAbsencesByMember"));
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
}
