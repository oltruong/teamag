package com.oltruong.teamag.transformer;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.webbean.TaskWebBean;

import java.util.List;


public class TaskWebBeanTransformer {

    private TaskWebBeanTransformer() {
    }


    public static TaskWebBean transformTask(Task task) {
        TaskWebBean taskWebBean = new TaskWebBean();
        taskWebBean.setActivity(task.getActivity());
        taskWebBean.setAmount(task.getAmount());
        taskWebBean.setComment(task.getComment());
        taskWebBean.setDelegated(task.getDelegated());
        taskWebBean.setId(task.getId());
        taskWebBean.setName(task.getName());
        taskWebBean.setProject(task.getProject());
        taskWebBean.setTotal(task.getTotal());
        taskWebBean.setDescription(task.getDescription());

        if (task.getTask() != null) {
            taskWebBean.setTask(transformTask(task.getTask()));
        }

        return taskWebBean;
    }

    public static List<TaskWebBean> transformTaskList(List<Task> taskList) {

        List<TaskWebBean> taskWebBeanList = Lists.newArrayListWithExpectedSize(taskList.size());
        taskList.forEach(task -> taskWebBeanList.add(transformTask(task)));
        return taskWebBeanList;
    }
}
