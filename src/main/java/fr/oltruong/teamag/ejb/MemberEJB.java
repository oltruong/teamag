package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.enumeration.MemberType;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.utils.TeamagUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.Query;
import java.util.List;

@Singleton
@Startup
public class MemberEJB extends AbstractEJB {


    private static List<Member> memberList;


    public static List<Member> getMemberList() {
        return memberList;
    }

    @PostConstruct
    public void build() {
        memberList = findMembers();

    }

    @SuppressWarnings("unchecked")
    public List<Member> findMembers() {
        Query query = getEntityManager().createNamedQuery("findMembers");
        return query.getResultList();
    }

    public List<Member> findActiveMembers() {
        Query query = getEntityManager().createNamedQuery("findActiveMembers");
        return query.getResultList();
    }

    public List<Member> findActiveNonAdminMembers() {
        List<Member> memberList = findActiveMembers();
        List<Member> nonAdminMemberList = Lists.newArrayListWithExpectedSize(memberList.size());

        for (Member member : memberList) {
            if (!member.isAdministrator()) {
                nonAdminMemberList.add(member);
            }
        }
        return nonAdminMemberList;
    }


    public Member findMember(Long id) {
        return getEntityManager().find(Member.class, id);
    }

    public Member findMemberForAuthentication(String name, String password)
            throws UserNotFoundException {
        checkMembersNotEmpty();

        Query query = getEntityManager().createNamedQuery("findByNamePassword");
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
        Query query = getEntityManager().createNamedQuery("findTaskByName");
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
            getEntityManager().persist(newTask);
            task = newTask;
        }

        member.setPassword(TeamagUtils.hashPassword(""));
        member.setEstimatedWorkDays(0d);
        getEntityManager().persist(member);

        task.addMember(member);
        getEntityManager().persist(task);

        return member;
    }

    public void updateMember(Member member) {
        getEntityManager().merge(member);
    }


    private void checkMembersNotEmpty() {

        if (findMembers().isEmpty()) {
            getLogger().info("No member so far. Default admin will be created");

            Member adminMember = generateAdminMember();
            getEntityManager().persist(adminMember);

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
        return adminMember;
    }

    //TODO enable this feature
/*    public void removeMember(Member selectedMember) {

        Query query = getEntityManager().createNamedQuery("deleteWorksByMember");
        query.setParameter("fmemberId", selectedMember.getId());
        int rowsNumberDeleted = query.executeUpdate();
        getLogger().debug("Works deleted : " + rowsNumberDeleted);


        getEntityManager().remove(selectedMember);
    }*/
}
