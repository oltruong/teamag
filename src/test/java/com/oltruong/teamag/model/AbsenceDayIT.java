package com.oltruong.teamag.model;


import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class AbsenceDayIT extends AbstractEntityIT {


    @Before
    public void prepareTest() {
        super.setup();
    }

    @Test
    public void createAndFind() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();

        assertThat(absenceDay.getId()).isNull();

        createWithoutCommit(absenceDay.getAbsence().getMember());
        absenceDay.setMember(absenceDay.getAbsence().getMember());
        createWithoutCommit(absenceDay.getAbsence());
        absenceDay = (AbsenceDay) createWithCommit(absenceDay);


        assertThat(absenceDay.getId()).isNotNull();
    }

    @Test(expected = PersistenceException.class)
    public void createMemberNull() {
        AbsenceDay absenceDay = EntityFactory.createAbsenceDay();
        absenceDay.setMember(null);
        createWithCommit(absenceDay);
    }


    @Test
    public void namedQueryFindAbsenceDayByMemberAndMonth() {
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



}
