package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskIT extends AbstractEntityIT {
    @Test
    public void testCreation() {
        Task task = EntityFactory.createTask();
        entityManager.persist(task.getMembers().get(0));
        entityManager.persist(task);

        transaction.commit();

        assertThat(task.getId()).isNotNull();

    }


    @Test
    public void testNamedQuery_findAllTasks() {

        Task task = EntityFactory.createTask();

        entityManager.persist(task.getMembers().get(0));
        entityManager.persist(task);

        transaction.commit();
        List<Task> listTasks = entityManager.createNamedQuery("Task.FIND_ALL", Task.class).getResultList();

        assertThat(listTasks).isNotNull().isNotEmpty();

        assertThat(listTasks.get(0).getMembers()).isNotNull().isNotEmpty();


    }

    @Test
    public void testNamedQuery_findByName() {

        String name = "myName" + Calendar.getInstance().getTimeInMillis();

        Task task1 = EntityFactory.createTask();
        Task task2 = EntityFactory.createTask();

        task2.setName(name);
        task1.setMembers(null);
        task2.setMembers(null);

        task2.setProject("");

        entityManager.persist(task1);
        entityManager.persist(task2);

        transaction.commit();

        TypedQuery<Task> query = entityManager.createNamedQuery("Task.FIND_BY_NAME", Task.class);
        query.setParameter("fname", name);
        query.setParameter("fproject", "");


        List<Task> listTasks = query.getResultList();

        assertThat(listTasks).isNotNull().isNotEmpty().hasSize(1).contains(task2);

        TypedQuery<Task> query2 = entityManager.createNamedQuery("Task.FIND_BY_NAME", Task.class);
        query2.setParameter("fname", task1.getName());
        query2.setParameter("fproject", task1.getProject());


        List<Task> listTasks2 = query2.getResultList();

        assertThat(listTasks2).isNotNull().isNotEmpty().hasSize(1).contains(task1);

    }

    @Test
    public void testNamedQuery_findMember() {


        Task task1 = EntityFactory.createTask();
        Task task2 = EntityFactory.createTask();

        entityManager.persist(task1.getMembers().get(0));
        entityManager.persist(task2.getMembers().get(0));

        entityManager.persist(task1);

        entityManager.persist(task2);

        transaction.commit();

        Long memberId = task2.getMembers().get(0).getId();

        List<Task> taskList = entityManager.createNamedQuery("Task.FIND_MEMBER", Task.class).setParameter("memberId", memberId).getResultList();

        assertThat(taskList).isNotEmpty().containsExactly(task2);

    }

    @Test
    public void testNamedQuery_findNonAdmin() {

        Task task1 = EntityFactory.createTask();
        Task task2 = EntityFactory.createTask();
        Task task3 = EntityFactory.createTask();
        task3.getMembers().get(0).setMemberType(MemberType.ADMINISTRATOR);

        entityManager.persist(task1.getMembers().get(0));
        entityManager.persist(task2.getMembers().get(0));
        entityManager.persist(task3.getMembers().get(0));

        entityManager.persist(task1);

        entityManager.persist(task2);
        entityManager.persist(task3);

        transaction.commit();
        List<Task> taskList = entityManager.createNamedQuery("Task.FIND_NONTYPE", Task.class).setParameter("memberType", MemberType.ADMINISTRATOR).getResultList();

        assertThat(taskList).isNotNull().containsExactly(task1, task2);
    }


}
