package com.oltruong.teamag.webbean;

import java.util.Date;

/**
 * @author Olivier Truong
 */
public class WorkWebBean {

    private String task;

    private String member;

    private Date day;

    private Double amount;


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public Date getDay() {
        return day;
    }

    public long getDaylong() {
        return day.getTime();
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
