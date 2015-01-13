package fr.oltruong.teamag.service;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class TaskServiceTest extends AbstractServiceTest {

    private TaskService taskService;

    private Task task;


    @Before
    public void init() {
        super.setup();
        taskService = new TaskService();
        prepareService(taskService);
        task = EntityFactory.createTask();
    }

    @Test
    public void testFindAllTasks() {
        testFindTasks("Task.FIND_ALL", taskService::findAllTasks);

    }

    @Test
    public void testFindAllTasksWithActivity() {
        testFindTasks("findAllTasksWithActivity", taskService::findTaskWithActivity);

    }

    private void testFindTasks(String namedQuery, Supplier<List<Task>> supplier) {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        when(getMockQuery().getResultList()).thenReturn(taskList);
        when(mockTypedQuery.getResultList()).thenReturn(taskList);

        List<Task> taskListReturned = supplier.get();

        assertThat(taskList).isEqualTo(taskListReturned);
        verify(mockEntityManager).createNamedQuery(eq(namedQuery), eq(Task.class));
    }

    @Test
    public void testFindAllNonAdminTasks() {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        when(mockTypedQuery.getResultList()).thenReturn(taskList);

        List<Task> taskListFound = taskService.findAllNonAdminTasks();


        assertThat(taskListFound).isEqualTo(taskList);

        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_NONTYPE"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("memberType"), eq(MemberType.ADMINISTRATOR));
    }

    @Test
    public void testFindTask() {
        Task task = setupTask();

        Task taskFound = taskService.findTask(randomLong);

        assertThat(taskFound).isEqualTo(task);
        verify(mockEntityManager).find(eq(Task.class), eq(randomLong));
    }

    @Test
    public void testDeleteTask() {
        Task task = setupTask();

        taskService.deleteTask(randomLong);
        verify(mockEntityManager).find(eq(Task.class), eq(randomLong));
        verify(mockEntityManager).remove(eq(task));
    }

    private Task setupTask() {
        task.setId(randomLong);
        when(mockEntityManager.find(eq(Task.class), eq(randomLong))).thenReturn(task);
        return task;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTask_null() {
        taskService.deleteTask(randomLong);
        verify(mockEntityManager).find(eq(Task.class), eq(randomLong));
        verify(mockEntityManager, never()).remove(any(Task.class));
    }


    @Test
    public void testUpdateTask() {

        task.setId(randomLong);
        taskService.updateTask(task);
        verify(mockEntityManager).merge(eq(task));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateTask_loop() {

        task.setId(randomLong);
        task.setTask(task);
        taskService.updateTask(task);
        verify(mockEntityManager, never()).merge(eq(task));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateTask_indirect_loop() {

        task.setId(randomLong);

        Task anotherTask = EntityFactory.createTask();
        anotherTask.setId(EntityFactory.createRandomLong());

        task.setTask(anotherTask);

        anotherTask.setTask(task);
        taskService.updateTask(task);
        verify(mockEntityManager, never()).merge(eq(task));
    }

    @Test
    public void testCreateTask() throws ExistingDataException {
        task.setId(randomLong);
        taskService.createTask(task);
        verify(mockEntityManager).persist(eq(task));
        verify(mockEntityManager).createNamedQuery(eq("findTaskByName"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("fname"), eq(task.getName()));
        verify(mockTypedQuery).setParameter(eq("fproject"), eq(task.getProject()));

    }

    @Test(expected = ExistingDataException.class)
    public void testCreateTask_existing() throws ExistingDataException {
        task.setId(randomLong);

        when(mockTypedQuery.getResultList()).thenReturn(Lists.newArrayList(task));
        taskService.createTask(task);


    }

}
