package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.ejb.WorkLoadEJB;
import fr.oltruong.teamag.entity.AbsenceDay;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.webbean.WeekLoadWebBean;
import fr.oltruong.teamag.webbean.WorkLoadFormWebBean;
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
    private WorkLoadEJB workLoadEJB;

    @Inject
    private MemberEJB memberEJB;


    private WorkLoadFormWebBean formWebBean;

    private static final String VIEWNAME = "workload";


    public String init() {
        formWebBean = new WorkLoadFormWebBean();
        fillInformation();
        return VIEWNAME;
    }

//    private void fillInformation() {
//        Map<Integer, Map<Member, Float>> map = workLoadEJB.buildWeekLoad();
//
//        List<Member> memberNonAdminList = memberEJB.findActiveNonAdminMembers();
//        formWebBean.setMemberList(memberNonAdminList);
//
//
//        int weekCursor = 1;
//        WeekLoadWebBean weekLoadWebBean = null;
//        while (weekCursor <= 53) {
//
//            float workedDays = getWorkedDays(weekCursor);
//
//            Map<Member, Float> memberFloatMap = map.get(Integer.valueOf(weekCursor));
//            weekLoadWebBean = new WeekLoadWebBean(getFirstDayWeek(weekCursor));
//
//
//            for (Member member : memberFloatMap.keySet()) {
//                Float valueComputed = Float.valueOf(workedDays - memberFloatMap.get(member).floatValue());
//
//                memberFloatMap.put(member, valueComputed);
//            }
//            weekLoadWebBean.setMemberLoadMap(memberFloatMap);
//
//
//            weekCursor++;
//        }
//
//
//        MutableDateTime dayCursor = DateTime.now().withTimeAtStartOfDay().withDayOfYear(1).toMutableDateTime();
//
//        int currentYear = dayCursor.getYear();
//
//        int weekCursor = dayCursor.getWeekOfWeekyear() - 1;
//        WeekLoadWebBean weekLoadWebBean = null;
//        while (dayCursor.getYear() == currentYear) {
//            if (!CalendarUtils.isDayOff(dayCursor.toDateTime())) {
//
//
//                if (weekCursor != dayCursor.getWeekOfWeekyear()) {//New week
//
//                    weekCursor = dayCursor.getWeekOfWeekyear();
//                    weekLoadWebBean = new WeekLoadWebBean(dayCursor.toDateTime());
//                    formWebBean.addWeek(weekLoadWebBean);
//                }
//
//                for (Member member : memberNonAdminList) {
//                    AbsenceDay absenceDay = findAbsenceDay(member, dayCursor.toDateTime(), absenceDayList);
//                    float value = 1f;
//                    if (absenceDay != null) {
//                        value = value - absenceDay.getValue().floatValue();
//                    }
//                    addValue(weekLoadWebBean, member, value);
//                }
//            }
//            dayCursor.addDays(1);
//        }
//    }
//
//    private DateTime getFirstDayWeek(int weekCursor) {
//        int currentYear = 0;
//        if (weekCursor == 53) {
//            weekCursor = 1;
//            currentYear = 1;
//        }
//
//        float total = 0f;
//        DateTime cursor = DateTime.now().withDayOfWeek(1).withWeekOfWeekyear(weekCursor).plusYears(currentYear);
//        return cursor;
//    }
//
//    private float getWorkedDays(int weekCursor) {
//        int currentYear = 0;
//        if (weekCursor == 53) {
//            weekCursor = 1;
//            currentYear = 1;
//        }
//
//        float total = 0f;
//        DateTime cursor = DateTime.now().withDayOfWeek(1).withWeekOfWeekyear(weekCursor).plusYears(currentYear);
//        for (int i = 0; i < 7; i++) {
//            if (!CalendarUtils.isDayOff(cursor.plusDays(i))) {
//                total++;
//            }
//        }
//
//
//        return total;
//    }


    private void fillInformation() {
        // workLoadEJB.buildWeekLoad();
        List<AbsenceDay> absenceDayList = workLoadEJB.getAllAbsenceDay();

        List<Member> memberNonAdminList = memberEJB.findActiveNonAdminMembers();
        formWebBean.setMemberList(memberNonAdminList);
        MutableDateTime dayCursor = DateTime.now().withTimeAtStartOfDay().withDayOfYear(1).toMutableDateTime();

        int currentYear = dayCursor.getYear();

        int weekCursor = dayCursor.getWeekOfWeekyear() - 1;
        WeekLoadWebBean weekLoadWebBean = null;
        while (dayCursor.getYear() == currentYear) {
            if (!CalendarUtils.isDayOff(dayCursor.toDateTime())) {


                if (weekCursor != dayCursor.getWeekOfWeekyear()) {//New week

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
