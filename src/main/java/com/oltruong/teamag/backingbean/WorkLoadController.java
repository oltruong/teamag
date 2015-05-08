package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.service.AbsenceDayService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.webbean.WeekLoadWebBean;
import com.oltruong.teamag.webbean.WorkLoadFormWebBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;


@SessionScoped
@ManagedBean
public class WorkLoadController extends Controller {

    @Inject
    private AbsenceDayService absenceDayService;

    @Inject
    private MemberService memberService;


    private WorkLoadFormWebBean formWebBean;

    private static final String VIEWNAME = "workload";


    public String init() {
        formWebBean = new WorkLoadFormWebBean();
        fillInformation();
        return VIEWNAME;
    }


    private void fillInformation() {
        List<AbsenceDay> absenceDayList = absenceDayService.findAll();

        List<Member> memberNonAdminList = memberService.findActiveNonAdminMembers();
        formWebBean.setMemberList(memberNonAdminList);
        LocalDate dayCursor = LocalDate.now().withDayOfYear(1);

        int currentYear = dayCursor.getYear();

        int weekCursor = dayCursor.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) - 1;
        WeekLoadWebBean weekLoadWebBean = null;
        while (dayCursor.getYear() == currentYear) {
            if (!CalendarUtils.isDayOff(dayCursor)) {


                //New week
                if (weekCursor != dayCursor.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())) {

                    weekCursor = dayCursor.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                    weekLoadWebBean = new WeekLoadWebBean(dayCursor);
                    formWebBean.addWeek(weekLoadWebBean);
                }

                for (Member member : memberNonAdminList) {
                    AbsenceDay absenceDay = findAbsenceDay(member, dayCursor, absenceDayList);
                    float value = 1f;
                    if (absenceDay != null) {
                        value = value - absenceDay.getValue().floatValue();
                    }
                    addValue(weekLoadWebBean, member, value);
                }
            }
            dayCursor = dayCursor.plusDays(1);
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

    private AbsenceDay findAbsenceDay(Member member, LocalDate dateTime, List<AbsenceDay> absenceDayList) {
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
