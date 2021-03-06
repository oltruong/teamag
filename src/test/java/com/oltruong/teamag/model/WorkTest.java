package com.oltruong.teamag.model;

import com.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkTest {


    private Work work;

    @Before
    public void setup() {
        work = new Work();
        work.setId(Long.valueOf(1l));

    }


    @Test
    public void setTotalStr() {

        TestUtils.setPrivateAttribute(work, LoggerFactory.getLogger(Work.class.getName()), "logger");

        work.setTotal(1.1d);

        assertThat(work.getTotalEdit()).isEqualTo(1.1d);

        work.setTotalEditStr("5");
        assertThat(work.getTotalEdit()).isEqualTo(5f);

        work.setTotalEditStr("sdqsd");
        assertThat(work.getTotalEdit()).isEqualTo(5f);

        work.setTotalEditStr(" ");
        assertThat(work.getTotalEdit()).isEqualTo(0f);

    }

    @Test
    public void getTotalEdit() {
        work.setTotal(3d);
        work.setTotalEdit(null);
        assertThat(work.getTotalEdit()).isEqualTo(3d);
    }

    @Test
    public void getTotalStr() {
        work.setTotalEdit(0d);
        assertThat(work.getTotalEditStr()).isEqualTo("");
        work.setTotalEdit(0.5d);
        assertThat(work.getTotalEditStr()).isEqualTo("0.5");
    }

    @Test
    public void hasChanged() {
        work.setTotal(1d);
        assertThat(work.hasChanged()).isFalse();
        work.setTotalEdit(19.29d);
        assertThat(work.hasChanged()).isTrue();
    }

    @Test
    public void getDay() {
        DateTime day = DateTime.now();
        work.setDay(day);
        assertThat(work.getDayStr()).isEqualTo(work.getDay().toString("E mmm dd"));
    }

}
