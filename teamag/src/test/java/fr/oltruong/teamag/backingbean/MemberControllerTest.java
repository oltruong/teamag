package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.MemberType;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest {

    @Mock
    private MemberEJB mockMemberEJB;

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
        assertThat(memberController.getMembertypeList()).isNotEmpty().hasSize(2).contains(MemberType.ADMINISTRATOR.toString(), MemberType.BASIC.toString());
    }

    @Ignore("to fix")
    public void testDoCreateMember() {
        assertThat(memberController.init()).isEqualTo(TestUtils.getPrivateAttribute(memberController, "VIEWNAME"));
    }

}
