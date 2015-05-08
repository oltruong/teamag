package com.oltruong.teamag.webbean;

import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.builder.EntityFactory;
import java.time.LocalDate;

/**
 * @author Olivier Truong
 */
public class WebBeanFactory {

    private WebBeanFactory() {

    }

    public static AbsenceWebBean generateAbsenceWebBean() {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setBeginDate(LocalDate.now().toDate());
        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);

        absenceWebBean.setEndDate(LocalDate.now().plusDays(1).toDate());
        absenceWebBean.setEndType(Absence.MORNING_ONLY);

        return absenceWebBean;
    }


    public static ProfileWebBean generateProfileWebBean() {
        return new ProfileWebBean(EntityFactory.createMember());
    }

}
