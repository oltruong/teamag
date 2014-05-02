package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.EntityFactory;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.enumeration.MemberType;
import fr.oltruong.teamag.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        testMemberList = EntityFactory.createList(EntityFactory::createMember);

        Long counter = Long.valueOf(1l);
        for (Member member : testMemberList) {
            member.setId(counter);
            counter++;
        }
    }

    @Test
    public void testBuild_empty() {
        when(getMockQuery().getResultList()).thenReturn(null);
        memberEJB.build();

        assertThat(MemberEJB.getMemberList()).isNotNull().isEmpty();
        assertThat(MemberEJB.getMemberMap()).isNotNull().isEmpty();
    }

    @Test
    public void testBuild() {
        memberEJB.build();
        assertThat(MemberEJB.getMemberList()).isEqualTo(testMemberList).hasSize(MemberEJB.getMemberMap().size());
        testMemberList.forEach(member -> assertThat(MemberEJB.getMemberMap().get(member.getId())).isEqualTo(member));
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

        int numberAdminMember = testMemberList.size() - numberAdminMembers;
        List<Member> memberList = memberEJB.findActiveNonAdminMembers();

        assertThat(memberList.size()).isEqualTo(numberAdminMember);

        memberList.forEach(member -> assertThat(!member.isAdministrator()));

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
        Task task = new Task();
        taskList.add(task);


        testCreateMember(taskList);
        verify(getMockEntityManager()).persist(refEq(task));

    }

    @Test
    public void testCreateMemberWithoutAbsenceTask() {

        List<Task> taskList = buildEmptyTaskList();
        testCreateMember(taskList);
        verify(getMockEntityManager(), times(2)).persist(isA(Task.class));

    }

    private void testCreateMember(List<Task> taskList) {
        //   when(getMockQuery().getResultList()).thenReturn(taskList);

        Query mockQueryTask = mock(Query.class);
        when(getMockEntityManager().createNamedQuery(eq("findTaskByName"))).thenReturn(mockQueryTask);
        when(mockQueryTask.getResultList()).thenReturn(taskList);

        Member member = EntityFactory.createMember();
        Member memberCreated = memberEJB.createMemberWithAbsenceTask(member);

        assertThat(memberCreated).isEqualTo(member);
        verify(getMockEntityManager()).createNamedQuery(eq("findTaskByName"));
        verify(mockQueryTask).setParameter(eq("fname"), isA(String.class));
        verify(mockQueryTask).setParameter(eq("fproject"), isA(String.class));


        verify(getMockEntityManager()).persist(eq(member));
    }


    @Test
    public void testUpdateMember() {
        Member member = EntityFactory.createMember();
        memberEJB.updateMember(member);

        verify(getMockEntityManager()).merge(eq(member));
    }


    private List<Task> buildEmptyTaskList() {

        return Lists.newArrayListWithCapacity(1);

    }

}
