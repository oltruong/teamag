package fr.oltruong.teamag.ejb;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.MemberType;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.exception.UserNotFoundException;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class MemberEJB extends AbstractEJB {

    public void checkMembersNotEmpty() {

        if (findMembers().isEmpty()) {
            getLogger().info("No member so far. Default admin will be created");

            Member adminMember = generateAdminMember();
            getEntityManager().persist(adminMember);

        }
    }

    @SuppressWarnings("unchecked")
    public List<Member> findMembers() {
        Query query = getEntityManager().createNamedQuery("findMembers");
        return query.getResultList();
    }

    public Member findByName(String name) throws UserNotFoundException {
        checkMembersNotEmpty();

        Query query = getEntityManager().createNamedQuery("findByName");
        query.setParameter("fname", name);
        @SuppressWarnings("unchecked")
        List<Member> liste = query.getResultList();
        if (!CollectionUtils.isEmpty(liste)) {
            return liste.get(0);
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
        }  else {
            getLogger().info("Task is not found. Will be created");
            Task newTask = new Task();
            newTask.setName("Absence");
            getEntityManager().persist(newTask);
            task = newTask;
        }

        getEntityManager().persist(member);

        task.addMember(member);
        getEntityManager().persist(task);

        return member;
    }

    private Member generateAdminMember() {
        Member adminMember = new Member();
        adminMember.setName("admin");
        adminMember.setCompany("ToBeDefined");
        adminMember.setEmail("tobedefined@email.com");
        adminMember.setMemberType(MemberType.ADMINISTRATOR);
        return adminMember;
    }

}
