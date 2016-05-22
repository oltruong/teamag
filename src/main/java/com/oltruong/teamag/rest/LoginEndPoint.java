package com.oltruong.teamag.rest;

import com.oltruong.teamag.exception.UserNotFoundException;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TeamagUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("login")
@Stateless
public class LoginEndPoint extends AbstractEndPoint<Member> {

    @Inject
    MemberService memberService;

    @GET
    @Path("/{loginInformation}")
    public Response login(@PathParam("loginInformation") String loginInformation) {


        Response response;
        if (loginInformation != null && loginInformation.contains("-")) {
            String username = extractUsername(loginInformation);
            String password = extractPassword(loginInformation);

            Member member;
            try {
                member = memberService.findMemberForAuthentication(username, TeamagUtils.hashPassword(password));
                response = ok(member);
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


    @Override
    AbstractService getService() {
        return memberService;
    }
}
