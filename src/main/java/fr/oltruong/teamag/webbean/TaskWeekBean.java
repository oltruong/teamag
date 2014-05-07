package fr.oltruong.teamag.webbean;

import com.google.common.collect.Maps;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.Work;

import java.util.Collection;
import java.util.Map;

public class TaskWeekBean implements Comparable<TaskWeekBean> {

    private Task task;

    private final Map<String, Work> mapWorks = Maps.newHashMapWithExpectedSize(5);

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Collection<Work> getWorks() {
        return mapWorks.values();
    }

    private Work getWork(String columnDay) {

        return mapWorks.get(columnDay);
    }

    public void addWork(String column, Work work) {
        mapWorks.put(column, work);
    }

    public Work getDay6() {
        return getWork("day6");
    }

    public Work getDay1() {
        return getWork("day1");
    }

    public Work getDay2() {
        return getWork("day2");
    }

    public Work getDay3() {
        return getWork("day3");
    }

    public Work getDay4() {
        return getWork("day4");
    }

    public Work getDay5() {
        return getWork("day5");
    }

    @Override
    public int compareTo(TaskWeekBean otherTask) {

        return task.compareTo(otherTask.getTask());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaskWebBean) {
            return task.equals((TaskWebBean) ((TaskWebBean) obj).getTask());
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
