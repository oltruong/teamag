package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.interfaces.SecurityChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    @Path("/activities")
    public Response getActivities() {
        return buildResponseOK(activityEJB.findActivities());
    }
}
