package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.ActivityService;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

    @PUT
    @Path("/{id}")
    public Response updateActivity(@PathParam("id") Long activityId, Activity activity) {
        activity.setId(activityId);
        activityService.merge(activity);
        return ok();
    }


    @Override
    AbstractService getService() {
        return activityService;
    }
}
