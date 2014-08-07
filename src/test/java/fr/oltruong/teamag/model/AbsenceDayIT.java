package fr.oltruong.teamag.model;


import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class AbsenceDayIT extends AbstractEntityIT {


    @Before
    public void prepareTest() {
        super.setup();
    }

    @Test
    public void testCreateAndFind() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();

        assertThat(absenceDay.getId()).isNull();

        createWithoutCommit(absenceDay.getAbsence().getMember());
        absenceDay.setMember(absenceDay.getAbsence().getMember());
        createWithoutCommit(absenceDay.getAbsence());
        absenceDay = (AbsenceDay) createWithCommit(absenceDay);


        assertThat(absenceDay.getId()).isNotNull();
    }

    @Test(expected = PersistenceException.class)
    public void testCreate_memberNull() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();
        absenceDay.setMember(null);
        createWithCommit(absenceDay);
    }


    @Test
    public void testNamedQuery_findAbsenceDayByMemberAndMonth() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();

        assertThat(absenceDay.getId()).isNull();

        createWithoutCommit(absenceDay.getAbsence().getMember());
        absenceDay.setMember(absenceDay.getAbsence().getMember());
        createWithoutCommit(absenceDay.getAbsence());
        absenceDay = (AbsenceDay) createWithCommit(absenceDay);


        Query query = entityManager.createNamedQuery("findAbsenceDayByMemberAndMonth");

        query.setParameter("fMemberId", absenceDay.getMember().getId());
        query.setParameter("fMonth", absenceDay.getMonth());

        assertThat(query.getResultList()).hasSize(1).contains(absenceDay);

        query.setParameter("fMemberId", absenceDay.getMember().getId() + 1);
        assertThat(query.getResultList()).isEmpty();


    }


    @Ignore("TODO")
    public void testNamedQuery_getAbsenceValuePerWeek() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();

        assertThat(absenceDay.getId()).isNull();

        createWithoutCommit(absenceDay.getAbsence().getMember());
        absenceDay.setMember(absenceDay.getAbsence().getMember());
        createWithoutCommit(absenceDay.getAbsence());
        absenceDay = (AbsenceDay) createWithCommit(absenceDay);
        List<Object[]> objects = entityManager.createQuery("SELECT SUM(a.value), a.week, a.member from AbsenceDay a GROUP BY a.week, a.member ORDER BY a.week").getResultList();

        assertThat(objects).isNotNull().isNotEmpty();
    }
}
