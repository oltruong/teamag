package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TeamagUtils;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MemberEndPointTest extends AbstractEndPointTest {

    @Mock
    MemberService mockMemberService;

    MemberEndPoint memberEndPoint;

    Member member;


    @Before
    public void prepare() {
        super.setup();
        memberEndPoint = new MemberEndPoint();
        member = EntityFactory.createMember();
        TestUtils.setPrivateAttribute(memberEndPoint, mockMemberService, "memberService");
        TestUtils.setPrivateAttribute(memberEndPoint, AbstractEndPoint.class, mockUriInfo, "uriInfo");

        assertThat(memberEndPoint.getService()).isEqualTo(mockMemberService);

    }


    @Test
    public void getMembers() {
        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);
        when(mockMemberService.findAll()).thenReturn(memberList);

        Response response = memberEndPoint.getMembers();
        checkResponseOK(response);

        List<Member> memberListReturned = (List<Member>) response.getEntity();

        assertThat(memberListReturned).isEqualTo(memberList);
    }

    @Test
    public void getCurrentMember() {
        getMemberById(() -> memberEndPoint.getCurrentMember(randomId));
    }

    @Test
    public void getCurrentMemberNotFound() {
        getMemberByIdNotFound(() -> memberEndPoint.getCurrentMember(randomId));
    }

    @Test
    public void getMember() {
        getMemberById(() -> memberEndPoint.getMember(randomId));
    }

    @Test
    public void getMemberNotFound() {
        getMemberByIdNotFound(() -> memberEndPoint.getMember(randomId));
    }


    private void getMemberById(Supplier<Response> supplier) {
        when(mockMemberService.find(any())).thenReturn(member);

        Response response = supplier.get();
        checkResponseOK(response);
        Member memberReturned = (Member) response.getEntity();
        assertThat(memberReturned).isEqualTo(member);

        verify(mockMemberService).find(eq(randomId));

    }


    private void getMemberByIdNotFound(Supplier<Response> supplier) {
        Response response = supplier.get();
        checkResponseNotFound(response);
        verify(mockMemberService).find(eq(randomId));
    }


    @Test
    public void createMember() {
        member.setId(randomId);

        Response response = memberEndPoint.createMember(member);

        checkResponseCreated(response);
        verify(mockMemberService).persist(eq(member));
    }

    @Test
    public void updateAnotherMember() {


        assertThat(member.getId()).isNull();

        Response response = memberEndPoint.updateAnotherMember(randomId, member);

        checkResponseOK(response);
        assertThat(member.getId()).isEqualTo(randomId);

        verify(mockMemberService).merge(eq(member));

    }

    @Test
    public void updateCurrentMember() {
        assertThat(member.getId()).isNull();

        Response response = memberEndPoint.updateCurrentMember(randomId, member);

        checkResponseOK(response);
        assertThat(member.getId()).isEqualTo(randomId);

        verify(mockMemberService).merge(eq(member));

    }

    @Test
    public void updateCurrentMemberNewPassword() {

        member.setNewPassword("newPassword");

        assertThat(member.getId()).isNull();

        Response response = memberEndPoint.updateCurrentMember(randomId, member);

        checkResponseOK(response);
        assertThat(member.getPassword()).isEqualTo(TeamagUtils.hashPassword(member.getNewPassword()));
        assertThat(member.getId()).isEqualTo(randomId);

        verify(mockMemberService).merge(eq(member));

    }


}