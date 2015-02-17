package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.enumeration.MemberType;
import fr.oltruong.teamag.utils.TeamagConstants;
import fr.oltruong.teamag.utils.TeamagUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Singleton
@Startup
public class MemberService extends AbstractService {

    private static final String DEFAULT_ADMIN = "admin";

    private static List<Member> memberList;
    private static Map<Long, Member> memberMap;


    @Inject
    private WorkLoadService workLoadService;

    public static Map<Long, Member> getMemberMap() {
        return memberMap;
    }

    public static List<Member> getMemberList() {
        return memberList;
    }


    @PostConstruct
    public void buildList() {
        memberList = findMembers();

        if (memberList == null || memberList.isEmpty()) {
            initMemberList();
        }

        memberMap = Maps.newHashMapWithExpectedSize(memberList.size());
        memberList.forEach(member -> memberMap.put(member.getId(), member));
    }

    protected void initMemberList() {
        logger.info("Creating admin member.");

        Member adminMember = generateAdminMember();
        persist(adminMember);

        Task absenceTask = getOrCreateAbsenceTask();
        absenceTask.addMember(adminMember);
        persist(absenceTask);

        memberList = Lists.newArrayListWithExpectedSize(1);
        memberList.add(adminMember);
    }


    public List<Member> findMembers() {
        return createNamedQuery("findMembers", Member.class).getResultList();
    }

    public List<Member> findActiveMembers() {
        return createNamedQuery("findActiveMembers", Member.class).getResultList();
    }

    public List<Member> findActiveNonAdminMembers() {
        List<Member> activeMemberList = findActiveMembers();
        activeMemberList.removeIf(member -> member.isAdministrator());
        return activeMemberList;
    }


    public Member findMember(Long id) {
        return find(Member.class, id);
    }

    public Member findMemberForAuthentication(String name, String password)
            throws UserNotFoundException {
        for (Member member : memberList) {
            if (member.getName().equals(name) && member.getPassword().equals(password)) {
                return member;
            }
        }
        throw new UserNotFoundException();
    }

    public Member create(Member member) {

        member.setPassword(getDefaultPasswordHashed());
        member.setEstimatedWorkDays(0d);
        persist(member);

        Task absenceTask = getOrCreateAbsenceTask();
        absenceTask.addMember(member);
        persist(absenceTask);

        workLoadService.createFromMember(member);

        buildList();
        return member;
    }

    private Task getOrCreateAbsenceTask() {
        TypedQuery<Task> query = createNamedQuery("Task.FIND_BY_NAME", Task.class);
        query.setParameter("fname", "Absence");
        query.setParameter("fproject", "");

        Task task;
        List<Task> taskList = query.getResultList();

        if (!CollectionUtils.isEmpty(taskList)) {
            task = taskList.get(0);
        } else {
            logger.info("Absence task is not found. Will be created");
            Task newTask = new Task();
            newTask.setName("Absence");
            persist(newTask);
            task = newTask;
        }
        return task;
    }

    public void updateMember(Member member) {
        merge(member);
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
