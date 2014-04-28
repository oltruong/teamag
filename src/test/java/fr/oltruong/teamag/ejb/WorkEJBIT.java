package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import org.junit.Ignore;

import javax.inject.Inject;
import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkEJBIT extends AbstractEJBIT {

    // ======================================
    // = Unit tests =
    // ======================================
    @Inject
    private MemberEJB memberEJB;

    // FIXME
    @Ignore("too long")
    public void createMember() throws NamingException {

        // Creates an instance of member
        Member member = new Member();

        member.setName("Carot");
        member.setCompany("My company");
        member.setEmail("dyummy@email.com");


        // Persists the member to the database
        member = memberEJB.createMemberWithAbsenceTask(member);
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
