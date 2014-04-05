package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.interfaces.AdminChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("task")
@Stateless
@AdminChecked
public class TaskEndPoint extends AbstractEndPoint {

    @EJB
    WorkEJB workEJB;


    @GET
    public Response getTasks() {
        return Response.ok(workEJB.findAllTasks()).build();
    }

    @GET
    @Path("/{id}")
    public Response getTask(@PathParam("id") Long taskId) {
        return buildResponseOK(workEJB.findTask(taskId));
    }

    @POST
    public Response createTask(Task task) {
        try {
            workEJB.createTask(task);
        } catch (ExistingDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseOK();
    }


    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long taskId, Task task) {
        task.setId(taskId);
        workEJB.updateTask(task);
        return buildResponseOK();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long taskId) {
        workEJB.deleteTask(taskId);
        return buildResponseOK();
    }


}
