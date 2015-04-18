package com.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.oltruong.teamag.model.WorkLoad;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.Member;

import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Stateless
public class WorkLoadService extends AbstractService {

    public List<WorkLoad> findOrCreateAllWorkLoad() {
        @SuppressWarnings("unchecked")
        List<WorkLoad> workLoadList = getNamedQueryList("findAllWorkLoad");

        if (noWorkLoadList(workLoadList)) {
            workLoadList = createWorkLoads();
        } else {
            @SuppressWarnings("unchecked")
            List<BusinessCase> businessCaseList = getBusinessCaseList();

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

    private List<BusinessCase> getBusinessCaseList() {
        return getNamedQueryList("findAllBC");
    }

    private boolean noWorkLoadList(List<WorkLoad> workLoadList) {
        return workLoadList == null || workLoadList.isEmpty();
    }

    private List<WorkLoad> createWorkLoads() {
        List<WorkLoad> workLoadList;
        getLogger().info("Creation of workLoad");
        @SuppressWarnings("unchecked")
        List<BusinessCase> businessCaseList = getBusinessCaseList();
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

    public void createFromBusinessCase(BusinessCase businessCase) {
        List<Member> memberList = MemberService.getMemberList();
        if (memberList != null) {
            memberList.forEach(member -> createWorkLoad(businessCase, member));
        }
    }

    public void createFromMember(Member member) {
        List<BusinessCase> businessCaseList = getBusinessCaseList();
        if (businessCaseList != null) {
            businessCaseList.forEach(bc -> createWorkLoad(bc, member));
        }
    }

    private void createWorkLoad(BusinessCase businessCase, Member member) {
        WorkLoad workLoad = new WorkLoad(businessCase, member);
        persist(workLoad);
    }

}
