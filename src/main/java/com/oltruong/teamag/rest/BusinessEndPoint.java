package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.service.ActivityService;
import com.oltruong.teamag.service.BusinessCaseService;

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
        return ok(businessCaseService.findAll());
    }

    @GET
    @Path("/bc/{id}")
    public Response getBC(@PathParam("id") Long businessCaseId) {
        return ok(businessCaseService.find(businessCaseId));
    }

    @GET
    @Path("/activity")
    public Response getActivities() {
        return get(() -> activityService.findActivities());
    }


    @GET
    @Path("/activity/{id}")
    public Response getActivity(@PathParam("id") Long activityId) {
        return get(() -> activityService.findActivity(activityId));
    }

    @POST
    @Path("/activity")
    public Response createActivity(Activity activity) {
        try {
            activityService.createActivity(activity);
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return created();
    }


    @PUT
    @Path("/activity/{id}")
    public Response updateActivity(@PathParam("id") Long activityId, Activity activity) {
        activity.setId(activityId);

        activityService.updateActivity(activity);
        return ok();
    }


    @DELETE
    @Path("/activity/{id}")
    public Response deleteActivity(@PathParam("id") Long activityId) {
        activityService.deleteActivity(activityId);
        return ok();
    }


    @POST
    @Path("/bc")
    public Response createBC(BusinessCase businessCase) {
        try {
            businessCaseService.create(businessCase);
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return created();
    }

    @PUT
    @Path("/bc/{id}")
    public Response updateBC(@PathParam("id") Long businessCaseId, BusinessCase businessCase) {
        businessCase.setId(businessCaseId);
        businessCaseService.update(businessCase);
        return ok();
    }


    @DELETE
    @Path("/bc/{id}")
    public Response deleteBC(@PathParam("id") Long businessCaseId) {
        businessCaseService.delete(businessCaseId);
        return ok();
    }
}
