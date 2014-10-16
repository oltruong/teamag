package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.interfaces.SecurityChecked;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.service.MemberService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
public class MemberEndPoint extends AbstractEndPoint {

    @Inject
    MemberService memberService;


    @GET

    public Response getMembers() {
        return buildResponseOK(memberService.findMembers());
    }


    @GET
    @Path("/profile")
    @SecurityChecked
    public Response getCurrentMember(@HeaderParam("userid") Long memberId) {
        return getMemberById(memberId);
    }


    @GET
    @Path("/{id}")

    public Response getMember(@PathParam("id") Long memberId) {
        return getMemberById(memberId);
    }

    private Response getMemberById(Long memberId) {
        Response response;
        Member member = memberService.findMember(memberId);
        if (member != null) {
            response = buildResponseOK(member);
        } else {
            response = buildResponseNotFound();
        }
        return response;
    }


    @POST

    public Response createMember(Member member) {
        memberService.createMemberWithAbsenceTask(member);
        return buildResponseCreated();
    }

    @PUT
    @Path("/{id}")
    @AdminChecked
    public Response updateMember(@PathParam("id") Long memberId, Member member) {
        member.setId(memberId);
        memberService.updateMember(member);
        return buildResponseOK();
    }


}
