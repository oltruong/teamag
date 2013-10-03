package fr.oltruong.teamag.ejb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.naming.NamingException;

import org.junit.Ignore;

import fr.oltruong.teamag.entity.Member;

public class MemberEJBIT extends AbstractEJBIT {

    // ======================================
    // = Unit tests =
    // ======================================

    // FIXME
    @Ignore
    public void createMember() throws NamingException {

        // Creates an instance of member
        Member member = new Member();

        member.setName("Carot");
        member.setCompany("My company");
        member.setEmail("dummy@email.com");
        // Looks up for the EJB
        MemberEJB memberEJB = (MemberEJB) getContext().lookup("java:global/classes/MemberEJB");

        // Persists the member to the database
        member = memberEJB.createMember(member);
        assertNotNull("ID should not be null", member.getId());

        // Retrieves all the members from the database
        List<Member> members = memberEJB.findMembers();
        assertNotNull(members);
        assertTrue(!members.isEmpty());
    }
}
