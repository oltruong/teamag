package com.oltruong.teamag.validation;

import com.google.common.collect.Lists;
import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

/**
 * @author Olivier Truong
 */
public class AbsenceValidatorTest {


    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(AbsenceValidator.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_null() throws DateOverlapException, InconsistentDateException {
        AbsenceValidator.validate(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_beginDatenull() throws DateOverlapException, InconsistentDateException {
        AbsenceValidator.validate(new Absence(), null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_endDatenull() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();
        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        AbsenceValidator.validate(absence, null);

    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronological() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 10, 22, 16, 40, 0));

        AbsenceValidator.validate(absence, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronologicalSameDay() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 22, 0, 40, 0));
        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setEndType(Absence.ALL_DAY);

        AbsenceValidator.validate(absence, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_InconsistentDate_gapAfternoon() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 25, 0, 40, 0));
        absence.setBeginType(Absence.MORNING_ONLY);
        absence.setEndType(Absence.ALL_DAY);

        AbsenceValidator.validate(absence, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_InconsistentDate_gapMorning() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 25, 0, 40, 0));
        absence.setBeginType(Absence.ALL_DAY);
        absence.setEndType(Absence.AFTERNOON_ONLY);

        AbsenceValidator.validate(absence, null);
    }

    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronologicalSameDay2() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 22, 0, 40, 0));
        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setEndType(Absence.MORNING_ONLY);

        AbsenceValidator.validate(absence, null);
    }


    @Test(expected = InconsistentDateException.class)
    public void testValidate_DateNonChronologicalSameDay3() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 22, 0, 40, 0));
        absence.setBeginType(Absence.MORNING_ONLY);
        absence.setEndType(Absence.AFTERNOON_ONLY);

        AbsenceValidator.validate(absence, null);
    }

    @Test
    public void testValidate() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 23, 16, 40, 0));
        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setEndType(Absence.MORNING_ONLY);
        AbsenceValidator.validate(absence, null);


        absence.setEndDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndType(Absence.AFTERNOON_ONLY);
        AbsenceValidator.validate(absence, null);

    }

    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlap() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 24, 0, 40, 0));
        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setEndType(Absence.MORNING_ONLY);


        Absence absence2 = new Absence();
        absence2.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence2.setEndDate(new DateTime(2013, 11, 23, 0, 40, 0));
        List<Absence> absenceList = Lists.newArrayListWithCapacity(1);
        absenceList.add(absence2);

        AbsenceValidator.validate(absence, absenceList);
    }


    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlapSameDay() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setBeginType(Absence.ALL_DAY);
        absence.setEndType(Absence.ALL_DAY);
        List<Absence> absenceList = Lists.newArrayListWithCapacity(1);
        absenceList.add(absence);


        AbsenceValidator.validate(absence, absenceList);
    }

    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlapInclude() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setEndDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence.setBeginType(Absence.ALL_DAY);
        absence.setEndType(Absence.ALL_DAY);

        Absence absence2 = new Absence();

        absence2.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence2.setEndDate(new DateTime(2013, 11, 22, 16, 40, 0));
        absence2.setBeginType(Absence.AFTERNOON_ONLY);
        absence2.setEndType(Absence.AFTERNOON_ONLY);

        List<Absence> absenceList = Lists.newArrayListWithCapacity(1);
        absenceList.add(absence2);


        AbsenceValidator.validate(absence, absenceList);
    }


    @Test(expected = DateOverlapException.class)
    public void testValidate_DateOverlap2() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));

        absence.setEndDate(new DateTime(2013, 11, 24, 0, 40, 0));
        absence.setEndType(Absence.MORNING_ONLY);


        Absence absence2 = new Absence();
        absence2.setBeginDate(new DateTime(2013, 11, 20, 16, 40, 0));

        absence2.setEndDate(new DateTime(2013, 11, 22, 0, 40, 0));
        absence2.setEndType(Absence.AFTERNOON_ONLY);

        List<Absence> absenceList = Lists.newArrayListWithCapacity(1);
        absenceList.add(absence2);

        AbsenceValidator.validate(absence, absenceList);
    }

    @Test
    public void testValidate_NoOverlap() throws DateOverlapException, InconsistentDateException {
        Absence absence = new Absence();

        absence.setBeginType(Absence.AFTERNOON_ONLY);
        absence.setBeginDate(new DateTime(2013, 11, 22, 16, 40, 0));

        absence.setEndDate(new DateTime(2013, 11, 24, 0, 40, 0));
        absence.setEndType(Absence.MORNING_ONLY);


        Absence absence2 = new Absence();
        absence2.setBeginDate(new DateTime(2013, 11, 20, 16, 40, 0));
        absence2.setEndDate(new DateTime(2013, 11, 22, 0, 40, 0));
        absence2.setBeginType(Absence.MORNING_ONLY);
        absence2.setEndType(Absence.MORNING_ONLY);
        List<Absence> absenceList = Lists.newArrayListWithCapacity(1);
        absenceList.add(absence2);

        AbsenceValidator.validate(absence, absenceList);
    }

}
