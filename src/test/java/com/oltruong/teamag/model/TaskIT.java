package com.oltruong.teamag.model;

import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.model.builder.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskIT extends AbstractEntityIT {
    @Test
    public void creation() {
        Task task = EntityFactory.createTask();
        entityManager.persist(task.getMembers().get(0));
        entityManager.persist(task);

        transaction.commit();

        assertThat(task.getId()).isNotNull();

    }


    @Test
    public void namedQueryFindAllTasks() {

        Task task = EntityFactory.createTask();

        entityManager.persist(task.getMembers().get(0));
        entityManager.persist(task);

        transaction.commit();
        List<Task> listTasks = entityManager.createNamedQuery("Task.FIND_ALL", Task.class).getResultList();

        Assertions.assertThat(listTasks).isNotNull().isNotEmpty();

        assertThat(listTasks.get(0).getMembers()).isNotNull().isNotEmpty();


    }

    @Test
    public void namedQueryFindByName() {

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

        Assertions.assertThat(listTasks).isNotNull().isNotEmpty().hasSize(1).contains(task2);

        TypedQuery<Task> query2 = entityManager.createNamedQuery("Task.FIND_BY_NAME", Task.class);
        query2.setParameter("fname", task1.getName());
        query2.setParameter("fproject", task1.getProject());


        List<Task> listTasks2 = query2.getResultList();

        Assertions.assertThat(listTasks2).isNotNull().isNotEmpty().hasSize(1).contains(task1);

    }

    @Test
    public void namedQueryFindMember() {


        Task task1 = EntityFactory.createTask();
        Task task2 = EntityFactory.createTask();

        entityManager.persist(task1.getMembers().get(0));
        entityManager.persist(task2.getMembers().get(0));

        entityManager.persist(task1);

        entityManager.persist(task2);

        transaction.commit();

        Long memberId = task2.getMembers().get(0).getId();

        List<Task> taskList = entityManager.createNamedQuery("Task.FIND_MEMBER", Task.class).setParameter("memberId", memberId).getResultList();

        Assertions.assertThat(taskList).isNotEmpty().containsExactly(task2);

    }

    @Test
    public void namedQueryFindNonAdmin() {

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

        Assertions.assertThat(taskList).isNotNull().containsExactly(task1, task2);
    }


}
