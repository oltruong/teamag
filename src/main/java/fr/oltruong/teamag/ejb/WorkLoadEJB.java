package fr.oltruong.teamag.ejb;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.entity.*;
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
        return createNamedQuery("findAllAbsenceDays").getResultList();

    }

    public Map<Integer, Map<Member, Float>> buildWeekLoad() {
        String qlString = "SELECT SUM(a.value), a.week, a.month, a.member from AbsenceDay a GROUP BY a.week, a.member ORDER BY a.week, a.month";
        Query q = createQuery(qlString);
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
        Query query = createNamedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", id);


        List<AbsenceDay> absenceDayList = query.getResultList();
        for (AbsenceDay absenceDay : absenceDayList) {
            remove(absenceDay);
        }

    }

    public void registerAbsence(Absence newAbsence) {
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(newAbsence);
        for (AbsenceDay absenceDay : absenceDayList) {
            persist(absenceDay);
        }
    }

    public void reloadAllAbsenceDay() {
        List<AbsenceDay> absenceDayList = getAllAbsenceDay();
        for (AbsenceDay absenceDay : absenceDayList) {
            remove(absenceDay);
        }

        List<Absence> absenceList = findAllAbsences();
        for (Absence absence : absenceList) {
            registerAbsence(absence);
        }
    }

    private List<Absence> findAllAbsences() {
        return createNamedQuery("findAllAbsences").getResultList();
    }


    public Map<BusinessCase, List<WorkLoad>> buildWorkLoadMap() {

        List<WorkLoad> workLoadList = findAllWorkLoad();
        Map<BusinessCase, List<WorkLoad>> map = Maps.newHashMap();
        BusinessCase businessCase = null;
        for (WorkLoad workLoad : workLoadList) {
            if (!workLoad.getBusinessCase().equals(businessCase)) {
                businessCase = workLoad.getBusinessCase();
                List<WorkLoad> workLoads = Lists.newArrayList();
                workLoads.add(workLoad);
                map.put(businessCase, workLoads);
            } else {
                List<WorkLoad> workLoads = map.get(businessCase);
                workLoads.add(workLoad);
                map.put(businessCase, workLoads);
            }
        }
        return map;
    }

    public List<WorkLoad> findAllWorkLoad() {
        List<WorkLoad> workLoadList = createNamedQuery("findAllWorkLoad").getResultList();

        if (workLoadList == null || workLoadList.isEmpty()) {

            workLoadList = createWorkLoads();
        }

        return workLoadList;
    }

    private List<WorkLoad> createWorkLoads() {
        List<WorkLoad> workLoadList;
        getLogger().warn("Creation of workLoad");
        List<BusinessCase> businessCaseList = createNamedQuery("findAllBC").getResultList();

        List<Member> memberList = MemberEJB.getMemberList();


        workLoadList = Lists.newArrayListWithExpectedSize(businessCaseList.size() * memberList.size());

        for (BusinessCase businessCase : businessCaseList) {
            for (Member member : memberList) {
                WorkLoad workLoad = new WorkLoad(businessCase, member);
                persist(workLoad);
                workLoadList.add(workLoad);
            }
        }
        return workLoadList;
    }


    public void updateWorkLoad(List<WorkLoad> workLoadList) {
        for (WorkLoad workLoad : workLoadList) {
//            WorkLoad workLoadDb = .find(WorkLoad.class, workLoad.getId());
//            workLoadDb.setEstimated(workLoad.getEstimated());
            merge(workLoad);
        }
    }
}
