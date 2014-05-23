package fr.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.model.WorkRealized;

import java.util.List;

/**
 * @author Olivier Truong
 */
public class WorkRealizedWrapper {

    private TaskWebBean task;

    private List<WorkRealized> workRealizedList = Lists.newArrayList();


    public WorkRealizedWrapper(TaskWebBean task) {

        this.task = task;
    }

    public void setTask(TaskWebBean task) {
        this.task = task;
    }

    public TaskWebBean getTask() {
        return task;
    }

    public List<WorkRealized> getWorkRealizedList() {
        return workRealizedList;
    }

    public void setWorkRealizedList(List<WorkRealized> workRealizedList) {
        this.workRealizedList = workRealizedList;
    }

    public void addWorkRealized(WorkRealized workRealized) {
        if (workRealizedList == null) {
            workRealizedList = Lists.newArrayList();
        }
        workRealizedList.add(workRealized);
    }
}
