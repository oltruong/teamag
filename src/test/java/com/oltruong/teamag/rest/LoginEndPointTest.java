package com.oltruong.teamag.rest;

import com.oltruong.teamag.exception.UserNotFoundException;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TeamagUtils;
import com.oltruong.teamag.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
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

        assertThat(loginEndPoint.getService()).isEqualTo(mockMemberService);

    }

    @Test
    public void loginNull() throws Exception {
        loginInvalid(null);
    }


    @Test
    public void loginIncomplete() throws Exception {
        loginInvalid("login");
    }

    private void loginInvalid(String loginInformation) throws UserNotFoundException {
        Response response = loginEndPoint.login(loginInformation);

        checkResponseNotFound(response);

        verify(mockMemberService, never()).findMemberForAuthentication(any(), any());
    }

    @Test
    public void loginNotFound() throws Exception {

        when(mockMemberService.findMemberForAuthentication(any(), any())).thenThrow(new UserNotFoundException());

        Response response = loginEndPoint.login("hello-World");

        checkResponseNotFound(response);

        verify(mockMemberService).findMemberForAuthentication(any(), any());
    }

    @Test
    public void login() throws Exception {

        String username = "hi";
        String password = "password";

        Member member = EntityFactory.createMember();
        when(mockMemberService.findMemberForAuthentication(any(), any())).thenReturn(member);

        Response response = loginEndPoint.login(username + "-" + password);

        checkResponseOK(response);

        assertThat(response.getEntity()).isEqualTo(member);

        verify(mockMemberService).findMemberForAuthentication(eq(username), Matchers.eq(TeamagUtils.hashPassword(password)));
    }
}