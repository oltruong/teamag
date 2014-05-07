package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.interfaces.AdminChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("business")
@Stateless
@AdminChecked
public class BusinessEndPoint extends AbstractEndPoint {

    @EJB
    ActivityEJB activityEJB;


    @GET
    @Path("/bc")
    public Response getBC() {
        return buildResponseOK(activityEJB.findBC());
    }

    @GET
    @Path("/bc/{id}")
    public Response getBC(@PathParam("id") Long businessCaseId) {
        return buildResponseOK(activityEJB.findBC(businessCaseId));
    }

    @GET
    @Path("/activity")
    public Response getActivities() {
        return buildResponseOK(activityEJB.findActivities());
    }


    @GET
    @Path("/activity/{id}")
    public Response getActivity(@PathParam("id") Long activityId) {
        return buildResponseOK(activityEJB.findActivity(activityId));
    }

    @POST
    @Path("/activity")
    public Response createActivity(Activity activity) {
        try {
            activityEJB.createActivity(activity);
        } catch (ExistingDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseCreated();
    }


    @PUT
    @Path("/activity/{id}")
    public Response updateActivity(@PathParam("id") Long activityId, Activity activity) {
        activity.setId(activityId);

        activityEJB.updateActivity(activity);
        return buildResponseOK();
    }


    @DELETE
    @Path("/activity/{id}")
    public Response deleteActivity(@PathParam("id") Long activityId) {
        activityEJB.deleteActivity(activityId);
        return buildResponseOK();
    }


    @POST
    @Path("/bc")
    public Response createBC(BusinessCase businessCase) {
        try {
            activityEJB.createBC(businessCase);
        } catch (ExistingDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseCreated();
    }

    @PUT
    @Path("/bc/{id}")
    public Response updateBC(@PathParam("id") Long businessCaseId, BusinessCase businessCase) {
        businessCase.setId(businessCaseId);
        activityEJB.updateBC(businessCase);
        return buildResponseOK();
    }


    @DELETE
    @Path("/bc/{id}")
    public Response deleteBC(@PathParam("id") Long businessCaseId) {
        activityEJB.deleteBC(businessCaseId);
        return buildResponseOK();
    }
}
