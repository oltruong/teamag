package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest {

    @Mock
    private MemberService mockMemberEJB;

    private MemberController memberController;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        memberController = new MemberController();
        TestUtils.setPrivateAttribute(memberController, mockMemberEJB, "memberEJB");
    }

    @Test
    public void testInitList() {
        assertThat(memberController.init()).isEqualTo(TestUtils.getPrivateAttribute(memberController, "VIEWNAME"));
    }

    @Test
    public void testGetMembertypeList() {
        assertThat(memberController.getMembertypeList()).isNotEmpty().hasSize(3).contains(MemberType.ADMINISTRATOR.toString(), MemberType.BASIC.toString(), MemberType.SUPERVISOR.toString());
    }

    @Ignore("to fix")
    public void testDoCreateMember() {
        assertThat(memberController.init()).isEqualTo(TestUtils.getPrivateAttribute(memberController, "VIEWNAME"));
    }

}
