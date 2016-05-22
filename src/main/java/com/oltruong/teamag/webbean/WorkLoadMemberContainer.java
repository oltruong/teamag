package com.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Member;

import java.util.List;

/**
 * @author Olivier Truong
 */
public class WorkLoadMemberContainer {

    private List<Member> memberList;

    private List<WorkLoadContainer> workLoadContainerList;


    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public List<WorkLoadContainer> getWorkLoadContainerList() {
        return workLoadContainerList;
    }

    public void setWorkLoadContainerList(List<WorkLoadContainer> workLoadContainerList) {
        this.workLoadContainerList = workLoadContainerList;
    }

    public void addWorkLoadContainer(WorkLoadContainer container) {
        if (this.workLoadContainerList == null) {
            workLoadContainerList = Lists.newArrayList();
        }
        workLoadContainerList.add(container);
    }
}
