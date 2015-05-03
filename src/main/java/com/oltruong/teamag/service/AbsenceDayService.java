package com.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import com.oltruong.teamag.model.AbsenceDay;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class AbsenceDayService extends AbstractService<AbsenceDay> {

    @Inject
    private WorkService workService;


    @Override
    public List<AbsenceDay> findAll() {
        return getTypedQueryList("findAllAbsenceDays");
    }


    @Override
    Class<AbsenceDay> entityProvider() {
        return AbsenceDay.class;
    }

    @Override
    public void remove(Long absenceId) {
        Preconditions.checkArgument(absenceId != null);
        TypedQuery<AbsenceDay> query = createTypedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", absenceId);
        List<AbsenceDay> absenceDayList = query.getResultList();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> {
                remove(absenceDay);
                workService.removeWorkAbsence(absenceDay);
            });
        }

    }


    public List<AbsenceDay> findByMemberAndMonth(Long memberId, Integer month) {

        Preconditions.checkArgument(memberId != null);
        Preconditions.checkArgument(month != null);

        TypedQuery<AbsenceDay> query = createTypedQuery("findAbsenceDayByMemberAndMonth", AbsenceDay.class);

        query.setParameter("fMemberId", memberId);
        query.setParameter("fMonth", month);

        return query.getResultList();
    }

    public void removeAll() {
        List<AbsenceDay> absenceDayList = findAll();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> {
                remove(absenceDay);
                workService.removeWorkAbsence(absenceDay);
            });
        }
    }
}
