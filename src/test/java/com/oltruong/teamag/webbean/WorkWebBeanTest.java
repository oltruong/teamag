package com.oltruong.teamag.webbean;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class WorkWebBeanTest {


    @Test
    public void getDaylong() throws Exception {

        Date date = new Date();
        WorkWebBean workWebBean = new WorkWebBean();
        workWebBean.setDay(date);

        assertThat(workWebBean.getDaylong()).isEqualTo(date.getTime());
    }

}