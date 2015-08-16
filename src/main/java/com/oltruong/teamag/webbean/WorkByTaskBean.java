package com.oltruong.teamag.webbean;

/**
 * @author Olivier Truong
 */
public class WorkByTaskBean {

    private String name;
    private Double total;

    public WorkByTaskBean(String task, Double total) {
        this.name = task;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public Double getTotal() {
        return total;
    }
}
