package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.utils.TeamagUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("login")
@Stateless
public class LoginEndPoint extends AbstractEndPoint {

    @EJB
    MemberService memberEJB;

    @GET
    @Path("/{loginInformation}")
    public Response login(@PathParam("loginInformation") String loginInformation) {


        Response response = null;
        if (loginInformation != null && loginInformation.contains("-")) {
            String username = extractUsername(loginInformation);
            String password = extractPassword(loginInformation);

            Member member;
            try {
                member = memberEJB.findMemberForAuthentication(username, TeamagUtils.hashPassword(password));
                response = buildResponseOK(member);
            } catch (UserNotFoundException e) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }

        return response;
    }

    private String extractPassword(String loginInformation) {

        return loginInformation.substring(loginInformation.indexOf("-") + 1);

    }

    private String extractUsername(String loginInformation) {
        return loginInformation.substring(0, loginInformation.indexOf("-"));
    }


}
