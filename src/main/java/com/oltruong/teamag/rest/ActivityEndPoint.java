package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.service.ActivityService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author oltruong
 */
@Path("activity")
@Stateless
@AdminChecked
public class ActivityEndPoint extends AbstractEndPoint {

    @Inject
    ActivityService activityService;

    @GET
    public Response getActivities() {
        return get(() -> activityService.findActivities());
    }

    @GET
    @Path("/{id}")
    public Response getActivity(@PathParam("id") Long activityId) {
        return get(() -> activityService.find(activityId));
    }

    @POST
    public Response createActivity(Activity activity) {
        return create(() -> activityService.persist(activity));
    }


    @PUT
    @Path("/{id}")
    public Response updateActivity(@PathParam("id") Long activityId, Activity activity) {
        activity.setId(activityId);
        activityService.merge(activity);
        return ok();
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long activityId) {
        return delete(activityService::remove, activityId);
    }

}
