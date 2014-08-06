package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.builder.EntityFactory;
import org.junit.Test;

import javax.persistence.Query;
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
    public void testNamedQueryFindAllTasks() {

        Task task = EntityFactory.createTask();

        entityManager.persist(task.getMembers().get(0));
        entityManager.persist(task);

        transaction.commit();
        @SuppressWarnings("unchecked")
        List<Task> listTasks = entityManager.createNamedQuery("findAllTasks").getResultList();

        assertThat(listTasks).isNotNull().isNotEmpty();

        assertThat(listTasks.get(0).getMembers()).isNotNull().isNotEmpty();


    }

    @Test
    public void testNamedQueryFindByName() {

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

        Query query = entityManager.createNamedQuery("findTaskByName");
        query.setParameter("fname", name);
        query.setParameter("fproject", "");

        @SuppressWarnings("unchecked")
        List<Task> listTasks = query.getResultList();

        assertThat(listTasks).isNotNull().isNotEmpty().hasSize(1).contains(task2);

        Query query2 = entityManager.createNamedQuery("findTaskByName");
        query2.setParameter("fname", task1.getName());
        query2.setParameter("fproject", task1.getProject());

        @SuppressWarnings("unchecked")
        List<Task> listTasks2 = query2.getResultList();

        assertThat(listTasks2).isNotNull().isNotEmpty().hasSize(1).contains(task1);

    }


}
