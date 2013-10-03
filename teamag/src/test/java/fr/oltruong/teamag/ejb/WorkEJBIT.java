package fr.oltruong.teamag.ejb;

import static org.fest.assertions.Assertions.assertThat;

import javax.naming.NamingException;

import org.junit.Ignore;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;

public class WorkEJBIT extends AbstractEJBIT {

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
        member.setEmail("dyummy@email.com");

        // Looks up for the EJB
        MemberEJB memberEJB = (MemberEJB) getContext().lookup("java:global/classes/MemberEJB");

        // Persists the member to the database
        member = memberEJB.createMember(member);
        assertThat(member.getId()).isNotNull();

        Task task = new Task();
        task.setName("my task!!!");

        task.setProject("my project");

        task.addMember(member);

        // WorkEJB workEJB = (WorkEJB) ctx.lookup( "java:global/classes/WorkEJB" );

        // workEJB.createTask( task );

        assertThat(task.getId()).isNotNull();

    }
}
