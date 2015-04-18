package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.service.AbsenceDayService;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.webbean.WeekLoadWebBean;
import com.oltruong.teamag.webbean.WorkLoadFormWebBean;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;


@SessionScoped
@ManagedBean
public class WorkLoadController extends Controller {

    @Inject
    private AbsenceDayService absenceDayService;

    @Inject
    private MemberService memberEJB;


    private WorkLoadFormWebBean formWebBean;

    private static final String VIEWNAME = "workload";


    public String init() {
        formWebBean = new WorkLoadFormWebBean();
        fillInformation();
        return VIEWNAME;
    }


    private void fillInformation() {
        List<AbsenceDay> absenceDayList = absenceDayService.findAll();

        List<Member> memberNonAdminList = memberEJB.findActiveNonAdminMembers();
        formWebBean.setMemberList(memberNonAdminList);
        MutableDateTime dayCursor = DateTime.now().withTimeAtStartOfDay().withDayOfYear(1).toMutableDateTime();

        int currentYear = dayCursor.getYear();

        int weekCursor = dayCursor.getWeekOfWeekyear() - 1;
        WeekLoadWebBean weekLoadWebBean = null;
        while (dayCursor.getYear() == currentYear) {
            if (!CalendarUtils.isDayOff(dayCursor.toDateTime())) {


                //New week
                if (weekCursor != dayCursor.getWeekOfWeekyear()) {

                    weekCursor = dayCursor.getWeekOfWeekyear();
                    weekLoadWebBean = new WeekLoadWebBean(dayCursor.toDateTime());
                    formWebBean.addWeek(weekLoadWebBean);
                }

                for (Member member : memberNonAdminList) {
                    AbsenceDay absenceDay = findAbsenceDay(member, dayCursor.toDateTime(), absenceDayList);
                    float value = 1f;
                    if (absenceDay != null) {
                        value = value - absenceDay.getValue().floatValue();
                    }
                    addValue(weekLoadWebBean, member, value);
                }
            }
            dayCursor.addDays(1);
        }
    }

    private void addValue(WeekLoadWebBean weekLoadWebBean, Member member, float value) {
        if (weekLoadWebBean.getMemberLoadMap().containsKey(member)) {
            Float total = weekLoadWebBean.getMemberLoadMap().get(member) + value;
            weekLoadWebBean.getMemberLoadMap().put(member, total);
        } else {
            weekLoadWebBean.getMemberLoadMap().put(member, Float.valueOf(value));
        }
    }

    private AbsenceDay findAbsenceDay(Member member, DateTime dateTime, List<AbsenceDay> absenceDayList) {
        for (AbsenceDay absenceDay : absenceDayList) {
            if (absenceDay.getDay().isEqual(dateTime) && absenceDay.getMember().equals(member)) {
                return absenceDay;
            }
        }

        return null;
    }

    public WorkLoadFormWebBean getFormWebBean() {
        return formWebBean;
    }


}
