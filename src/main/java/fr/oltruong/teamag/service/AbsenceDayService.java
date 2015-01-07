package fr.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.model.AbsenceDay;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class AbsenceDayService extends AbstractService {

    @Inject
    private WorkService workService;


    @SuppressWarnings("unchecked")
    public List<AbsenceDay> findAll() {
        return getNamedQueryList("findAllAbsenceDays");

    }

    @SuppressWarnings("unchecked")
    public void remove(Long absenceId) {

        Preconditions.checkArgument(absenceId != null);
        Query query = createNamedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", absenceId);

        List<AbsenceDay> absenceDayList = query.getResultList();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> {
                remove(absenceDay);
                workService.removeWorkAbsence(absenceDay);
            });
        }

    }


    public List<AbsenceDay> findAbsenceDayList(Long memberId, Integer month) {

        Preconditions.checkArgument(memberId != null);
        Preconditions.checkArgument(month != null);

        Query query = createNamedQuery("findAbsenceDayByMemberAndMonth");

        query.setParameter("fMemberId", memberId);
        query.setParameter("fMonth", month);

        return query.getResultList();
    }

    public void deleteAll() {
        List<AbsenceDay> absenceDayList = findAll();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> {
                remove(absenceDay);
                workService.removeWorkAbsence(absenceDay);
            });
        }

    }
}
