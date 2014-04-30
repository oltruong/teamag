package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.entity.Member;
import org.junit.Ignore;

import javax.inject.Inject;
import javax.naming.NamingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;


public class MemberEJBIT extends AbstractEJBIT {

    // ======================================
    // = Unit tests =
    // ======================================

    @Inject
    private MemberEJB memberEJB;

    //    @Ignore("Create a permGenSpace + require Guava 14.0")
    @Ignore
    public void createMember() throws NamingException {

        // Creates an instance of member
        Member member = EntityFactory.createMember();


        // Persists the member to the database
        member = memberEJB.createMemberWithAbsenceTask(member);
        assertThat(member.getId()).isNotNull();
        // Retrieves all the members from the database
        List<Member> members = memberEJB.findMembers();
        assertNotNull(members);
        assertThat(members).isNotNull();
        assertThat(members).isNotEmpty();
    }
}
