package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.interfaces.SecurityChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("member")
@Stateless
@SecurityChecked
public class MemberEndPoint extends AbstractEndPoint {

    @EJB
    MemberEJB memberEJB;


    @GET
    public Response getMembers() {
        return Response.ok(memberEJB.findActiveMembers()).build();
    }


}
