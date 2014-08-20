package fr.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.WorkLoad;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class WorkLoadService extends AbstractService {


    @Inject
    private WorkService workService;

    @SuppressWarnings("unchecked")
    public List<AbsenceDay> getAllAbsenceDay() {
        return getNamedQueryList("findAllAbsenceDays");

    }

    @SuppressWarnings("unchecked")
    public void removeAbsence(Long id) {

        Preconditions.checkArgument(id != null);
        Query query = createNamedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", id);

        List<AbsenceDay> absenceDayList = query.getResultList();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> {
                remove(absenceDay);
                workService.removeWorkAbsence(absenceDay);
            });
        }

    }


    public void registerAbsence(Absence newAbsence) {
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(newAbsence);
        absenceDayList.forEach(absenceDay -> {
            persist(absenceDay);
            workService.updateWorkAbsence(absenceDay);
        });
    }


    public void reloadAllAbsenceDay() {
        List<AbsenceDay> absenceDayList = getAllAbsenceDay();
        if (absenceDayList != null) {
            absenceDayList.forEach(absenceDay -> {
                remove(absenceDay);
                workService.removeWorkAbsence(absenceDay);
            });
        }

        List<Absence> absenceList = findAllAbsences();

        if (absenceList != null) {
            absenceList.forEach(absence -> registerAbsence(absence));
        }
    }

    @SuppressWarnings("unchecked")
    private List<Absence> findAllAbsences() {
        return getNamedQueryList("findAllAbsences");
    }


    public List<WorkLoad> findOrCreateAllWorkLoad() {
        @SuppressWarnings("unchecked")
        List<WorkLoad> workLoadList = getNamedQueryList("findAllWorkLoad");

        if (noWorkLoadList(workLoadList)) {
            workLoadList = createWorkLoads();
        } else {
            @SuppressWarnings("unchecked")
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
        @SuppressWarnings("unchecked")
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
        Preconditions.checkArgument(workLoadList != null);
        workLoadList.forEach(workload -> merge(workload));

    }


    public void updateWorkLoadWithRealized(Table<Member, BusinessCase, Double> values) {
        Preconditions.checkArgument(values != null);
        List<WorkLoad> existingWorkLoadList = findOrCreateAllWorkLoad();

        values.rowMap().forEach((member, value) ->
                        value.forEach((bc, realized) -> {
                            WorkLoad workLoad = findOrCreate(member, bc, existingWorkLoadList);
                            workLoad.setRealized(realized);
                            merge(workLoad);
                        })

        );
        Preconditions.checkNotNull(values);


    }

    private WorkLoad findOrCreate(Member member, BusinessCase bc, List<WorkLoad> existingWorkLoadList) {

        if (existingWorkLoadList != null) {
            for (WorkLoad workLoad : existingWorkLoadList) {
                if (workLoad.getBusinessCase().equals(bc) && workLoad.getMember().equals(member)) {
                    return workLoad;
                }
            }
        }

        WorkLoad workLoad = new WorkLoad();
        workLoad.setBusinessCase(bc);
        workLoad.setMember(member);

        return workLoad;
    }
}
