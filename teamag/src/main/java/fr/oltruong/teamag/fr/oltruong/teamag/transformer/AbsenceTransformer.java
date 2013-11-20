package fr.oltruong.teamag.fr.oltruong.teamag.transformer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Olivier Truong
 */
public final class AbsenceTransformer {

    private AbsenceTransformer() {

    }

    public static Absence transformWebBean(AbsenceWebBean absenceWebBean) {
        Preconditions.checkArgument(absenceWebBean != null);
        Absence absence = new Absence();
        absence.setBeginDate(new DateTime(absenceWebBean.getBeginDate()));
        absence.setEndDate(new DateTime(absenceWebBean.getEndDate()));
        absence.setBeginType(Integer.valueOf(absenceWebBean.getBeginType()));
        absence.setEndType(Integer.valueOf(absenceWebBean.getEndType()));
        return absence;
    }

    public static AbsenceWebBean transform(Absence absence) {
        Preconditions.checkArgument(absence != null);
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setBeginDate(absence.getBeginDate().toDate());
        absenceWebBean.setEndDate(absence.getEndDate().toDate());
        absenceWebBean.setBeginType(absence.getBeginType().intValue());
        absenceWebBean.setEndType(absence.getEndType().intValue());
        return absenceWebBean;
    }

    public static List<AbsenceWebBean> transformList(List<Absence> absenceList) {
        Preconditions.checkArgument(absenceList != null);
        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithExpectedSize(absenceList.size());
        for (Absence absence : absenceList) {
            absenceWebBeanList.add(transform(absence));
        }
        return absenceWebBeanList;
    }
}
