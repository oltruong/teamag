package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.transformer.TaskWebBeanTransformer;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.TaskWebBean;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskEndPointTest extends AbstractEndPointTest {

    @Mock
    private TaskService mockTaskService;

    @Mock
    private MemberService mockMemberService;

    @Mock
    private Logger mockLogger;

    private TaskEndPoint taskEndPoint;

    private Task task;

    @Before
    public void prepare() {
        super.setup();

        taskEndPoint = new TaskEndPoint();
        TestUtils.setPrivateAttribute(taskEndPoint, mockTaskService, "taskService");
        TestUtils.setPrivateAttribute(taskEndPoint, mockMemberService, "memberService");
        TestUtils.setPrivateAttribute(taskEndPoint, AbstractEndPoint.class, mockLogger, "LOGGER");
        TestUtils.setPrivateAttribute(taskEndPoint, mockLogger, "LOGGER");
        TestUtils.setPrivateAttribute(taskEndPoint, AbstractEndPoint.class, mockUriInfo, "uriInfo");

        task = EntityFactory.createTask();
        when(mockTaskService.persist(eq(task))).thenReturn(task);

        assertThat(taskEndPoint.getService()).isEqualTo(mockTaskService);


    }


    @Test
    public void getTasks() throws Exception {
        testFindTasks(mockTaskService::findAll, taskEndPoint::getAll);
        verify(mockTaskService, atLeastOnce()).findAll();
    }

    @Test
    public void getTasksWithActivity() throws Exception {
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
    public void get() throws Exception {


        when(mockTaskService.find(any())).thenReturn(task);

        Response response = taskEndPoint.getSingle(randomId);
        checkResponseOK(response);

        TaskWebBean taskReturned = (TaskWebBean) response.getEntity();


        assertThat(taskReturned).isEqualToComparingFieldByField(TaskWebBeanTransformer.transformTask(task));
        verify(mockTaskService).find(eq(randomId));
    }

    @Test
    public void create() throws Exception {
        task.setId(randomId);

        Response response = taskEndPoint.create(task);

        checkResponseCreated(response);
        verify(mockTaskService).persist(eq(task));

    }

    @Test
    public void createWithParamsNull() throws Exception {
        task.setId(randomId);
        createWithParams(null, null);
    }

    @Test
    public void createWithParamsYearNull() throws Exception {
        task.setId(randomId);
        createWithParams(10, null);
    }

    @Test
    public void createWithParamsMonthNull() throws Exception {
        task.setId(randomId);
        createWithParams(null, 2016);
    }

    private void createWithParams(Integer month, Integer year) {
        Response response = taskEndPoint.create(null, month, year, task);
        checkResponseCreated(response);
        verify(mockTaskService).persist(eq(task));
    }


    @Test
    public void createWithParams() throws Exception {
        task.setId(randomId);

        Task taskCreated = EntityFactory.createTask();
        taskCreated.setId(randomId);
        when(mockTaskService.persist(any(DateTime.class), any(Member.class), eq(task))).thenReturn(taskCreated);

        Response response = taskEndPoint.create(randomId, 9, 2015, task);
        checkResponseCreated(response);
    }

    @Test
    public void createWithParamsException() throws Exception {
        task.setId(randomId);

        when(mockTaskService.persist(any(DateTime.class), any(Member.class), eq(task))).thenThrow(new EntityExistsException());

        Response response = taskEndPoint.create(randomId, 9, 2015, task);
        checkResponseBadRequest(response);
    }

    @Test
    public void create_existing() throws Exception {

        task.setId(randomId);
        doThrow(new EntityExistsException()).when(mockTaskService).persist(eq(task));

        Response response = taskEndPoint.create(task);

        checkResponseNotAcceptable(response);
        verify(mockTaskService).persist(eq(task));
        verify(mockLogger).warn(anyString(), isA(EntityExistsException.class));

    }

    @Test
    public void deleteWithParamsNull() throws Exception {
        task.setId(randomId);
        deleteWithParams(null, null);
    }

    @Test
    public void deleteWithParamsYearNull() throws Exception {
        task.setId(randomId);
        deleteWithParams(10, null);
    }

    @Test
    public void deleteWithParamsMonthNull() throws Exception {
        task.setId(randomId);
        deleteWithParams(null, 2016);
    }

    private void deleteWithParams(Integer month, Integer year) {

        Member member = EntityFactory.createMember();
        member.setMemberType(MemberType.ADMINISTRATOR);
        when(mockMemberService.find(randomId)).thenReturn(member);

        Response response = taskEndPoint.delete(randomId, randomId, month, year);
        checkResponseNoContent(response);
        verify(mockTaskService).remove(eq(randomId));
    }

    @Test
    public void deleteWithParamsForbidden() {

        Member member = EntityFactory.createMember();
        member.setMemberType(MemberType.BASIC);
        when(mockMemberService.find(randomId)).thenReturn(member);

        Response response = taskEndPoint.delete(randomId, randomId, null, null);
        checkResponseForbidden(response);
        verify(mockTaskService, never()).remove(eq(randomId));
    }


    @Test
    public void deleteWithParams() throws Exception {
        task.setId(randomId);

        Task taskCreated = EntityFactory.createTask();
        taskCreated.setId(randomId);
        when(mockTaskService.persist(any(DateTime.class), any(Member.class), eq(task))).thenReturn(taskCreated);

        Response response = taskEndPoint.delete(randomId, randomId, 9, 2015);
        checkResponseNoContent(response);
        verify(mockTaskService).remove(eq(randomId), eq(randomId), isA(DateTime.class));

    }

    @Test
    public void updateTask() throws Exception {
        Task task = EntityFactory.createTask();
        assertThat(task.getId()).isNull();

        Response response = taskEndPoint.updateTask(randomId, task);
        checkResponseOK(response);

        assertThat(task.getId()).isEqualTo(randomId);
        verify(mockTaskService).merge(eq(task));
    }

    @Test
    public void delete() throws Exception {
        Response response = taskEndPoint.delete(randomId);
        checkResponseNoContent(response);
        verify(mockTaskService).remove(eq(randomId));
    }

    @Test
    public void deleteNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(mockTaskService).remove(anyLong());
        Response response = taskEndPoint.delete(randomId);
        checkResponseNotFound(response);
        verify(mockTaskService).remove(eq(randomId));
    }
}