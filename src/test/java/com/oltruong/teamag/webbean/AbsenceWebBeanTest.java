package com.oltruong.teamag.webbean;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AbsenceWebBeanTest {


    @Test
    public void getBeginDateTime() throws Exception {


        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        final DateTime dateTime = DateTime.parse("2015-09-07");

        absenceWebBean.setBeginDate(dateTime.toDate());
        assertThat(absenceWebBean.getBeginDateTime()).isEqualTo(dateTime.withTimeAtStartOfDay());
    }

    @Test
    public void getBeginDateTimeString() throws Exception {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        final String string = "2016-09-07";
        final DateTime dateTime = DateTime.parse(string);

        absenceWebBean.setBeginDateString(string);
        assertThat(absenceWebBean.getBeginDateString()).isEqualTo(string);
        assertThat(absenceWebBean.getBeginDateTime()).isEqualTo(dateTime);
        assertThat(absenceWebBean.getBeginDate()).isEqualTo(dateTime.toDate());
    }

    @Test
    public void getEndDateTime() throws Exception {


        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        final DateTime dateTime = DateTime.parse("2015-09-07");

        absenceWebBean.setEndDate(dateTime.toDate());
        assertThat(absenceWebBean.getEndDateTime()).isEqualTo(dateTime.withTimeAtStartOfDay());
    }

    @Test
    public void getEndDateTimeString() throws Exception {
        AbsenceWebBean absenceWebBean = new AbsenceWebBean();
        final String string = "2016-09-07";
        final DateTime dateTime = DateTime.parse(string);

        absenceWebBean.setEndDateString(string);
        assertThat(absenceWebBean.getEndDateString()).isEqualTo(string);
        assertThat(absenceWebBean.getEndDateTime()).isEqualTo(dateTime);
        assertThat(absenceWebBean.getEndDate()).isEqualTo(dateTime.toDate());

    }

}