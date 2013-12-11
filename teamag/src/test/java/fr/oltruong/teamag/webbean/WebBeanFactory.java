package fr.oltruong.teamag.webbean;

import fr.oltruong.teamag.entity.Absence;
import org.joda.time.DateTime;

/**
 * @author Olivier Truong
 */
public class WebBeanFactory {

    private WebBeanFactory() {

    }

    public static AbsenceWebBean generateAbsenceWebBean() {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setBeginDate(DateTime.now().toDate());
        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);

        absenceWebBean.setEndDate(DateTime.now().plusDays(1).toDate());
        absenceWebBean.setEndType(Absence.MORNING_ONLY);

        return absenceWebBean;
    }

}
