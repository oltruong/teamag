package fr.oltruong.teamag.backingbean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.MemberType;
import fr.oltruong.teamag.utils.TestUtils;

public class MemberControllerTest {

    @Mock
    private MemberEJB mockMemberEJB;

    private MemberController memberController;

    private final String VIEW_NAME = "newMember";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        memberController = new MemberController();
        TestUtils.setPrivateAttribute(memberController, mockMemberEJB, "memberEJB");
    }

    @Test
    public void testDoNewMemberForm() {

        assertThat(memberController.doNewMemberForm()).isEqualTo(VIEW_NAME);
    }

    @Test
    public void testGetMembertypeList() {
        assertThat(memberController.getMembertypeList()).isNotEmpty().hasSize(2).contains(MemberType.ADMINISTRATOR.toString(), MemberType.BASIC.toString());
    }

    @Ignore("to fix")
    public void testDoCreateMember() {
        assertThat(memberController.doCreateMember()).isEqualTo(VIEW_NAME);
    }

}
