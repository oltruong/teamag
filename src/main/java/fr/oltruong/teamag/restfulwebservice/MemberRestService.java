package fr.oltruong.teamag.restfulwebservice;

import fr.oltruong.teamag.ejb.MemberEJB;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("member")
@Stateless
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MemberRestService {

    @EJB
    MemberEJB memberEJB;

    @GET
    public Response getMembers() {
        return Response.ok(memberEJB.findActiveMembers()).build();
    }

//    @GET
//    @Produces("text/plain")
//    public String getMembers() {
//        return "camarche";
//    }
}
