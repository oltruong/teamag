package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.model.enumeration.MemberType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class WorkServiceTest extends AbstractServiceTest {


    private WorkService workService;


    @Before
    public void init() {
        workService = new WorkService();
        prepareService(workService);

    }

    @Test
    public void testFindAllTasks() {
        testFindTask("findAllTasks", workService::findAllTasks);

    }

    @Test
    public void testFindAllTasksWithActivity() {
        testFindTask("findAllTasksWithActivity", workService::findTaskWithActivity);

    }

    private void testFindTask(String namedQuery, Supplier<List<Task>> supplier) {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        when(getMockQuery().getResultList()).thenReturn(taskList);

        List<Task> taskListFound = supplier.get();

        assertThat(taskListFound).isEqualTo(taskList);
        verify(getMockEntityManager()).createNamedQuery(eq(namedQuery));

    }


    @Test
    public void testFindAllNonAdminTasks() {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);

        for (int i = 0; i < 2; i++) {
            taskList.get(i).getMembers().get(0).setMemberType(MemberType.ADMINISTRATOR);
        }

        when(getMockQuery().getResultList()).thenReturn(taskList);

        List<Task> taskListFound = workService.findAllNonAdminTasks();


        assertThat(taskListFound).hasSize(taskList.size() - 2);

        taskListFound.forEach(task -> assertThat(task.isAdmin()).isFalse());
        verify(getMockEntityManager()).createNamedQuery(eq("findAllTasks"));
    }
}
