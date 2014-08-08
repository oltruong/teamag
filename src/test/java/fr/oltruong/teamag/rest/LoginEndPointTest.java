package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.utils.TeamagUtils;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginEndPointTest extends AbstractEndPointTest {


    private LoginEndPoint loginEndPoint;

    @Mock
    private MemberService mockMemberService;


    @Before
    public void prepare() {
        super.setup();

        loginEndPoint = new LoginEndPoint();

        TestUtils.setPrivateAttribute(loginEndPoint, mockMemberService, "memberService");
    }

    @Test
    public void testLogin_null() throws Exception {
        Response response = loginEndPoint.login(null);

        checkResponseNotFound(response);

        verify(mockMemberService, never()).findMemberForAuthentication(any(), any());
    }


    @Test
    public void testLogin_notFound() throws Exception {

        when(mockMemberService.findMemberForAuthentication(any(), any())).thenThrow(new UserNotFoundException());

        Response response = loginEndPoint.login("hello-World");

        checkResponseNotFound(response);

        verify(mockMemberService).findMemberForAuthentication(any(), any());
    }

    @Test
    public void testLogin() throws Exception {

        String username = "hi";
        String password = "password";

        Member member = EntityFactory.createMember();
        when(mockMemberService.findMemberForAuthentication(any(), any())).thenReturn(member);

        Response response = loginEndPoint.login(username + "-" + password);

        checkResponseOK(response);

        assertThat(response.getEntity()).isEqualTo(member);

        verify(mockMemberService).findMemberForAuthentication(eq(username), eq(TeamagUtils.hashPassword(password)));
    }
}