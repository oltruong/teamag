package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oltruong.teamag.exception.UserNotFoundException;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.TeamagConstants;
import com.oltruong.teamag.utils.TeamagUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Singleton
@Startup
public class MemberService extends AbstractService<Member> {

    private static final String DEFAULT_ADMIN = "admin";

    private static List<Member> memberList;
    private static Map<Long, Member> memberMap;


    @Inject
    private WorkLoadService workLoadService;

    @Inject
    private BusinessCaseService businessCaseService;

    @Inject
    private TaskService taskService;


    public static Map<Long, Member> getMemberMap() {
        return memberMap;
    }

    public static Member getMember(Long id) {
        return memberMap.get(id);
    }

    public static List<Member> getMemberList() {
        return memberList;
    }


    @PostConstruct
    public void buildList() {
        memberList = findAll();

        if (memberList == null || memberList.isEmpty()) {
            initMemberList();
        }

        memberMap = Maps.newHashMapWithExpectedSize(memberList.size());
        memberList.forEach(member -> memberMap.put(member.getId(), member));
    }

    protected void initMemberList() {
        logger.info("Creating admin member.");

        Member adminMember = generateAdminMember();
        super.persist(adminMember);

        Task absenceTask = taskService.getOrCreateAbsenceTask();
        absenceTask.addMember(adminMember);
        taskService.merge(absenceTask);

        memberList = Lists.newArrayListWithExpectedSize(1);
        memberList.add(adminMember);
    }


    @Override
    public List<Member> findAll() {
        return getTypedQueryList("Member.FIND_ALL");
    }

    public List<Member> findActiveMembers() {
        return getTypedQueryList("findActiveMembers");
    }

    public List<Member> findActiveNonAdminMembers() {
        List<Member> activeMemberList = findActiveMembers();
        activeMemberList.removeIf(member -> member.isAdministrator());
        return activeMemberList;
    }


    public Member findMemberForAuthentication(String name, String password)
            throws UserNotFoundException {
        for (Member member : memberList) {
            if (member.getName().equalsIgnoreCase(name) && member.getPassword().equals(password)) {
                return member;
            }
        }
        throw new UserNotFoundException();
    }

    @Override
    Class<Member> entityProvider() {
        return Member.class;
    }

    @Override
    public Member persist(Member member) {

        member.setPassword(getDefaultPasswordHashed());
        member.setEstimatedWorkDays(0d);
        super.persist(member);

        Task absenceTask = taskService.getOrCreateAbsenceTask();
        absenceTask.addMember(member);
        taskService.merge(absenceTask);
        workLoadService.createFromMember(member, businessCaseService.findAll());

        buildList();
        return member;
    }


    @Override
    public void merge(Member member) {
        super.merge(member);
        buildList();
    }

    private Member generateAdminMember() {
        Member adminMember = new Member();
        adminMember.setName(DEFAULT_ADMIN);
        adminMember.setPassword(getDefaultPasswordHashed());
        adminMember.setCompany("ToBeDefined");
        adminMember.setEmail("tobedefined@email.com");
        adminMember.setMemberType(MemberType.ADMINISTRATOR);
        adminMember.setEstimatedWorkDays(0d);
        return adminMember;
    }

    private String getDefaultPasswordHashed() {
        return TeamagUtils.hashPassword(TeamagConstants.DEFAULT_PASSWORD);
    }

}
