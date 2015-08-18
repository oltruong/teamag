package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.transformer.TaskWebBeanTransformer;
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

/**
 * @author Olivier Truong
 */
@Path("tasks")
@Stateless
@SecurityChecked
public class TaskEndPoint extends AbstractEndPoint<Task> {

    @Inject
    private Logger LOGGER;

    @Inject
    TaskService taskService;

    @Inject
    MemberService memberService;

    @Override
    @GET
    @AdminChecked
    public Response getAll() {
        return get(() -> TaskWebBeanTransformer.transformTaskList(getService().findAll()));
    }

    @Override
    @GET
    @Path("/{id}")
    public Response getSingle(@PathParam("id") Long id) {
        return get(() -> TaskWebBeanTransformer.transformTask((Task) getService().find(id)));
    }

    @POST
    public Response create(@HeaderParam("userid") Long memberId, @QueryParam("month") Integer month, @QueryParam("year") Integer year, Task task) {

        if (month == null || year == null) {
            return super.create(task);
        } else {
            DateTime theMonth = new DateTime(year, month, 1, 0, 0);
            try {
                Task taskCreated = taskService.persist(theMonth, memberService.find(memberId), task);
                return created(taskCreated.getId());
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
            if (!memberService.find(memberId).isAdministrator()) {
                return forbidden();
            } else {
                return super.delete(id);
            }
        } else {
            DateTime theMonth = new DateTime(year, month, 1, 0, 0);
            taskService.remove(id, memberId, theMonth);
            return noContent();
        }
    }

    @GET
    @Path("/withactivity")
    public Response getTasksWithActivity() {
        return ok(TaskWebBeanTransformer.transformTaskList(taskService.findTaskWithActivity()));
    }


    @PUT
    @Path("/{id}")
    @AdminChecked
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
