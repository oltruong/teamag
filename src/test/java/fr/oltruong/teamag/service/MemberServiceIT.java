package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.Member;
import org.junit.Ignore;

import javax.inject.Inject;
import javax.naming.NamingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;


public class MemberServiceIT extends AbstractServiceIT {

    // ======================================
    // = Unit tests =
    // ======================================

    @Inject
    private MemberService memberEJB;

    //    @Ignore("Create a permGenSpace + require Guava 14.0")
    @Ignore
    public void createMember() throws NamingException {

        // Creates an instance of member
        Member member = EntityFactory.createMember();


        // Persists the member to the database
        member = memberEJB.create(member);
        assertThat(member.getId()).isNotNull();
        // Retrieves all the members from the database
        List<Member> members = memberEJB.findMembers();
        assertNotNull(members);
        assertThat(members).isNotNull();
        assertThat(members).isNotEmpty();
    }
}
