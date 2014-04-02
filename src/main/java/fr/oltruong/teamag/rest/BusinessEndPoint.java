package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.interfaces.SecurityChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("business")
@Stateless
@SecurityChecked
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
    @Path("/activities")
    public Response getActivities() {
        return buildResponseOK(activityEJB.findActivities());
    }

    @POST
    @Path("/bc")
    public Response createBC(BusinessCase businessCase) {
        try {
            activityEJB.createBC(businessCase);
        } catch (ExistingDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return buildResponseOK();
    }

    @PUT
    @Path("/bc/{id}")
    public Response updateBC(BusinessCase businessCase) {
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
