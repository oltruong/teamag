package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TeamagUtils;
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
public class MemberEndPoint extends AbstractEndPoint<Member> {

    @Inject
    MemberService memberService;


    @GET
    @AdminChecked
    public Response getMembers() {
        return ok(memberService.findAll());
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
        return get(() -> memberService.find(memberId));
    }


    @POST
    @AdminChecked
    public Response createMember(Member member) {
        memberService.persist(member);
        return created(member.getId());
    }

    @PUT
    @Path("/{id}")
    @AdminChecked
    public Response updateAnotherMember(@PathParam("id") Long memberId, Member member) {
        return updateMember(memberId, member);
    }

    private Response updateMember(Long memberId, Member member) {
        member.setId(memberId);
        memberService.merge(member);
        return ok();
    }


    @Override
    AbstractService getService() {
        return memberService;
    }
}
