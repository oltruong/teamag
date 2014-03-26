package fr.oltruong.teamag.entity;

import org.junit.Test;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskIT extends AbstractEntityIT {
    @Test
    public void testCreation() {
        Task task = createTask();
        getEntityManager().persist(task.getMembers().get(0));
        getEntityManager().persist(task);

        getTransaction().commit();

        assertThat(task.getId()).isNotNull();

    }

    @Test
    public void testNamedQueryFindAllTasks() {

        Task task = createTask();

        getEntityManager().persist(task.getMembers().get(0));
        getEntityManager().persist(task);

        getTransaction().commit();
        @SuppressWarnings("unchecked")
        List<Task> listTasks = getEntityManager().createNamedQuery("findAllTasks").getResultList();

        assertThat(listTasks).isNotNull().isNotEmpty();

        assertThat(listTasks.get(0).getMembers()).isNotNull().isNotEmpty();


    }

    @Test
    public void testNamedQueryFindByName() {

        String name = "myName" + Calendar.getInstance().getTimeInMillis();

        Task task1 = createTask();
        Task task2 = createTask();

        task2.setName(name);
        task1.setMembers(null);
        task2.setMembers(null);

        task2.setProject("");


        getEntityManager().persist(task1);
        getEntityManager().persist(task2);

        getTransaction().commit();

        Query query = getEntityManager().createNamedQuery("findTaskByName");
        query.setParameter("fname", name);
        query.setParameter("fproject", "");

        @SuppressWarnings("unchecked")
        List<Task> listTasks = query.getResultList();

        assertThat(listTasks).isNotNull().isNotEmpty().hasSize(1).contains(task2);

        Query query2 = getEntityManager().createNamedQuery("findTaskByName");
        query2.setParameter("fname", task1.getName());
        query2.setParameter("fproject", task1.getProject());

        @SuppressWarnings("unchecked")
        List<Task> listTasks2 = query2.getResultList();

        assertThat(listTasks2).isNotNull().isNotEmpty().hasSize(1).contains(task1);

    }

    private Task createTask() {
        Task task = new Task();

        task.setName("createTask" + Calendar.getInstance().getTimeInMillis());
        task.setProject("my project");

        Member myMember = new Member();
        myMember.setName("Bob" + Calendar.getInstance().getTimeInMillis());
        myMember.setCompany("my Company");
        myMember.setEmail("email@dummy.com");
        myMember.setEstimatedWorkDays(0d);
        task.addMember(myMember);

        return task;
    }
}
