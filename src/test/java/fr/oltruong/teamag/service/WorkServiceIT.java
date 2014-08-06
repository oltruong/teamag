package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import org.junit.Ignore;

import javax.inject.Inject;
import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkServiceIT extends AbstractServiceIT {

    // ======================================
    // = Unit tests =
    // ======================================
    @Inject
    private MemberService memberEJB;

    // FIXME
    @Ignore("too long")
    public void createMember() throws NamingException {

        // Creates an instance of member
        Member member = new Member();

        member.setName("Carot");
        member.setCompany("My company");
        member.setEmail("dyummy@email.com");


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
