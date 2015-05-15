package com.oltruong.teamag.webbean;

import java.util.Date;

/**
 * @author Olivier Truong
 */
public class WorkWebBean {


    private String project;
    private TaskWebBean taskBean;
    private String task;

    private String member;

    private Date day;

    private Double amount;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public TaskWebBean getTaskBean() {
        return taskBean;
    }

    public void setTaskBean(TaskWebBean task) {
        this.taskBean = task;
    }

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

    public Double getOriginal() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
