package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.service.MemberService;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.utils.MessageManager;
import fr.oltruong.teamag.utils.TeamagUtils;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.ProfileWebBean;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.enterprise.inject.Instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class ProfileControllerTest extends ControllerTest {


    @Mock
    private Instance<Member> mockMemberInstance;

    private Member member;

    @Mock
    private MemberService mockMemberEJB;

    private ProfileController profileController;

    @Before
    public void setup() {
        super.setup();

        member = EntityFactory.createMember();
        when(mockMemberInstance.get()).thenReturn(member);
        profileController = new ProfileController();
        TestUtils.setPrivateAttribute(profileController, mockMemberInstance, "memberInstance");
        TestUtils.setPrivateAttribute(profileController, mockMemberEJB, "memberEJB");
        TestUtils.setPrivateAttribute(profileController, Controller.class, mockMessageManager, "messageManager");
    }

    @Test
    public void testInit() throws Exception {

        String view = profileController.init();
        verify(mockMemberInstance).get();
        assertThat(profileController.getProfileWebBean().getMember()).isEqualTo(member);
        checkView(view);
    }

    private void checkView(String view) {
        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(profileController, "VIEWNAME"));
    }

    @Test
    public void testUpdatePassword() throws Exception {


        String newPassword = "SecurityFilter" + DateTime.now().toString();
        String newPasswordEncrypted = TeamagUtils.hashPassword(newPassword);

        profileController.setProfileWebBean(new ProfileWebBean(member));
        profileController.getProfileWebBean().setPassword(newPassword);

        assertThat(TestUtils.getPrivateAttribute(member, "password")).isNotEqualTo(newPasswordEncrypted);


        String view = profileController.updatePassword();

        assertThat(TestUtils.getPrivateAttribute(member, "password")).isEqualTo(newPasswordEncrypted);
        verify(mockMemberEJB).updateMember(eq(member));
        verify(mockMessageManager).displayMessage(eq(MessageManager.INFORMATION), anyString());
        checkView(view);

    }
}
