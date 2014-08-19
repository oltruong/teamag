package fr.oltruong.teamag.transformer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.AbsenceDay;
import fr.oltruong.teamag.utils.CalendarUtils;
import org.joda.time.MutableDateTime;

import java.util.List;

public final class AbsenceDayTransformer {


    private AbsenceDayTransformer() {

    }

    public static List<AbsenceDay> transformAbsence(Absence absence) {

        Preconditions.checkArgument(absence != null);
        List<AbsenceDay> absenceDayList = null;


        if (isAbsenceSingleDay(absence)) {
            absenceDayList = buildAbsenceListSameDay(absence);

        } else {
            absenceDayList = buildAbsenceListDifferentDays(absence);
        }


        return absenceDayList;
    }

    private static boolean isAbsenceSingleDay(Absence absence) {
        return absence.getBeginDate().withTimeAtStartOfDay().isEqual(absence.getEndDate().withTimeAtStartOfDay());
    }

    private static List<AbsenceDay> buildAbsenceListDifferentDays(Absence absence) {
        List<AbsenceDay> absenceDayList = Lists.newArrayList();
        boolean firstDay = true;
        MutableDateTime beginMutableTime = absence.getBeginDate().withTimeAtStartOfDay().toMutableDateTime();
        MutableDateTime endMutableTime = absence.getEndDate().withTimeAtStartOfDay().toMutableDateTime();

        //days between
        while (!beginMutableTime.isEqual(endMutableTime)) {
            if (!CalendarUtils.isDayOff(beginMutableTime.toDateTime())) {
                AbsenceDay absenceDay = new AbsenceDay(absence);
                absenceDay.setDay(beginMutableTime.toDateTime());
                absenceDay.setMember(absence.getMember());

                if (firstDay && Absence.AFTERNOON_ONLY.equals(absence.getBeginType())) {
                    absenceDay.setValue(Float.valueOf(0.5f));
                }
                absenceDayList.add(absenceDay);
            }
            beginMutableTime.addDays(1);
            firstDay = false;
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

        return absenceDayList;
    }

    private static List<AbsenceDay> buildAbsenceListSameDay(Absence absence) {
        List<AbsenceDay> absenceDayList = Lists.newArrayListWithExpectedSize(1);

        if (!CalendarUtils.isDayOff(absence.getBeginDate())) {
            AbsenceDay absenceDay = new AbsenceDay(absence);
            absenceDay.setDay(absence.getBeginDate().withTimeAtStartOfDay());
            absenceDay.setMember(absence.getMember());

            if (Absence.MORNING_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getEndType())) {
                absenceDay.setValue(Float.valueOf(0.5f));
            }


            absenceDayList.add(absenceDay);

        }
        return absenceDayList;
    }

}
