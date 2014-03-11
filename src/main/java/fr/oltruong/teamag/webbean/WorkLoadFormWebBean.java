package fr.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Member;

import java.util.List;

/**
 * Created by m405640 on 11/03/14.
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