package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.TaskWebBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskEndPointTest extends AbstractEndPointTest {

    @Mock
    private TaskService mockTaskService;

    private TaskEndPoint taskEndPoint;

    private Task task;

    @Before
    public void prepare() {
        super.setup();

        taskEndPoint = new TaskEndPoint();
        TestUtils.setPrivateAttribute(taskEndPoint, mockTaskService, "taskService");
        task = EntityFactory.createTask();


    }


    @Test
    public void testGetTasks() throws Exception {
        testFindTasks(mockTaskService::findAll, taskEndPoint::getAll);
        verify(mockTaskService, atLeastOnce()).findAll();
    }

    @Test
    public void testGetTasksWithActivity() throws Exception {
        testFindTasks(mockTaskService::findTaskWithActivity, taskEndPoint::getTasksWithActivity);
        verify(mockTaskService, atLeastOnce()).findTaskWithActivity();

    }


    private void testFindTasks(Supplier<List<Task>> taskListSupplier, Supplier<Response> responseSupplier) {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);


        Task parentTask = EntityFactory.createTask();
        parentTask.setId(EntityFactory.createRandomLong());


        taskList.forEach(task -> task.setTask(parentTask));
        when(taskListSupplier.get()).thenReturn(taskList);

        Response response = responseSupplier.get();

        checkResponseOK(response);
        List<TaskWebBean> taskListReturned = (List<TaskWebBean>) response.getEntity();

        assertThat(taskListReturned).hasSameSizeAs(taskList);


    }


    @Test
    public void testGet() throws Exception {


        when(mockTaskService.find(any())).thenReturn(task);

        Response response = taskEndPoint.getSingle(randomId);
        checkResponseOK(response);

        Task taskReturned = (Task) response.getEntity();


        assertThat(taskReturned).isEqualToIgnoringNullFields(task);
        verify(mockTaskService).find(eq(randomId));
    }

    @Test
    public void testCreate() throws Exception {
        task.setId(randomId);

        Response response = taskEndPoint.create(task);

        checkResponseCreated(response);
        verify(mockTaskService).persist(eq(task));

    }


    @Test
    public void testCreate_existing() throws Exception {

        task.setId(randomId);
        doThrow(new EntityExistsException()).when(mockTaskService).persist(eq(task));

        Response response = taskEndPoint.create(task);

        checkResponseNotAcceptable(response);
        verify(mockTaskService).persist(eq(task));

    }

    @Test
    public void testUpdateTask() throws Exception {
        Task task = EntityFactory.createTask();
        assertThat(task.getId()).isNull();

        Response response = taskEndPoint.updateTask(randomId, task);
        checkResponseOK(response);

        assertThat(task.getId()).isEqualTo(randomId);
        verify(mockTaskService).merge(eq(task));
    }

    @Test
    public void testDelete() throws Exception {
        Response response = taskEndPoint.delete(randomId);
        checkResponseNoContent(response);
        verify(mockTaskService).remove(eq(randomId));
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(mockTaskService).remove(anyLong());
        Response response = taskEndPoint.delete(randomId);
        checkResponseNotFound(response);
        verify(mockTaskService).remove(eq(randomId));
    }
}