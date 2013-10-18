package fr.oltruong.teamag.ejb;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;

public class MemberEJBTest extends AbstractEJBTest {

    private MemberEJB buildMemberEJB() {
        MemberEJB memberEJB = new MemberEJB();
        memberEJB.setEntityManager(getMockEntityManager());

        memberEJB.setLogger(getMockLogger());
        return memberEJB;
    }

    private List<Member> buildMemberList() {
        List<Member> mockList = Lists.newArrayListWithExpectedSize(1);

        Member newMember = buildMember();
        mockList.add(newMember);
        return mockList;
    }

    private Member buildMember() {
        Member newMember = new Member();
        newMember.setId(Long.valueOf(1l));
        return newMember;
    }

    @Test
    public void testFindMembers() {
        MemberEJB memberEJB = buildMemberEJB();
        List<Member> mockList = buildMemberList();
        when(getMockQuery().getResultList()).thenReturn(mockList);

        List<Member> memberList = memberEJB.findMembers();

        assertThat(memberList).isEqualTo(mockList);
        verify(getMockEntityManager()).createNamedQuery(eq("findMembers"));

    }

    @Test
    public void testFindByNameNull() {

        MemberEJB memberEJB = buildMemberEJB();

        assertThat(memberEJB.findByName(null)).isNull();
        verify(getMockEntityManager()).createNamedQuery(eq("findByName"));

    }

    @Test
    public void testFindByName() {

        String name = "FOOONAME";
        MemberEJB memberEJB = buildMemberEJB();

        List<Member> mockList = buildMemberList();

        when(getMockQuery().getResultList()).thenReturn(mockList);

        assertThat(memberEJB.findByName(name)).isNotNull().isEqualTo(mockList.get(0));
        verify(getMockEntityManager()).createNamedQuery(eq("findByName"));
        verify(getMockQuery()).setParameter("fname", name);

    }

    @Test
    public void testCreateMemberWithAbsenceTask() {
        MemberEJB memberEJB = buildMemberEJB();
        List<Task> taskList = buildEmptyTaskList();
        when(getMockQuery().getResultList()).thenReturn(taskList);

        Member member = buildMember();
        Member memberCreated = memberEJB.createMemberWithAbsenceTask(member);

        assertThat(memberCreated).isEqualTo(member);
        verify(getMockEntityManager()).createNamedQuery(eq("findTaskByName"));
        verify(getMockQuery()).setParameter(eq("fname"), isA(String.class));
        verify(getMockQuery()).setParameter(eq("fproject"), isA(String.class));

        verify(getMockEntityManager(), times(2)).persist(isA(Task.class));

        verify(getMockEntityManager()).persist(eq(member));

    }

    private List<Task> buildEmptyTaskList() {

        List<Task> taskList = Lists.newArrayListWithCapacity(1);
        return taskList;
    }

}
