package fr.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.entity.Member;

import java.util.List;
import java.util.Map;

/**
 * Created by m405640 on 11/03/14.
 */
public class WeekLoadWebBean {

    int weekNumber;
    Map<Member, Float> memberLoadMap;

    public WeekLoadWebBean(int weekNumber) {
        this.weekNumber = weekNumber;
        memberLoadMap = Maps.newHashMap();
    }

    public Map<Member, Float> getMemberLoadMap() {
        return memberLoadMap;
    }

    public int getWeekNumber() {
        System.out.println("GET WEEEEEK NUMBER" + weekNumber + " " + memberLoadMap.size());
        return weekNumber;
    }

    public List<Member> getMemberList() {
        List<Member> memberList = Lists.newArrayListWithExpectedSize(memberLoadMap.size());
        memberList.addAll(memberLoadMap.keySet());
        System.out.println("EEEEEEE" + weekNumber + " " + memberList.size());
        return memberList;
    }
}
