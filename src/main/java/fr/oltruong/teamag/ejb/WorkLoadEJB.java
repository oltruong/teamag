package fr.oltruong.teamag.ejb;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.AbsenceDay;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * @author Olivier Truong
 */
@Stateless
public class WorkLoadEJB extends AbstractEJB {


    public List<AbsenceDay> getAllAbsenceDay() {
        return getEntityManager().createNamedQuery("findAllAbsenceDays").getResultList();

    }

    public Map<Integer, Map<Member, Float>> buildWeekLoad() {
        String qlString = "SELECT SUM(a.value), a.week, a.month, a.member from AbsenceDay a GROUP BY a.week, a.member ORDER BY a.week, a.month";
        Query q = getEntityManager().createQuery(qlString);
        List<Object[]> results = q.getResultList();


        Map<Integer, Map<Member, Float>> map = Maps.newHashMap();

        int weekCursor = 0;

        for (Object[] object : results) {
            int week = ((Integer) object[1]).intValue();
            if (week == 1 && ((Integer) object[2]).intValue() == 12) {
                week = 53;
            }
            if (weekCursor != week) {
                weekCursor = week;
                Map<Member, Float> memberFloatMap = Maps.newHashMap();
                map.put(Integer.valueOf(week), memberFloatMap);
            }
            map.get(Integer.valueOf(week)).put(((Member) object[3]), ((Float) object[0]));
        }
        return map;
    }


    public void removeAbsence(Long id) {

        Preconditions.checkArgument(id != null);
        Query query = getEntityManager().createNamedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", id);


        List<AbsenceDay> absenceDayList = query.getResultList();
        for (AbsenceDay absenceDay : absenceDayList) {
            getEntityManager().remove(absenceDay);
        }

    }

    public void registerAbsence(Absence newAbsence) {
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(newAbsence);
        for (AbsenceDay absenceDay : absenceDayList) {
            getEntityManager().persist(absenceDay);
        }
    }

    public void reloadAllAbsenceDay() {
        List<AbsenceDay> absenceDayList = getAllAbsenceDay();
        for (AbsenceDay absenceDay : absenceDayList) {
            getEntityManager().remove(absenceDay);
        }

        List<Absence> absenceList = findAllAbsences();
        for (Absence absence : absenceList) {
            registerAbsence(absence);
        }
    }

    private List<Absence> findAllAbsences() {
        return getEntityManager().createNamedQuery("findAllAbsences").getResultList();
    }


}
