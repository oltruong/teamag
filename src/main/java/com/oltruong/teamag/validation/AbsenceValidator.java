package com.oltruong.teamag.validation;

import com.google.common.base.Preconditions;
import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.model.Absence;
import java.time.LocalDate;
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

        LocalDate beginLocalDate = absence.getBeginDate();
        LocalDate endLocalDate = absence.getEndDate();

        dateInconsistent = beginLocalDate.isAfter(endLocalDate);
        dateInconsistent |= beginLocalDate.equals(endLocalDate) && absence.getBeginType() != absence.getEndType();
        dateInconsistent |= !beginLocalDate.equals(endLocalDate) && (Absence.MORNING_ONLY.equals(Integer.valueOf(absence.getBeginType())) || Absence.AFTERNOON_ONLY.equals(Integer.valueOf(absence.getEndType())));

        if (dateInconsistent) {
            throw new InconsistentDateException();
        }
    }

    private static void validateAbsenceDoNotOverlap(Absence absence, List<Absence> absenceList) throws DateOverlapException {
        if (absenceList != null) {
            LocalDate[] dateTimes = computeLocalDate(absence);
            Interval interval = new Interval(dateTimes[0], dateTimes[1]);

            for (Absence absence1 : absenceList) {
                LocalDate[] dateTimes1 = computeLocalDate(absence1);
                Interval interval1 = new Interval(dateTimes1[0], dateTimes1[1]);
                if (interval.overlaps(interval1) || interval.isEqual(interval1)) {
                    throw new DateOverlapException();
                }
            }

        }
    }

    private static LocalDate[] computeLocalDate(Absence absence) {
        LocalDate beginTime = absence.getBeginDate().withTimeAtStartOfDay();
        if (absence.getBeginType() == Absence.AFTERNOON_ONLY) {
            beginTime = beginTime.plusHours(12);
        }

        LocalDate endTime = absence.getEndDate().withHourOfDay(0).withTimeAtStartOfDay();
        if (absence.getEndType() == Absence.MORNING_ONLY) {
            endTime = endTime.plusHours(12);
        } else {
            endTime = endTime.plusHours(20);
        }
        LocalDate[] dateTimes = new LocalDate[2];

        dateTimes[0] = beginTime;
        dateTimes[1] = endTime;
        return dateTimes;
    }

}


