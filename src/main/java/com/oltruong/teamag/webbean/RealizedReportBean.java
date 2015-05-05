package com.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Task;

import java.util.List;

public class RealizedReportBean {
    private String name;

    private List<Task> tasks = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getTotalRealized() {
        Double total = 0d;
        for (Task task : tasks) {
            total += task.getTotal();
        }
        return total.toString();
    }

    public String getTotalWorkRealized() {
        Double total = 0d;
        for (Task task : tasks) {

            if (!task.isAbsenceTask()) {
                total += task.getTotal();
            }
        }
        return total.toString();
    }

}
