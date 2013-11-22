package fr.oltruong.teamag.fr.oltruong.teamag.transformer;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.transformer.AbsenceTransformer;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class AbsenceTransformerTest {


    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(AbsenceTransformer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformNull() throws Exception {
        AbsenceTransformer.transform(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformWebBeanNull() throws Exception {
        AbsenceTransformer.transformWebBean(null);

    }

    @Test
    public void testTransformWebBean() throws Exception {
        AbsenceWebBean absenceWebBean = buildAbsenceWebBean();
        Absence absence = AbsenceTransformer.transformWebBean(absenceWebBean);
        compare(absenceWebBean, absence);
    }

    @Test
    public void testTransform() throws Exception {
        Absence absence = EntityFactory.createAbsence();
        AbsenceWebBean absenceWebBean = AbsenceTransformer.transform(absence);
        compare(absenceWebBean, absence);
    }

    @Test
    public void testTransformList() throws Exception {
        List<Absence> absenceList = EntityFactory.createAbsenceList(99);
        List<AbsenceWebBean> absenceWebBeanList = AbsenceTransformer.transformList(absenceList);

        assertThat(absenceWebBeanList).hasSameSizeAs(absenceList);
        for (int i = 0; i < absenceList.size(); i++) {
            compare(absenceWebBeanList.get(i), absenceList.get(i));
        }
    }

    private AbsenceWebBean buildAbsenceWebBean() {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setBeginType(Absence.MORNING_ONLY.intValue());
        absenceWebBean.setBeginDateTime((DateTime.now()));
        absenceWebBean.setEndDateTime((DateTime.now().plusDays(5)));
        absenceWebBean.setEndType(Absence.AFTERNOON_ONLY.intValue());
        return absenceWebBean;
    }

    private void compare(AbsenceWebBean absenceWebBean, Absence absence) {
        assertThat(new DateTime(absenceWebBean.getBeginDateTime())).isEqualTo(absence.getBeginDate());
        assertThat(new DateTime(absenceWebBean.getEndDate())).isEqualTo(absence.getEndDate());
        assertThat(absenceWebBean.getBeginType()).isEqualTo(absence.getBeginType().intValue());
        assertThat(absenceWebBean.getEndType()).isEqualTo(absence.getEndType().intValue());
    }
}
