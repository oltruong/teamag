package fr.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.model.Member;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * @author Olivier Truong
 */
public class WeekLoadWebBean {

    String weekNumberString;
    int yearNumber;
    DateTime firstDayWeek;
    Map<Member, Float> memberLoadMap;

    public WeekLoadWebBean(DateTime date) {
        int weekNumber = date.getWeekOfWeekyear();
        this.yearNumber = date.getYear();
        firstDayWeek = date.withDayOfWeek(1);

        if (date.getMonthOfYear() == 12 && weekNumber == 1) {
            weekNumber = 53;
        }

        if (weekNumber < 10) {
            weekNumberString = "0" + weekNumber;
        } else {
            weekNumberString = String.valueOf(weekNumber);
        }


        memberLoadMap = Maps.newHashMap();
    }


    public void setMemberLoadMap(Map<Member, Float> memberLoadMap) {
        this.memberLoadMap = memberLoadMap;
    }

    public Map<Member, Float> getMemberLoadMap() {
        return memberLoadMap;
    }

    public String getWeekNumberYear() {
        return yearNumber + "" + weekNumberString;
    }

    public String getFirstDay() {
        return firstDayWeek.toString("dd/MM");
    }

    public List<Member> getMemberList() {
        List<Member> memberList = Lists.newArrayListWithExpectedSize(memberLoadMap.size());
        memberList.addAll(memberLoadMap.keySet());
        return memberList;
    }
}
