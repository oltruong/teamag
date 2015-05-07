package com.oltruong.teamag.model;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
        entityManager.persist(absence.getMember());
        entityManager.persist(absence);
        transaction.commit();
        assertThat(absence.getId()).isNotNull();

        Absence absenceDB = entityManager.find(Absence.class, absence.getId());

        assertThat(absence).isEqualTo(absenceDB);

    }

    @Test(expected = PersistenceException.class)
    public void testCreationMissingEndDate() {
        Absence absence = EntityFactory.createAbsence();
        absence.setEndDate(null);
        entityManager.persist(absence.getMember());
        entityManager.persist(absence);
        transaction.commit();
    }

    @Test
    public void testFindMemberById() {
        Absence absence = EntityFactory.createAbsence();

        Absence absence2 = EntityFactory.createAbsence();
        Member member2 = EntityFactory.createMember();
        member2.setName("BUDDY");
        absence2.setMember(member2);

        entityManager.persist(absence.getMember());
        entityManager.persist(absence);

        entityManager.persist(absence2.getMember());
        entityManager.persist(absence2);

        transaction.commit();

        Query query = entityManager.createNamedQuery("Absence.FIND_BY_MEMBER");
        query.setParameter("fmemberId", member2.getId());
        List<Absence> absenceList = query.getResultList();

        Assertions.assertThat(absenceList).isNotNull().hasSize(1).contains(absence2);

    }
}
