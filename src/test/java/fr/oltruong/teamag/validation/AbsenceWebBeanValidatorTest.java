package fr.oltruong.teamag.validation;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author Olivier Truong
 */
public class AbsenceWebBeanValidatorTest {


    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(AbsenceWebBeanValidator.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_null() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBeanValidator.validate(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_beginDatenull() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        AbsenceWebBeanValidator.validate(absenceWebBean, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_endDatenull() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        AbsenceWebBeanValidator.validate(absenceWebBean, null);

    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronological() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 10, 22, 16, 40, 0));

        AbsenceWebBeanValidator.validate(absenceWebBean, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronologicalSameDay() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 22, 0, 40, 0));
        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean.setEndType(Absence.ALL_DAY);

        AbsenceWebBeanValidator.validate(absenceWebBean, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_InconsistentDate_gapAfternoon() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 25, 0, 40, 0));
        absenceWebBean.setBeginType(Absence.MORNING_ONLY);
        absenceWebBean.setEndType(Absence.ALL_DAY);

        AbsenceWebBeanValidator.validate(absenceWebBean, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_InconsistentDate_gapMorning() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 25, 0, 40, 0));
        absenceWebBean.setBeginType(Absence.ALL_DAY);
        absenceWebBean.setEndType(Absence.AFTERNOON_ONLY);

        AbsenceWebBeanValidator.validate(absenceWebBean, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronologicalSameDay2() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 22, 0, 40, 0));
        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean.setEndType(Absence.MORNING_ONLY);

        AbsenceWebBeanValidator.validate(absenceWebBean, null);
    }

    @Test
    public void testValidate() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 23, 16, 40, 0));
        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean.setEndType(Absence.MORNING_ONLY);
        AbsenceWebBeanValidator.validate(absenceWebBean, null);


        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndType(Absence.AFTERNOON_ONLY);
        AbsenceWebBeanValidator.validate(absenceWebBean, null);

    }

    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlap() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 24, 0, 40, 0));
        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean.setEndType(Absence.MORNING_ONLY);


        AbsenceWebBean absenceWebBean2 = new AbsenceWebBean();
        absenceWebBean2.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean2.setEndDateTime(new DateTime(2013, 11, 23, 0, 40, 0));
        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithCapacity(1);
        absenceWebBeanList.add(absenceWebBean2);

        AbsenceWebBeanValidator.validate(absenceWebBean, absenceWebBeanList);
    }


    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlapSameDay() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setBeginType(Absence.ALL_DAY);
        absenceWebBean.setEndType(Absence.ALL_DAY);
        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithCapacity(1);
        absenceWebBeanList.add(absenceWebBean);


        AbsenceWebBeanValidator.validate(absenceWebBean, absenceWebBeanList);
    }

    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlapInclude() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean.setBeginType(Absence.ALL_DAY);
        absenceWebBean.setEndType(Absence.ALL_DAY);

        AbsenceWebBean absenceWebBean2 = new AbsenceWebBean();

        absenceWebBean2.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean2.setEndDateTime(new DateTime(2013, 11, 22, 16, 40, 0));
        absenceWebBean2.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean2.setEndType(Absence.AFTERNOON_ONLY);

        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithCapacity(1);
        absenceWebBeanList.add(absenceWebBean2);


        AbsenceWebBeanValidator.validate(absenceWebBean, absenceWebBeanList);
    }


    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlap2() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));

        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 24, 0, 40, 0));
        absenceWebBean.setEndType(Absence.MORNING_ONLY);


        AbsenceWebBean absenceWebBean2 = new AbsenceWebBean();
        absenceWebBean2.setBeginDateTime(new DateTime(2013, 11, 20, 16, 40, 0));

        absenceWebBean2.setEndDateTime(new DateTime(2013, 11, 22, 0, 40, 0));
        absenceWebBean2.setEndType(Absence.AFTERNOON_ONLY);

        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithCapacity(1);
        absenceWebBeanList.add(absenceWebBean2);

        AbsenceWebBeanValidator.validate(absenceWebBean, absenceWebBeanList);
    }

    @Test
    public void testValidate_NoOverlap() throws DateOverlapException, InconsistentDateException {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();

        absenceWebBean.setBeginType(Absence.AFTERNOON_ONLY);
        absenceWebBean.setBeginDateTime(new DateTime(2013, 11, 22, 16, 40, 0));

        absenceWebBean.setEndDateTime(new DateTime(2013, 11, 24, 0, 40, 0));
        absenceWebBean.setEndType(Absence.MORNING_ONLY);


        AbsenceWebBean absenceWebBean2 = new AbsenceWebBean();
        absenceWebBean2.setBeginDateTime(new DateTime(2013, 11, 20, 16, 40, 0));
        absenceWebBean2.setEndDateTime(new DateTime(2013, 11, 22, 0, 40, 0));
        absenceWebBean2.setBeginType(Absence.MORNING_ONLY);
        absenceWebBean2.setEndType(Absence.MORNING_ONLY);
        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithCapacity(1);
        absenceWebBeanList.add(absenceWebBean2);

        AbsenceWebBeanValidator.validate(absenceWebBean, absenceWebBeanList);
    }

}
