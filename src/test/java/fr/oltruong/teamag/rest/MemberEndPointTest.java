package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.utils.TeamagUtils;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

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


    @Before
    public void prepare() {
        super.setup();
        memberEndPoint = new MemberEndPoint();

        TestUtils.setPrivateAttribute(memberEndPoint, mockMemberService, "memberService");

    }


    @Test
    public void testGetMembers() {
        List<Member> memberList = EntityFactory.createList(EntityFactory::createMember);
        when(mockMemberService.findMembers()).thenReturn(memberList);

        Response response = memberEndPoint.getMembers();
        checkResponseOK(response);

        List<Member> memberListReturned = (List<Member>) response.getEntity();

        assertThat(memberListReturned).isEqualTo(memberList);
    }

    @Test
    public void testGetCurrentMember() {
        testGetMemberById(() -> memberEndPoint.getCurrentMember(randomId));
    }

    @Test
    public void testGetCurrentMember_notFound() {
        testGetMemberById_notFound(() -> memberEndPoint.getCurrentMember(randomId));
    }

    @Test
    public void testGetMember() {
        testGetMemberById(() -> memberEndPoint.getMember(randomId));
    }

    @Test
    public void testGetMember_notFound() {
        testGetMemberById_notFound(() -> memberEndPoint.getMember(randomId));
    }


    private void testGetMemberById(Supplier<Response> supplier) {

        Member member = EntityFactory.createMember();

        when(mockMemberService.findMember(any())).thenReturn(member);

        Response response = supplier.get();
        checkResponseOK(response);
        Member memberReturned = (Member) response.getEntity();
        assertThat(memberReturned).isEqualTo(member);

        verify(mockMemberService).findMember(eq(randomId));

    }


    private void testGetMemberById_notFound(Supplier<Response> supplier) {
        Response response = supplier.get();
        checkResponseNotFound(response);
        verify(mockMemberService).findMember(eq(randomId));
    }


    @Test
    public void testCreateMember() {
        Member member = EntityFactory.createMember();
        Response response = memberEndPoint.createMember(member);

        checkResponseCreated(response);
        verify(mockMemberService).createMemberWithAbsenceTask(eq(member));
    }

    @Test
    public void testUpdateAnotherMember() {


        Member member = Mockito.spy(EntityFactory.createMember());

        assertThat(member.getId()).isNull();

        Response response = memberEndPoint.updateAnotherMember(randomId, member);

        checkResponseOK(response);
        assertThat(member.getId()).isEqualTo(randomId);

        verify(mockMemberService).updateMember(eq(member));

    }

    @Test
    public void testUpdateCurrentMember() {


        Member member = Mockito.spy(EntityFactory.createMember());

        assertThat(member.getId()).isNull();

        Response response = memberEndPoint.updateCurrentMember(randomId, member);

        checkResponseOK(response);
        assertThat(member.getId()).isEqualTo(randomId);

        verify(mockMemberService).updateMember(eq(member));

    }

    @Test
    public void testUpdateCurrentMember_newPassword() {


        Member member = Mockito.spy(EntityFactory.createMember());
        member.setNewPassword("newPassword");

        assertThat(member.getId()).isNull();

        Response response = memberEndPoint.updateCurrentMember(randomId, member);

        checkResponseOK(response);
        assertThat(member.getPassword()).isEqualTo(TeamagUtils.hashPassword(member.getNewPassword()));
        assertThat(member.getId()).isEqualTo(randomId);

        verify(mockMemberService).updateMember(eq(member));

    }


}