package com.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.webbean.TaskWebBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("task")
@Stateless
@AdminChecked
public class TaskEndPoint extends AbstractEndPoint {

    @Inject
    TaskService taskService;

    @GET
    public Response getTasks() {
        return ok(buildTask(taskService.findAll()));
    }

    @GET
    @Path("/withactivity")
    public Response getTasksWithActivity() {
        return ok(buildTask(taskService.findTaskWithActivity()));
    }

    private List<TaskWebBean> buildTask(List<Task> taskList) {


        List<TaskWebBean> taskWebBeanList = Lists.newArrayListWithExpectedSize(taskList.size());

        taskList.forEach(task -> taskWebBeanList.add(transformTask(task)));

        return taskWebBeanList;
    }


    private TaskWebBean transformTask(Task task) {
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

    @GET
    @Path("/{id}")
    public Response getTask(@PathParam("id") Long taskId) {
        return ok(taskService.find(taskId));
    }

    @POST
    public Response createTask(Task task) {
        try {
            taskService.createTask(task);
        } catch (EntityExistsException e) {
            return notAcceptable();
        }
        return created();
    }


    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long taskId, Task task) {
        task.setId(taskId);
        taskService.merge(task);
        return ok();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long taskId) {
        return delete(taskService::remove, taskId);
    }


}
