package fr.oltruong.teamag.validation;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.List;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBeanValidator {
    private AbsenceWebBeanValidator() {

    }

    public static void validate(AbsenceWebBean absenceWebBean, List<AbsenceWebBean> absenceWebBeanList) throws InconsistentDateException, DateOverlapException {

        Preconditions.checkArgument(absenceWebBean != null && absenceWebBean.getBeginDateTime() != null && absenceWebBean.getEndDateTime() != null);

        validateDatesAreConsistent(absenceWebBean);
        validateAbsenceDoNotOverlap(absenceWebBean, absenceWebBeanList);
    }

    private static void validateDatesAreConsistent(AbsenceWebBean absenceWebBean) throws InconsistentDateException {
        boolean dateInconsistent = false;

        DateTime beginDateTime = absenceWebBean.getBeginDateTime().withHourOfDay(0).withTimeAtStartOfDay();
        DateTime endDateTime = absenceWebBean.getEndDateTime().withHourOfDay(0).withTimeAtStartOfDay();

        dateInconsistent = beginDateTime.isAfter(endDateTime);
        dateInconsistent |= beginDateTime.equals(endDateTime) && absenceWebBean.getBeginType() > absenceWebBean.getEndType();

        if (dateInconsistent) {
            throw new InconsistentDateException();
        }
    }

    private static void validateAbsenceDoNotOverlap(AbsenceWebBean absenceWebBean, List<AbsenceWebBean> absenceWebBeanList) throws DateOverlapException {
        if (absenceWebBeanList != null) {

            DateTime[] dateTimes = computeDateTime(absenceWebBean);

            Interval interval = new Interval(dateTimes[0], dateTimes[1]);


            for (AbsenceWebBean absenceWebBean1 : absenceWebBeanList) {
                DateTime[] dateTimes1 = computeDateTime(absenceWebBean1);
                Interval interval1 = new Interval(dateTimes1[0], dateTimes1[1]);
                if (interval.overlaps(interval1)) {
                    throw new DateOverlapException();
                }
            }

        }
    }

    private static DateTime[] computeDateTime(AbsenceWebBean absenceWebBean) {
        DateTime beginTime = absenceWebBean.getBeginDateTime().withTimeAtStartOfDay();
        if (absenceWebBean.getBeginType() == Absence.AFTERNOON_ONLY) {
            beginTime = beginTime.plusHours(12);
        }

        DateTime endTime = absenceWebBean.getEndDateTime().withHourOfDay(0).withTimeAtStartOfDay();
        if (absenceWebBean.getEndType() == Absence.MORNING_ONLY) {
            endTime = endTime.plusHours(12);
        } else if (absenceWebBean.getEndType() == Absence.AFTERNOON_ONLY) {
            endTime = endTime.plusHours(20);
        }
        DateTime[] dateTimes = new DateTime[2];

        dateTimes[0] = beginTime;
        dateTimes[1] = endTime;
        return dateTimes;
    }

}


