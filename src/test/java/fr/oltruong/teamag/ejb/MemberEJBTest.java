package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.enumeration.MemberType;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Olivier Truong
 */
public class MemberEJBTest extends AbstractEJBTest {


    private MemberEJB memberEJB;

    private List<Member> testMemberList;

    @Before
    public void init() {
        memberEJB = new MemberEJB();
        prepareEJB(memberEJB);

        buildMemberList();
        when(getMockQuery().getResultList()).thenReturn(testMemberList);

    }

    private void buildMemberList() {
        int numberOfMembers = 7;
        testMemberList = Lists.newArrayListWithExpectedSize(numberOfMembers);
        for (int i = 0; i < numberOfMembers; i++) {
            testMemberList.add(EntityFactory.createMember());
        }
    }

    @Test
    public void testFindActiveMembers() {

        List<Member> memberList = memberEJB.findActiveMembers();

        assertThat(memberList).isEqualTo(testMemberList);
        verify(getMockEntityManager()).createNamedQuery(eq("findActiveMembers"));
    }


    @Test
    public void testFindMembers() {

        List<Member> memberList = memberEJB.findMembers();

        assertThat(memberList).isEqualTo(testMemberList);
        verify(getMockEntityManager()).createNamedQuery(eq("findMembers"));
    }

    @Test
    public void testFindByNameNull() {
        List<Member> memberEmptyList = Lists.newArrayListWithExpectedSize(0);
        when(getMockQuery().getResultList()).thenReturn(memberEmptyList);

        try {
            memberEJB.findMemberForAuthentication(null, null);
            fail("UserNotFoundException expected");
        } catch (UserNotFoundException e) {

        }
        verify(getMockEntityManager()).createNamedQuery(eq("findByNamePassword"));

    }


    @Test
    public void testFindMember() {
        Member newMember = EntityFactory.createMember();
        Long id = Long.valueOf(365l);

        when(getMockEntityManager().find(eq(Member.class), any(Object.class))).thenReturn(newMember);

        Member memberFound = memberEJB.findMember(id);

        assertThat(memberFound).isEqualTo(newMember);
        verify(getMockEntityManager()).find(eq(Member.class), eq(id));

    }


    @Test
    public void testFindActiveNonAdminMembers() {

        int numberAdminMembers = 3;
        for (int i = 0; i < numberAdminMembers; i++) {
            Member member = testMemberList.get(i);
            member.setMemberType(MemberType.ADMINISTRATOR);
        }

        List<Member> memberList = memberEJB.findActiveNonAdminMembers();

        assertThat(memberList.size()).isEqualTo(testMemberList.size() - numberAdminMembers);

        assertThat(testMemberList).containsAll(memberList);
    }

    @Test
    public void testFindMemberForAuthentication() throws UserNotFoundException {

        String name = "FOOONAME";
        String password = "PASSWORD";


        assertThat(memberEJB.findMemberForAuthentication(name, password)).isNotNull().isEqualTo(testMemberList.get(0));
        verify(getMockEntityManager()).createNamedQuery(eq("findByNamePassword"));
        verify(getMockQuery()).setParameter("fname", name);

    }

    @Test
    public void testCreateMemberWithAbsenceTask() {

        List<Task> taskList = buildEmptyTaskList();
        taskList.add(new Task());
        testCreateMember(taskList);
        verify(getMockEntityManager()).persist(isA(Task.class));

    }

    @Test
    public void testCreateMemberWithoutAbsenceTask() {

        List<Task> taskList = buildEmptyTaskList();
        testCreateMember(taskList);
        verify(getMockEntityManager(), times(2)).persist(isA(Task.class));

    }

    @Test
    public void testUpdateMember() {
        Member member = EntityFactory.createMember();
        memberEJB.updateMember(member);

        verify(getMockEntityManager()).merge(eq(member));
    }

    private void testCreateMember(List<Task> taskList) {
        when(getMockQuery().getResultList()).thenReturn(taskList);

        Member member = EntityFactory.createMember();
        Member memberCreated = memberEJB.createMemberWithAbsenceTask(member);

        assertThat(memberCreated).isEqualTo(member);
        verify(getMockEntityManager()).createNamedQuery(eq("findTaskByName"));
        verify(getMockQuery()).setParameter(eq("fname"), isA(String.class));
        verify(getMockQuery()).setParameter(eq("fproject"), isA(String.class));


        verify(getMockEntityManager()).persist(eq(member));
    }

    private List<Task> buildEmptyTaskList() {

        List<Task> taskList = Lists.newArrayListWithCapacity(1);
        return taskList;
    }

}
