package com.oltruong.teamag.service;

import com.oltruong.teamag.exception.UserNotFoundException;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class MemberServiceTest extends AbstractServiceTest {


    private MemberService memberService;

    @Mock
    private WorkLoadService mockWorkLoadService;

    @Mock
    private BusinessCaseService mockBusinessCaseService;

    @Mock
    private TaskService mockTaskService;

    private List<Member> testMemberList;

    private Task absenceTask;

    @Before
    public void init() {
        memberService = new MemberService();
        prepareService(memberService);

        buildMemberList();
        when(mockTypedQuery.getResultList()).thenReturn(testMemberList);
        TestUtils.setPrivateAttribute(memberService, mockWorkLoadService, "workLoadService");
        TestUtils.setPrivateAttribute(memberService, mockTaskService, "taskService");
        TestUtils.setPrivateAttribute(memberService, mockBusinessCaseService, "businessCaseService");

        absenceTask = new Task();
        when(mockTaskService.getOrCreateAbsenceTask()).thenReturn(absenceTask);


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
    public void findActiveMembers() {

        List<Member> memberList = memberService.findActiveMembers();

        assertThat(memberList).isEqualTo(testMemberList);
        verify(mockEntityManager).createNamedQuery(eq("findActiveMembers"), eq(Member.class));
    }


    @Test
    public void findAll() {

        List<Member> memberList = memberService.findAll();

        assertThat(memberList).isEqualTo(testMemberList);
        verify(mockEntityManager).createNamedQuery(eq("Member.FIND_ALL"), eq(Member.class));
    }

    @Test
    public void findMember() {
        Member newMember = EntityFactory.createMember();
        Long id = Long.valueOf(365l);

        when(mockEntityManager.find(eq(Member.class), any(Object.class))).thenReturn(newMember);

        Member memberFound = memberService.find(id);

        assertThat(memberFound).isEqualTo(newMember);
        verify(mockEntityManager).find(eq(Member.class), eq(id));

    }


    @Test
    public void findActiveNonAdminMembers() {

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
    public void findMemberForAuthentication() throws UserNotFoundException {

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
    public void findMemberForAuthenticationNull() throws UserNotFoundException {

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
    public void createMember() {

        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);

        when(mockBusinessCaseService.findAll()).thenReturn(businessCaseList);

        Member member = EntityFactory.createMember();
        Member memberCreated = memberService.persist(member);

        assertThat(memberCreated).isEqualTo(member);


        verify(mockEntityManager).persist(eq(member));
        verify(mockWorkLoadService).createFromMember(eq(member), eq(businessCaseList));
        verify(mockTaskService).getOrCreateAbsenceTask();
        verify(mockTaskService).merge(refEq(absenceTask));
        verify(mockBusinessCaseService).findAll();
    }


    @Test
    public void testUpdateMember() {
        Member member = EntityFactory.createMember();
        memberService.merge(member);

        verify(mockEntityManager).merge(eq(member));
    }


}
