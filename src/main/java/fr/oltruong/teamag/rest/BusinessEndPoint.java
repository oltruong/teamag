package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.service.ActivityService;
import fr.oltruong.teamag.service.BusinessCaseService;

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

/**
 * @author Olivier Truong
 */
@Path("business")
@Stateless
@AdminChecked
public class BusinessEndPoint extends AbstractEndPoint {

    @Inject
    ActivityService activityService;

    @Inject
    BusinessCaseService businessCaseService;


    @GET
    @Path("/bc")
    public Response getBC() {
        return buildResponseOK(businessCaseService.findAll());
    }

    @GET
    @Path("/bc/{id}")
    public Response getBC(@PathParam("id") Long businessCaseId) {
        return buildResponseOK(businessCaseService.find(businessCaseId));
    }

    @GET
    @Path("/activity")
    public Response getActivities() {
        return buildResponseOK(activityService.findActivities());
    }


    @GET
    @Path("/activity/{id}")
    public Response getActivity(@PathParam("id") Long activityId) {
        return buildResponseOK(activityService.findActivity(activityId));
    }

    @POST
    @Path("/activity")
    public Response createActivity(Activity activity) {
        try {
            activityService.createActivity(activity);
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseCreated();
    }


    @PUT
    @Path("/activity/{id}")
    public Response updateActivity(@PathParam("id") Long activityId, Activity activity) {
        activity.setId(activityId);

        activityService.updateActivity(activity);
        return buildResponseOK();
    }


    @DELETE
    @Path("/activity/{id}")
    public Response deleteActivity(@PathParam("id") Long activityId) {
        activityService.deleteActivity(activityId);
        return buildResponseOK();
    }


    @POST
    @Path("/bc")
    public Response createBC(BusinessCase businessCase) {
        try {
            businessCaseService.create(businessCase);
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseCreated();
    }

    @PUT
    @Path("/bc/{id}")
    public Response updateBC(@PathParam("id") Long businessCaseId, BusinessCase businessCase) {
        businessCase.setId(businessCaseId);
        businessCaseService.update(businessCase);
        return buildResponseOK();
    }


    @DELETE
    @Path("/bc/{id}")
    public Response deleteBC(@PathParam("id") Long businessCaseId) {
        businessCaseService.delete(businessCaseId);
        return buildResponseOK();
    }
}
