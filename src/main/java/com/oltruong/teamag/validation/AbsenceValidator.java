package com.oltruong.teamag.validation;

import com.google.common.base.Preconditions;
import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.model.Absence;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.List;

/**
 * @author Olivier Truong
 */
public final class AbsenceValidator {
    private AbsenceValidator() {

    }

    public static void validate(Absence absence, List<Absence> absenceList) throws InconsistentDateException, DateOverlapException {

        Preconditions.checkArgument(absence != null && absence.getBeginDate() != null && absence.getEndDate() != null);

        validateDatesAreConsistent(absence);
        validateAbsenceDoNotOverlap(absence, absenceList);
    }

    private static void validateDatesAreConsistent(Absence absence) throws InconsistentDateException {
        boolean dateInconsistent = false;

        DateTime beginDateTime = absence.getBeginDate().withHourOfDay(0).withTimeAtStartOfDay();
        DateTime endDateTime = absence.getEndDate().withHourOfDay(0).withTimeAtStartOfDay();

        dateInconsistent = beginDateTime.isAfter(endDateTime);
        dateInconsistent |= beginDateTime.equals(endDateTime) && absence.getBeginType() != absence.getEndType();
        dateInconsistent |= !beginDateTime.equals(endDateTime) && (Absence.MORNING_ONLY.equals(Integer.valueOf(absence.getBeginType())) || Absence.AFTERNOON_ONLY.equals(Integer.valueOf(absence.getEndType())));

        if (dateInconsistent) {
            throw new InconsistentDateException();
        }
    }

    private static void validateAbsenceDoNotOverlap(Absence absence, List<Absence> absenceList) throws DateOverlapException {
        if (absenceList != null) {
            DateTime[] dateTimes = computeDateTime(absence);
            Interval interval = new Interval(dateTimes[0], dateTimes[1]);

            for (Absence absence1 : absenceList) {
                DateTime[] dateTimes1 = computeDateTime(absence1);
                Interval interval1 = new Interval(dateTimes1[0], dateTimes1[1]);
                if (interval.overlaps(interval1) || interval.isEqual(interval1)) {
                    throw new DateOverlapException();
                }
            }

        }
    }

    private static DateTime[] computeDateTime(Absence absence) {
        DateTime beginTime = absence.getBeginDate().withTimeAtStartOfDay();
        if (absence.getBeginType() == Absence.AFTERNOON_ONLY) {
            beginTime = beginTime.plusHours(12);
        }

        DateTime endTime = absence.getEndDate().withHourOfDay(0).withTimeAtStartOfDay();
        if (absence.getEndType() == Absence.MORNING_ONLY) {
            endTime = endTime.plusHours(12);
        } else {
            endTime = endTime.plusHours(20);
        }
        DateTime[] dateTimes = new DateTime[2];

        dateTimes[0] = beginTime;
        dateTimes[1] = endTime;
        return dateTimes;
    }

}


