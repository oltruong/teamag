package fr.oltruong.teamag.webbean;

import java.util.ArrayList;
import java.util.List;

import fr.oltruong.teamag.entity.Task;

public class RealizedReportBean {
    private String name;

    private List<Task> tasks = new ArrayList<Task>();

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
	Float total = 0f;
	for (Task task : tasks) {
	    total += task.getTotal();
	}
	return total.toString();
    }

    public String getTotalWorkRealized() {
	Float total = 0f;
	for (Task task : tasks) {

	    if (task.getId().intValue() != 1) {
		total += task.getTotal();
	    }
	}
	return total.toString();
    }

}
