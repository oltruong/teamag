package com.oltruong.teamag.transformer;

import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.TaskWebBean;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


public class TaskWebBeanTransformerTest {
    @Test
    public void constructorIsPrivate() {
        TestUtils.constructorIsPrivate(TaskWebBeanTransformer.class);
    }

    @Test
    public void transformTask() {
        Task task = EntityFactory.createTask();
        TaskWebBean taskWebBean = TaskWebBeanTransformer.transformTask(task);
        testEquals(task, taskWebBean);
    }

    protected void testEquals(Task task, TaskWebBean taskWebBean) {
        assertThat(taskWebBean.getActivity()).isEqualTo(task.getActivity());
        assertThat(taskWebBean.getAmount()).isEqualTo(task.getAmount());
        assertThat(taskWebBean.getComment()).isEqualTo(task.getComment());
        assertThat(taskWebBean.getDelegated()).isEqualTo(task.getDelegated());
        assertThat(taskWebBean.getId()).isEqualTo(task.getId());
        assertThat(taskWebBean.getName()).isEqualTo(task.getName());
        assertThat(taskWebBean.getProject()).isEqualTo(task.getProject());
        assertThat(taskWebBean.getTotal()).isEqualTo(task.getTotal());
        assertThat(taskWebBean.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void transformTaskSubTask() {
        Task task = EntityFactory.createTask();
        task.setTask(EntityFactory.createTask());
        TaskWebBean taskWebBean = TaskWebBeanTransformer.transformTask(task);
        testEquals(task.getTask(), taskWebBean.getTask());
    }


    @Test
    public void transformTaskList() {

        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        List<TaskWebBean> taskWebBeanList = TaskWebBeanTransformer.transformTaskList(taskList);

        assertThat(taskWebBeanList).hasSameSizeAs(taskList);
        IntStream.range(0, taskWebBeanList.size()).forEach(i -> testEquals(taskList.get(i), taskWebBeanList.get(i)));

    }


}