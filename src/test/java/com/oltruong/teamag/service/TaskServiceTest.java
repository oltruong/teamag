package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
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

    @Mock
    private WorkService mockWorkService;

    @Before
    public void init() {
        super.setup();
        taskService = new TaskService();
        prepareService(taskService);
        task = EntityFactory.createTask();
        TestUtils.setPrivateAttribute(taskService, mockWorkService, "workService");

    }

    @Test
    public void testFindAllTasks() {
        testFindTasks("Task.FIND_ALL", taskService::findAllTasks);

    }


    @Test
    public void testFindAllTasksWithActivity() {
        testFindTasks("Task.FIND_ALL_WITH_ACTIVITY", taskService::findTaskWithActivity);

    }

    private void testFindTasks(String namedQuery, Supplier<List<Task>> supplier) {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        when(mockTypedQuery.getResultList()).thenReturn(taskList);
        when(mockTypedQuery.getResultList()).thenReturn(taskList);

        List<Task> taskListReturned = supplier.get();

        assertThat(taskList).isEqualTo(taskListReturned);
        verify(mockEntityManager).createNamedQuery(eq(namedQuery), eq(Task.class));
    }

    @Test
    public void testFindTasksForMember() {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        when(mockTypedQuery.getResultList()).thenReturn(taskList);
        Member member = EntityFactory.createMember();
        member.setId(randomLong);

        List<Task> taskListReturned = taskService.findTasksForMember(member);

        assertThat(taskListReturned).isEqualTo(taskList);
        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_MEMBER"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("memberId"), eq(randomLong));
    }

    @Test
    public void testFindTasksForMember_empty() {
        Task absenceTask = EntityFactory.createTask();
        absenceTask.setId(randomLong);
        when(mockEntityManager.find(eq(Task.class), eq(1L))).thenReturn(absenceTask);
        Member member = EntityFactory.createMember();
        member.setId(randomLong);

        List<Task> taskListReturned = taskService.findTasksForMember(member);

        assertThat(taskListReturned).containsExactly(absenceTask);
        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_MEMBER"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("memberId"), eq(randomLong));
        verify(mockEntityManager).find(eq(Task.class), eq(1L));

    }


    @Test
    public void testFindAllNonAdminTasks() {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);
        when(mockTypedQuery.getResultList()).thenReturn(taskList);

        List<Task> taskListFound = taskService.findAllNonAdminTasks();


        assertThat(taskListFound).isEqualTo(taskList);

        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_NONTYPE"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("memberType"), Matchers.eq(MemberType.ADMINISTRATOR));
    }

    @Test
    public void testFindTask() {
        Task task = setupTask();

        Task taskFound = taskService.find(randomLong);

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

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteTask_null() {
        taskService.deleteTask(randomLong);
        verify(mockEntityManager).find(eq(Task.class), eq(randomLong));
        verify(mockEntityManager, never()).remove(any(Task.class));
    }


    @Test
    public void testUpdateTask() {

        task.setId(randomLong);
        taskService.merge(task);
        verify(mockEntityManager).merge(eq(task));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateTask_loop() {

        task.setId(randomLong);
        task.setTask(task);
        taskService.merge(task);
        verify(mockEntityManager, never()).merge(eq(task));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateTask_indirect_loop() {

        task.setId(randomLong);

        Task anotherTask = EntityFactory.createTask();
        anotherTask.setId(EntityFactory.createRandomLong());

        task.setTask(anotherTask);

        anotherTask.setTask(task);
        taskService.merge(task);
        verify(mockEntityManager, never()).merge(eq(task));
    }

    @Test
    public void testCreateTask() {
        task.setId(randomLong);
        taskService.createTask(task);
        verify(mockEntityManager).persist(eq(task));
        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_BY_NAME"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("fname"), eq(task.getName()));
        verify(mockTypedQuery).setParameter(eq("fproject"), eq(task.getProject()));

    }

    @Test(expected = EntityExistsException.class)
    public void testCreateTask_existing() {
        task.setId(randomLong);

        when(mockTypedQuery.getResultList()).thenReturn(Lists.newArrayList(task));
        taskService.createTask(task);
    }


    @Test
    public void testRemoveTask_anotherMember() {
        task.setId(randomLong);
        Member member = EntityFactory.createMember();
        Long randomId = EntityFactory.createRandomLong();

        member.setId(randomId);
        task.getMembers().get(0).setId(EntityFactory.createRandomLong());
        task.getMembers().add(member);

        DateTime month = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();

        when(mockEntityManager.find(eq(Task.class), any())).thenReturn(task);
        when(mockEntityManager.find(eq(Member.class), any())).thenReturn(member);


        testRemoveTask(member, randomId, month);

        verify(mockEntityManager).persist(eq(task));
        Assertions.assertThat(task.getMembers()).hasSize(1).doesNotContain(member);


    }

    @Test
    public void testRemoveTask_noMember() {
        testRemoveTask_noMember(1);
        verify(mockEntityManager).persist(eq(task));
        Assertions.assertThat(task.getMembers()).isEmpty();

    }

    @Test
    public void testRemoveTask_noMember_Empty() {
        testRemoveTask_noMember(0);
        verify(mockEntityManager).remove(eq(task));

    }


    public void testRemoveTask_noMember(int value) {
        task.setId(randomLong);
        Member member = EntityFactory.createMember();
        Long randomId = EntityFactory.createRandomLong();

        member.setId(randomId);
        task.getMembers().clear();
        task.getMembers().add(member);

        DateTime month = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();

        when(mockEntityManager.find(eq(Task.class), any())).thenReturn(task);
        when(mockEntityManager.find(eq(Member.class), any())).thenReturn(member);
        when(mockNamedQuery.getSingleResult()).thenReturn(Integer.valueOf(value));


        testRemoveTask(member, randomId, month);
        verify(mockEntityManager).createNamedQuery(eq("Work.COUNT_BY_TASK"));
        verify(mockNamedQuery).setParameter(eq("fTaskId"), eq(randomLong));
        verify(mockNamedQuery).executeUpdate();

    }


    private void testRemoveTask(Member member, Long randomId, DateTime month) {
        taskService.remove(task, member, month);
        verify(mockEntityManager).createNamedQuery(eq("Work.DELETE_BY_MEMBERTaskMonth"));
        verify(mockNamedQuery).setParameter(eq("fmemberId"), eq(randomId));
        verify(mockNamedQuery).setParameter(eq("ftaskId"), eq(randomLong));
        verify(mockNamedQuery).setParameter(eq("fmonth"), eq(month));
        verify(mockNamedQuery).executeUpdate();
    }


    @Test
    public void testCreateTask_member() {
        task.setId(randomLong);
        task.getMembers().clear();
        Member member = EntityFactory.createMember();

        DateTime month = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();

        taskService.createTask(month, member, task);

        assertThat(task.getId()).isNull();
        Assertions.assertThat(task.getMembers()).containsExactly(member);

        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_BY_NAME"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("fname"), eq(task.getName()));
        verify(mockTypedQuery).setParameter(eq("fproject"), eq(task.getProject()));

        verify(mockEntityManager).flush();

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(mockEntityManager).persist(taskCaptor.capture());
        List<DateTime> workingDayList = CalendarUtils.getWorkingDays(month);
        workingDayList.forEach(day -> verify(mockWorkService).createWork(eq(member), eq(month), taskCaptor.capture(), eq(day)));
        taskCaptor.getAllValues().forEach(myTask -> assertThat(myTask).isEqualToComparingFieldByField(task));
    }

    @Test
    public void testCreateTask_member_existing() {
        task.setId(randomLong);
        Member member = EntityFactory.createMember();
        Long randomId = EntityFactory.createRandomLong();

        member.setId(randomId);
        task.getMembers().clear();
        task.getMembers().add(member);

        DateTime month = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();

        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask, 1);
        final Task existingTask = taskList.get(0);
        existingTask.setId(EntityFactory.createRandomLong());

        when(mockTypedQuery.getResultList()).thenReturn(taskList);

        taskService.createTask(month, member, task);

        verify(mockEntityManager).createNamedQuery(eq("Task.FIND_BY_NAME"), eq(Task.class));
        verify(mockTypedQuery).setParameter(eq("fname"), eq(task.getName()));
        verify(mockTypedQuery).setParameter(eq("fproject"), eq(task.getProject()));

        verify(mockEntityManager).flush();
        verify(mockEntityManager).merge(eq(existingTask));


        Assertions.assertThat(existingTask.getMembers()).contains(member);
        List<DateTime> workingDayList = CalendarUtils.getWorkingDays(month);


        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        workingDayList.forEach(day -> verify(mockWorkService).createWork(eq(member), eq(month), taskCaptor.capture(), eq(day)));
        taskCaptor.getAllValues().forEach(myTask -> assertThat(myTask).isEqualToComparingFieldByField(existingTask));
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateTask_member_existing_exception() {
        task.setId(randomLong);
        Member member = EntityFactory.createMember();
        Long randomId = EntityFactory.createRandomLong();

        member.setId(randomId);
        task.getMembers().clear();
        task.getMembers().add(member);

        DateTime month = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();

        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask, 1);
        final Task existingTask = taskList.get(0);
        existingTask.setId(EntityFactory.createRandomLong());
        existingTask.addMember(member);

        when(mockTypedQuery.getResultList()).thenReturn(taskList);

        taskService.createTask(month, member, task);

    }

}
