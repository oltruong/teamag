package com.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Member;

import java.util.List;

/**
 * @author Olivier Truong
 */
public class WorkLoadFormWebBean {

    List<WeekLoadWebBean> weekLoadWebBeanList = Lists.newArrayListWithExpectedSize(52);
    List<Member> memberList;

    public void addWeek(WeekLoadWebBean weekLoadWebBean) {
        weekLoadWebBeanList.add(weekLoadWebBean);
    }

    public List<WeekLoadWebBean> getWeekLoadWebBeanList() {
        return weekLoadWebBeanList;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}