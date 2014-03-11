package fr.oltruong.teamag.transformer;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.AbsenceDay;
import fr.oltruong.teamag.utils.CalendarUtils;
import org.joda.time.MutableDateTime;

import java.util.List;

public final class AbsenceDayTransformer {


    public static List<AbsenceDay> transformAbsence(Absence absence) {
        List<AbsenceDay> absenceDayList = Lists.newArrayList();


        if (absence.getBeginDate().withTimeAtStartOfDay().isEqual(absence.getEndDate().withTimeAtStartOfDay())) {//Same day

            if (!CalendarUtils.isDayOff(absence.getBeginDate())) {
                AbsenceDay absenceDay = new AbsenceDay(absence);
                absenceDay.setDay(absence.getBeginDate().withTimeAtStartOfDay());
                absenceDay.setMember(absence.getMember());

                if (Absence.MORNING_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getEndType())) {
                    absenceDay.setValue(Float.valueOf(0.5f));
                }


                absenceDayList.add(absenceDay);

            }

        } else {//Not same day

            MutableDateTime beginMutableTime = absence.getBeginDate().withTimeAtStartOfDay().toMutableDateTime();
            MutableDateTime endMutableTime = absence.getEndDate().withTimeAtStartOfDay().toMutableDateTime();


            //First day

            //Other days

            while (!beginMutableTime.isEqual(endMutableTime)) {//days between
                if (!CalendarUtils.isDayOff(beginMutableTime.toDateTime())) {
                    AbsenceDay absenceDay = new AbsenceDay(absence);
                    absenceDay.setDay(beginMutableTime.toDateTime());
                    absenceDay.setMember(absence.getMember());

                    if (Absence.AFTERNOON_ONLY.equals(absence.getBeginType())) {
                        absenceDay.setValue(Float.valueOf(0.5f));
                    }
                    absenceDayList.add(absenceDay);

                }
                beginMutableTime.addDays(1);

            }

            //Final day
            if (!CalendarUtils.isDayOff(beginMutableTime.toDateTime())) {
                AbsenceDay absenceDay = new AbsenceDay(absence);
                absenceDay.setDay(beginMutableTime.toDateTime());
                absenceDay.setMember(absence.getMember());

                if (Absence.MORNING_ONLY.equals(absence.getEndType())) {
                    absenceDay.setValue(Float.valueOf(0.5f));
                }
                absenceDayList.add(absenceDay);
            }


        }


        return absenceDayList;
    }

}
