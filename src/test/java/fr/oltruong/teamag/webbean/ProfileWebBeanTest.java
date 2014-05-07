package fr.oltruong.teamag.webbean;

import fr.oltruong.teamag.model.EntityFactory;
import fr.oltruong.teamag.model.Member;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class ProfileWebBeanTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        ProfileWebBean profileWebBean = new ProfileWebBean(null);
    }

    @Test
    public void testConstructor() {
        Member member = EntityFactory.createMember();
        ProfileWebBean profileWebBean = new ProfileWebBean(member);

        assertThat(member).isEqualTo(profileWebBean.getMember());
        assertThat(member.getName()).isEqualTo(profileWebBean.getName());
        assertThat(member.getCompany()).isEqualTo(profileWebBean.getCompany());
        assertThat(member.getEmail()).isEqualTo(profileWebBean.getEmail());
    }
}
