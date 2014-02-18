package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.utils.TestUtils;
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

    @Before
    public void init() {
        memberEJB = new MemberEJB();
        TestUtils.setPrivateAttribute(memberEJB, AbstractEJB.class, getMockEntityManager(), "entityManager");
        TestUtils.setPrivateAttribute(memberEJB, AbstractEJB.class, getMockLogger(), "logger");

    }

    private List<Member> buildMemberList() {
        List<Member> mockList = Lists.newArrayListWithExpectedSize(1);

        Member newMember = EntityFactory.createMember();
        mockList.add(newMember);
        return mockList;
    }


    @Test
    public void testFindMembers() {

        List<Member> mockList = buildMemberList();
        when(getMockQuery().getResultList()).thenReturn(mockList);

        List<Member> memberList = memberEJB.findMembers();

        assertThat(memberList).isEqualTo(mockList);
        verify(getMockEntityManager()).createNamedQuery(eq("findMembers"));
    }

    @Test
    public void testFindByNameNull() {


        try {
            memberEJB.findMember(null, null);
            fail("UserNotFoundException expected");
        } catch (UserNotFoundException e) {

        }
        verify(getMockEntityManager()).createNamedQuery(eq("findByNamePassword"));

    }

    @Test
    public void testFindMember() throws UserNotFoundException {

        String name = "FOOONAME";
        String password = "PASSWORD";

        List<Member> mockList = buildMemberList();

        when(getMockQuery().getResultList()).thenReturn(mockList);

        assertThat(memberEJB.findMember(name, password)).isNotNull().isEqualTo(mockList.get(0));
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
