package fr.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.service.WorkService;
import fr.oltruong.teamag.webbean.TaskWebBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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

    @EJB
    WorkService workService;


    @GET
    public Response getTasks() {

        return buildResponseOK(buildTask(workService.findAllTasks()));

    }

    @GET
    @Path("/withactivity")
    public Response getTasksWithActivity() {
        return buildResponseOK(buildTask(workService.findTaskWithActivity()));
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
        return buildResponseOK(workService.findTask(taskId));
    }

    @POST
    public Response createTask(Task task) {
        try {
            workService.createTask(task);
        } catch (ExistingDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseCreated();
    }


    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long taskId, Task task) {
        task.setId(taskId);
        workService.updateTask(task);
        return buildResponseOK();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long taskId) {
        workService.deleteTask(taskId);
        return buildResponseOK();
    }


}
