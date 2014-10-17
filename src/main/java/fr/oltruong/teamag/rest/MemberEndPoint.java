package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.interfaces.SecurityChecked;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.utils.TeamagUtils;
import org.apache.commons.lang3.StringUtils;

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
    @AdminChecked
    public Response getMembers() {
        return buildResponseOK(memberService.findMembers());
    }


    @GET
    @Path("/profile")
    @SecurityChecked
    public Response getCurrentMember(@HeaderParam("userid") Long memberId) {
        return getMemberById(memberId);
    }


    @PUT
    @Path("/profile")
    @SecurityChecked
    public Response updateCurrentMember(@HeaderParam("userid") Long memberId, Member member) {

        if (StringUtils.isNotBlank(member.getNewPassword())) {
            member.setPassword(TeamagUtils.hashPassword(member.getNewPassword()));
        }
        return updateMember(memberId, member);
    }


    @GET
    @Path("/{id}")
    @AdminChecked
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
    @AdminChecked
    public Response createMember(Member member) {
        memberService.createMemberWithAbsenceTask(member);
        return buildResponseCreated();
    }

    @PUT
    @Path("/{id}")
    @AdminChecked
    public Response updateAnotherMember(@PathParam("id") Long memberId, Member member) {
        return updateMember(memberId, member);
    }

    private Response updateMember(Long memberId, Member member) {
        member.setId(memberId);
        memberService.updateMember(member);
        return buildResponseOK();
    }


}
