package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.enumeration.MemberType;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.utils.TeamagUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Singleton
@Startup
public class MemberService extends AbstractService {


    private static List<Member> memberList;
    private static Map<Long, Member> memberMap;

    public static Map<Long, Member> getMemberMap() {
        return memberMap;
    }

    public static List<Member> getMemberList() {
        return memberList;
    }

    @PostConstruct
    public void build() {
        memberList = findMembers();

        if (memberList != null) {
            memberMap = Maps.newHashMapWithExpectedSize(memberList.size());
            memberList.forEach(member -> memberMap.put(member.getId(), member));
        } else {
            memberList = Lists.newArrayListWithExpectedSize(0);
            memberMap = Maps.newHashMapWithExpectedSize(0);
        }

    }

    @SuppressWarnings("unchecked")
    public List<Member> findMembers() {
        return getNamedQueryList("findMembers");
    }

    public List<Member> findActiveMembers() {
        return getNamedQueryList("findActiveMembers");
    }

    public List<Member> findActiveNonAdminMembers() {
        List<Member> memberList = findActiveMembers();
        memberList.removeIf(member -> member.isAdministrator());

        return memberList;
    }


    public Member findMember(Long id) {
        return find(Member.class, id);
    }

    public Member findMemberForAuthentication(String name, String password)
            throws UserNotFoundException {
        checkMembersNotEmpty();

        Query query = createNamedQuery("findByNamePassword");
        query.setParameter("fname", name);
        query.setParameter("fpassword", password);
        @SuppressWarnings("unchecked")
        List<Member> memberList = query.getResultList();
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        } else {
            throw new UserNotFoundException();
        }
    }

    @SuppressWarnings("unchecked")
    public Member createMemberWithAbsenceTask(Member member) {

        // Adding default task
        Query query = createNamedQuery("findTaskByName");
        query.setParameter("fname", "Absence");
        query.setParameter("fproject", "");

        Task task = null;
        List<Task> tasklist = query.getResultList();

        if (!CollectionUtils.isEmpty(tasklist)) {
            task = tasklist.get(0);
        } else {
            getLogger().info("Task is not found. Will be created");
            Task newTask = new Task();
            newTask.setName("Absence");
            persist(newTask);
            task = newTask;
        }

        member.setPassword(TeamagUtils.hashPassword(""));
        member.setEstimatedWorkDays(0d);
        persist(member);

        task.addMember(member);
        persist(task);
        build();
        return member;
    }

    public void updateMember(Member member) {
        merge(member);
        build();
    }


    private void checkMembersNotEmpty() {

        if (findMembers().isEmpty()) {
            getLogger().warn("No member so far. Default admin will be created");

            Member adminMember = generateAdminMember();
            persist(adminMember);

        }
    }

    private Member generateAdminMember() {
        String defaultValue = "admin";

        Member adminMember = new Member();
        adminMember.setName(defaultValue);
        adminMember.setPassword(TeamagUtils.hashPassword(defaultValue));
        adminMember.setCompany("ToBeDefined");
        adminMember.setEmail("tobedefined@email.com");
        adminMember.setMemberType(MemberType.ADMINISTRATOR);
        adminMember.setEstimatedWorkDays(Double.valueOf(0d));
        return adminMember;
    }

    //TODO enable this feature
/*    public void removeMember(Member selectedMember) {

        Query query = createNamedQuery("deleteWorksByMember");
        query.setParameter("fmemberId", selectedMember.getId());
        int rowsNumberDeleted = query.executeUpdate();
        getLogger().debug("Works deleted : " + rowsNumberDeleted);


        getEntityManager().remove(selectedMember);
    }*/
}
