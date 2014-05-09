package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.interfaces.AdminChecked;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("member")
@Stateless
@AdminChecked
public class MemberEndPoint extends AbstractEndPoint {

    @EJB
    MemberService memberEJB;


    @GET
    public Response getMembers() {
        return Response.ok(memberEJB.findMembers()).build();
    }

    @GET
    @Path("/{id}")
    public Response getMember(@PathParam("id") Long memberId) {
        return buildResponseOK(memberEJB.findMember(memberId));
    }


    @POST
    public Response createMember(Member member) {
        memberEJB.createMemberWithAbsenceTask(member);
        return buildResponseCreated();
    }


    @PUT
    @Path("/{id}")
    public Response updateMember(@PathParam("id") Long memberId, Member member) {
        member.setId(memberId);
        memberEJB.updateMember(member);
        return buildResponseOK();
    }


}
