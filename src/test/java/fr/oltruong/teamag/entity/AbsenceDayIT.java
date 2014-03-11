package fr.oltruong.teamag.entity;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.RollbackException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class AbsenceDayIT extends AbstractEntityIT {


    @Ignore("FIXME")
    public void testCreate() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();

        assertThat(absenceDay.getId()).isNull();
        createWithoutCommit(absenceDay.getMember());
        createWithoutCommit(absenceDay.getAbsence());
        absenceDay = (AbsenceDay) createWithCommit(absenceDay);
        assertThat(absenceDay.getId()).isNotNull();
    }

    @Test(expected = RollbackException.class)
    public void testCreate_memberNull() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();
        absenceDay.setMember(null);
        createWithCommit(absenceDay);
    }
}
