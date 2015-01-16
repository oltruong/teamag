package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class MemberTest {

    private Member member;

    @Before
    public void setup() {
        member = EntityFactory.createMember();
        member.setComment(EntityFactory.createRandomDouble().toString());
    }

    @Test
    public void testIsSupervisor() {
        member.setMemberType(MemberType.ADMINISTRATOR);
        assertThat(member.isSupervisor()).isTrue();
        member.setMemberType(MemberType.SUPERVISOR);
        assertThat(member.isSupervisor()).isTrue();
        member.setMemberType(MemberType.BASIC);
        assertThat(member.isSupervisor()).isFalse();
    }


}
