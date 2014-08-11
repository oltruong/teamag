package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.service.MemberService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("member")
@Stateless
@AdminChecked
public class MemberEndPoint extends AbstractEndPoint {

    @EJB
    MemberService memberService;


    @GET
    public Response getMembers() {
        return buildResponseOK(memberService.findMembers());
    }

    @GET
    @Path("/{id}")
    public Response getMember(@PathParam("id") Long memberId) {
        return buildResponseOK(memberService.findMember(memberId));
    }


    @POST
    public Response createMember(Member member) {
        memberService.createMemberWithAbsenceTask(member);
        return buildResponseCreated();
    }


    @PUT
    @Path("/{id}")
    public Response updateMember(@PathParam("id") Long memberId, Member member) {
        member.setId(memberId);
        memberService.updateMember(member);
        return buildResponseOK();
    }


}
