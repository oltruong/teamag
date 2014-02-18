package fr.oltruong.teamag.entity;

import org.junit.Test;

import javax.persistence.Query;
import javax.persistence.RollbackException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class AbsenceIT extends AbstractEntityIT {

    @Test
    public void testCreation() {
        Absence absence = EntityFactory.createAbsence();

        assertThat(absence.getId()).isNull();
        getEntityManager().persist(absence.getMember());
        getEntityManager().persist(absence);
        getTransaction().commit();
        assertThat(absence.getId()).isNotNull();

        Absence absenceDB = getEntityManager().find(Absence.class, absence.getId());

        assertThat(absence.getBeginDate()).isEqualTo(absenceDB.getBeginDate());
        assertThat(absence.getEndDate()).isEqualTo(absenceDB.getEndDate());
        assertThat(absence.getBeginType()).isEqualTo(absenceDB.getBeginType());
        assertThat(absence.getEndType()).isEqualTo(absenceDB.getEndType());
        assertThat(absence.getMember()).isEqualTo(absenceDB.getMember());

    }

    @Test(expected = RollbackException.class)
    public void testCreationMissingEndDate() {
        Absence absence = EntityFactory.createAbsence();
        absence.setEndDate(null);
        getEntityManager().persist(absence.getMember());
        getEntityManager().persist(absence);
        getTransaction().commit();
    }

    @Test
    public void testFindMemberById() {
        Absence absence = EntityFactory.createAbsence();

        Absence absence2 = EntityFactory.createAbsence();
        Member member2 = EntityFactory.createMember();
        member2.setName("BUDDY");
        absence2.setMember(member2);

        getEntityManager().persist(absence.getMember());
        getEntityManager().persist(absence);

        getEntityManager().persist(absence2.getMember());
        getEntityManager().persist(absence2);

        getTransaction().commit();

        Query query = getEntityManager().createNamedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", member2.getId());
        List<Absence> absenceList = query.getResultList();

        assertThat(absenceList).isNotNull().hasSize(1).contains(absence2);

    }
}
