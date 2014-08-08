package fr.oltruong.teamag.transformer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.webbean.AbsenceWebBean;

import java.util.List;

/**
 * @author Olivier Truong
 */
public final class AbsenceWebBeanTransformer {

    private AbsenceWebBeanTransformer() {

    }

    public static Absence transformWebBean(AbsenceWebBean absenceWebBean) {
        Preconditions.checkArgument(absenceWebBean != null);
        Absence absence = new Absence();
        absence.setBeginDate(absenceWebBean.getBeginDateTime());
        absence.setEndDate(absenceWebBean.getEndDateTime());
        absence.setBeginType(Integer.valueOf(absenceWebBean.getBeginType()));
        absence.setEndType(Integer.valueOf(absenceWebBean.getEndType()));
        return absence;
    }

    public static AbsenceWebBean transform(Absence absence) {
        Preconditions.checkArgument(absence != null);
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setId(absence.getId());
        absenceWebBean.setBeginDateTime(absence.getBeginDate());
        absenceWebBean.setEndDateTime(absence.getEndDate());
        absenceWebBean.setBeginType(absence.getBeginType().intValue());
        absenceWebBean.setEndType(absence.getEndType().intValue());
        return absenceWebBean;
    }

    public static List<AbsenceWebBean> transformList(List<Absence> absenceList) {
        Preconditions.checkArgument(absenceList != null);
        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithExpectedSize(absenceList.size());
        absenceList.forEach(absence -> absenceWebBeanList.add(transform(absence)));
        return absenceWebBeanList;
    }
}
