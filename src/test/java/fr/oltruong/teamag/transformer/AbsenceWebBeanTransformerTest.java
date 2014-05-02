package fr.oltruong.teamag.transformer;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBeanTransformerTest {


    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(AbsenceWebBeanTransformer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformNull() throws Exception {
        AbsenceWebBeanTransformer.transform(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformWebBeanNull() throws Exception {
        AbsenceWebBeanTransformer.transformWebBean(null);

    }

    @Test
    public void testTransformWebBean() throws Exception {
        AbsenceWebBean absenceWebBean = buildAbsenceWebBean();
        Absence absence = AbsenceWebBeanTransformer.transformWebBean(absenceWebBean);
        compare(absenceWebBean, absence);
    }

    @Test
    public void testTransform() throws Exception {
        Absence absence = EntityFactory.createAbsence();
        AbsenceWebBean absenceWebBean = AbsenceWebBeanTransformer.transform(absence);
        compare(absenceWebBean, absence);
        assertThat(absenceWebBean.getId()).isEqualTo(absence.getId());
    }

    @Test
    public void testTransformList() throws Exception {
        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        List<AbsenceWebBean> absenceWebBeanList = AbsenceWebBeanTransformer.transformList(absenceList);

        assertThat(absenceWebBeanList).hasSameSizeAs(absenceList);
        for (int i = 0; i < absenceList.size(); i++) {
            compare(absenceWebBeanList.get(i), absenceList.get(i));
        }
    }

    private AbsenceWebBean buildAbsenceWebBean() {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setId(Long.valueOf(326l));
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
