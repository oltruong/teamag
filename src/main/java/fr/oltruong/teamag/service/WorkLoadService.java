package fr.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class WorkLoadService extends AbstractService {


    public List<AbsenceDay> getAllAbsenceDay() {
        return getNamedQueryList("findAllAbsenceDays");

    }


    public void removeAbsence(Long id) {

        Preconditions.checkArgument(id != null);
        Query query = createNamedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", id);

        List<AbsenceDay> absenceDayList = query.getResultList();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> remove(absenceDay));
        }

    }

    public void registerAbsence(Absence newAbsence) {
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(newAbsence);
        absenceDayList.forEach(absenceDay -> persist(absenceDay));
    }

    public void reloadAllAbsenceDay() {
        List<AbsenceDay> absenceDayList = getAllAbsenceDay();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> remove(absenceDay));
        }

        List<Absence> absenceList = findAllAbsences();

        if (absenceList != null) {
            absenceList.forEach(absence -> registerAbsence(absence));
        }
    }

    private List<Absence> findAllAbsences() {
        return getNamedQueryList("findAllAbsences");
    }


    public List<WorkLoad> findOrCreateAllWorkLoad() {
        List<WorkLoad> workLoadList = getNamedQueryList("findAllWorkLoad");

        if (noWorkLoadList(workLoadList)) {
            workLoadList = createWorkLoads();
        } else {
            List<BusinessCase> businessCaseList = getNamedQueryList("findAllBC");

            List<Member> memberList = MemberService.getMemberList();
            if (workLoadList.size() != businessCaseList.size() * memberList.size()) {
                for (Member member : memberList) {
                    for (BusinessCase businessCase : businessCaseList) {

                        boolean found = false;
                        for (WorkLoad workLoad : workLoadList) {
                            if (businessCase.getId().equals(workLoad.getBusinessCase().getId()) && member.getId().equals(workLoad.getMember().getId())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            getLogger().warn("Creating missing workLoad businessCase" + businessCase.getId() + " member:" + member.getId());
                            WorkLoad workLoad = new WorkLoad(businessCase, member);
                            persist(workLoad);
                            workLoadList.add(workLoad);
                        }
                    }
                }
            }
        }

        return workLoadList;
    }

    private boolean noWorkLoadList(List<WorkLoad> workLoadList) {
        return workLoadList == null || workLoadList.isEmpty();
    }

    private List<WorkLoad> createWorkLoads() {
        List<WorkLoad> workLoadList;
        getLogger().info("Creation of workLoad");
        List<BusinessCase> businessCaseList = createNamedQuery("findAllBC").getResultList();

        List<Member> memberList = MemberService.getMemberList();


        workLoadList = buildAndSaveWorkLoadList(businessCaseList, memberList);
        return workLoadList;
    }

    private List<WorkLoad> buildAndSaveWorkLoadList(List<BusinessCase> businessCaseList, List<Member> memberList) {
        List<WorkLoad> workLoadList;
        workLoadList = Lists.newArrayListWithExpectedSize(businessCaseList.size() * memberList.size());

        businessCaseList.forEach(businessCase -> memberList.forEach(member -> {
            WorkLoad workLoad = new WorkLoad(businessCase, member);
            persist(workLoad);
            workLoadList.add(workLoad);
        }));
        return workLoadList;
    }


    public void updateWorkLoad(List<WorkLoad> workLoadList) {
        if (workLoadList != null) {
            workLoadList.forEach(workload -> merge(workload));
        }

    }
}
