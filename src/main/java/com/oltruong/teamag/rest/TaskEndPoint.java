package com.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.webbean.TaskWebBean;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("tasks")
@Stateless
@AdminChecked
public class TaskEndPoint extends AbstractEndPoint<Task> {

    @Inject
    private Logger LOGGER;

    @Inject
    TaskService taskService;

    @Inject
    MemberService memberService;

    @Override
    @GET
    public Response getAll() {
        return get(() -> buildTask(getService().findAll()));
    }

    @GET
    @Path("/nonadmin")
    public Response getNonAdmin() {
        return get(() -> buildTask(taskService.findAllNonAdminTasks()));
    }


    @POST
    public Response create(@HeaderParam("userid") Long memberId, @QueryParam("month") Integer month, @QueryParam("year") Integer year, Task task) {

        if (month == null || year == null) {
            return super.create(task);
        } else {
            DateTime theMonth = new DateTime(year, month, 1, 0, 0);
            try {
                taskService.persist(theMonth, memberService.find(memberId), task);
                return created(task.getId());
            } catch (EntityExistsException e) {
                LOGGER.info("trying to add an already existing task", e);
                return badRequest();
            }
        }

    }


    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("userid") Long memberId, @PathParam("id") Long id, @QueryParam("month") Integer month, @QueryParam("year") Integer year) {

        if (month == null || year == null) {
            return super.delete(id);
        } else {
            DateTime theMonth = new DateTime(year, month, 1, 0, 0);
            taskService.remove(id, memberId, theMonth);
            return noContent();
        }
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

    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long taskId, Task task) {
        task.setId(taskId);
        taskService.merge(task);
        return ok();
    }


    @Override
    AbstractService getService() {
        return taskService;
    }
}
