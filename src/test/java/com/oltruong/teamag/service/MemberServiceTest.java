package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.exception.UserNotFoundException;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class MemberServiceTest extends AbstractServiceTest {


    private MemberService memberService;

    @Mock
    private WorkLoadService mockWorkLoadService;

    private List<Member> testMemberList;

    @Before
    public void init() {
        memberService = new MemberService();
        prepareService(memberService);

        buildMemberList();
        when(mockTypedQuery.getResultList()).thenReturn(testMemberList);
        TestUtils.setPrivateAttribute(memberService, mockWorkLoadService, "workLoadService");

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
        when(mockTypedQuery.getResultList()).thenReturn(null);
        memberService.buildList();

        List<Member> memberList = MemberService.getMemberList();
        assertThat(memberList).isNotNull().hasSize(1);
        Member member = memberList.get(0);
        assertThat(member.isAdministrator()).isTrue();
        assertThat(MemberService.getMemberMap()).isNotNull().hasSize(1);
    }

    @Test
    public void testBuild() {
        memberService.buildList();
        assertThat(MemberService.getMemberList()).isEqualTo(testMemberList).hasSize(MemberService.getMemberMap().size());
        testMemberList.forEach(member -> assertThat(MemberService.getMemberMap().get(member.getId())).isEqualTo(member));
    }


    @Test
    public void testFindActiveMembers() {

        List<Member> memberList = memberService.findActiveMembers();

        assertThat(memberList).isEqualTo(testMemberList);
        verify(mockEntityManager).createNamedQuery(eq("findActiveMembers"), eq(Member.class));
    }


    @Test
    public void testFindMembers() {

        List<Member> memberList = memberService.findMembers();

        assertThat(memberList).isEqualTo(testMemberList);
        verify(mockEntityManager).createNamedQuery(eq("findMembers"), eq(Member.class));
    }

    @Test
    public void testFindMember() {
        Member newMember = EntityFactory.createMember();
        Long id = Long.valueOf(365l);

        when(mockEntityManager.find(eq(Member.class), any(Object.class))).thenReturn(newMember);

        Member memberFound = memberService.findMember(id);

        assertThat(memberFound).isEqualTo(newMember);
        verify(mockEntityManager).find(eq(Member.class), eq(id));

    }


    @Test
    public void testFindActiveNonAdminMembers() {

        int numberAdminMembers = 3;
        for (int i = 0; i < numberAdminMembers; i++) {
            Member member = testMemberList.get(i);
            member.setMemberType(MemberType.ADMINISTRATOR);
        }

        int numberAdminMember = testMemberList.size() - numberAdminMembers;
        List<Member> memberList = memberService.findActiveNonAdminMembers();

        assertThat(memberList.size()).isEqualTo(numberAdminMember);

        memberList.forEach(member -> assertThat(!member.isAdministrator()));

    }

    @Test
    public void testFindMemberForAuthentication() throws UserNotFoundException {

        String name = "FOOONAME";
        String password = "PASSWORD";

        Member member = new Member();
        member.setName(name);
        member.setPassword(password);
        testMemberList.clear();
        testMemberList.add(member);

        TestUtils.setPrivateAttribute(memberService, testMemberList, "memberList");

        assertThat(memberService.findMemberForAuthentication(name, password)).isNotNull().isEqualTo(testMemberList.get(0));
        verify(mockEntityManager, never()).createNamedQuery(eq("findByNamePassword"), eq(Member.class));

    }

    @Test(expected = UserNotFoundException.class)
    public void testFindMemberForAuthentication_null() throws UserNotFoundException {

        String name = "FOOONAME";
        String password = "PASSWORD";
        Member member = new Member();
        member.setName(name);
        member.setPassword(password + "2");
        testMemberList.clear();
        testMemberList.add(member);

        TestUtils.setPrivateAttribute(memberService, testMemberList, "memberList");
        memberService.findMemberForAuthentication(name, password);
    }

    @Test
    public void testCreateMemberWithAbsenceTask() {

        List<Task> taskList = buildEmptyTaskList();
        Task task = new Task();
        taskList.add(task);

        testCreateMember(taskList);
        verify(mockEntityManager).persist(refEq(task));

    }

    @Test
    public void testCreateMemberWithoutAbsenceTask() {

        List<Task> taskList = buildEmptyTaskList();
        testCreateMember(taskList);
        verify(mockEntityManager, times(2)).persist(isA(Task.class));
    }

    private void testCreateMember(List<Task> taskList) {


        TypedQuery<Task> mockQueryTask = mock(TypedQuery.class);
        when(mockEntityManager.createNamedQuery(eq("Task.FIND_BY_NAME"), eq(Task.class))).thenReturn(mockQueryTask);
        when(mockQueryTask.getResultList()).thenReturn(taskList);

        Member member = EntityFactory.createMember();
        Member memberCreated = memberService.create(member);

        assertThat(memberCreated).isEqualTo(member);
        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_BY_NAME"), eq(Task.class));
        verify(mockQueryTask).setParameter(eq("fname"), isA(String.class));
        verify(mockQueryTask).setParameter(eq("fproject"), isA(String.class));


        verify(mockEntityManager).persist(eq(member));
        verify(mockWorkLoadService).createFromMember(eq(member));
    }


    @Test
    public void testUpdateMember() {
        Member member = EntityFactory.createMember();
        memberService.updateMember(member);

        verify(mockEntityManager).merge(eq(member));
    }


    private List<Task> buildEmptyTaskList() {

        return Lists.newArrayListWithCapacity(1);

    }

}
