package com.oltruong.teamag.transformer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.List;

public final class AbsenceDayTransformer {


    private AbsenceDayTransformer() {

    }

    public static List<AbsenceDay> transformAbsence(Absence absence) {

        Preconditions.checkArgument(absence != null);
        List<AbsenceDay> absenceDayList;

        if (isAbsenceSingleDay(absence)) {
            absenceDayList = buildAbsenceListSameDay(absence);
        } else {
            absenceDayList = buildAbsenceListDifferentDays(absence);
        }

        return absenceDayList;
    }

    private static boolean isAbsenceSingleDay(Absence absence) {
        return absence.getBeginDate().isEqual(absence.getEndDate());
    }

    private static List<AbsenceDay> buildAbsenceListDifferentDays(Absence absence) {
        List<AbsenceDay> absenceDayList = Lists.newArrayList();
        boolean firstDay = true;
        LocalDate beginDate = absence.getBeginDate();
        LocalDate endDate = absence.getEndDate();

        //days between
        while (!beginDate.isEqual(endDate)) {
            if (!CalendarUtils.isDayOff(beginDate)) {
                AbsenceDay absenceDay = new AbsenceDay(absence);
                absenceDay.setDay(beginDate);
                absenceDay.setMember(absence.getMember());

                if (firstDay && Absence.AFTERNOON_ONLY.equals(absence.getBeginType())) {
                    absenceDay.setValue(Float.valueOf(0.5f));
                }
                absenceDayList.add(absenceDay);
            }
            beginDate = beginDate.plusDays(1);
            firstDay = false;
        }

        //Final day
        if (!CalendarUtils.isDayOff(beginDate)) {
            AbsenceDay absenceDay = new AbsenceDay(absence);
            absenceDay.setDay(beginDate);
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
            absenceDay.setDay(absence.getBeginDate());
            absenceDay.setMember(absence.getMember());

            if (Absence.MORNING_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getBeginType()) || Absence.AFTERNOON_ONLY.equals(absence.getEndType())) {
                absenceDay.setValue(Float.valueOf(0.5f));
            }


            absenceDayList.add(absenceDay);

        }
        return absenceDayList;
    }

}
