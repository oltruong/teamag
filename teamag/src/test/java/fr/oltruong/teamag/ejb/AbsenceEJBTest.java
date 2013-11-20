package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class AbsenceEJBTest extends AbstractEJBTest {

    private AbsenceEJB buildAbsenceEJB() {
        AbsenceEJB absenceEJB = new AbsenceEJB();

        TestUtils.setPrivateAttribute(absenceEJB, AbstractEJB.class, getMockEntityManager(), "entityManager");
        TestUtils.setPrivateAttribute(absenceEJB, AbstractEJB.class, getMockLogger(), "logger");
        return absenceEJB;
    }

    @Test
    public void testFindAbsencesByMember() throws Exception {
        AbsenceEJB absenceEJB = buildAbsenceEJB();

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

    @Test(expected = IllegalArgumentException.class)
    public void testFindAbsencesByMemberNull() throws Exception {
        AbsenceEJB absenceEJB = buildAbsenceEJB();
        absenceEJB.findAbsencesByMember(null);
    }

    @Test
    public void testAddAbsence() throws Exception {
        AbsenceEJB absenceEJB = buildAbsenceEJB();
        Absence absence = EntityFactory.createAbsence();
        absenceEJB.addAbsence(absence);

        verify(getMockEntityManager()).persist(eq(absence));


    }
}
