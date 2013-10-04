package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;

@Stateless
public class MemberEJB extends AbstractEJB {

    // ======================================
    // = Public Methods =
    // ======================================

    @SuppressWarnings("unchecked")
    public List<Member> findMembers() {
        Query query = getEntityManager().createNamedQuery("findMembers");
        return query.getResultList();
    }

    public Member findByName(String name) {
        Query query = getEntityManager().createNamedQuery("findByName");
        query.setParameter("fname", name);
        @SuppressWarnings("unchecked")
        List<Member> liste = query.getResultList();
        if (CollectionUtils.isEmpty(liste)) {
            return null;
        } else {
            return liste.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public Member createMember(Member member) {

        // Adding default task
        Query query = getEntityManager().createNamedQuery("findTaskByName");
        query.setParameter("fname", "Absence");
        query.setParameter("fproject", "");

        Task task = null;
        List<Task> tasks = query.getResultList();

        if (tasks != null && !tasks.isEmpty()) {
            task = tasks.get(0);
        }

        if (task == null) {
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

}
