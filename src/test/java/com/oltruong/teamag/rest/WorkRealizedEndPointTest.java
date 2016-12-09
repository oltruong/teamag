package com.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.WorkRealized;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.TaskService;
import com.oltruong.teamag.service.WorkRealizedService;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.WorkRealizedWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkRealizedEndPointTest extends AbstractEndPointTest {

    @Mock
    private WorkRealizedService mockWorkRealizedService;

    @Mock
    private WorkService mockWorkService;

    @Mock
    private TaskService mockTaskService;

    private WorkRealizedEndPoint workRealizedEndPoint;

    @Before
    public void prepare() throws Exception {
        super.setup();

        workRealizedEndPoint = new WorkRealizedEndPoint();

        TestUtils.setPrivateAttribute(workRealizedEndPoint, mockWorkRealizedService, "workRealizedService");
        TestUtils.setPrivateAttribute(workRealizedEndPoint, mockTaskService, "taskService");

        assertThat(workRealizedEndPoint.getService()).isEqualTo(mockWorkRealizedService);

    }


    @Test
    public void getAll() {

        List<WorkRealized> workRealizedList = EntityFactory.createList(EntityFactory::createWorkRealized);


        Task task = EntityFactory.createTask();
        task.setId(randomId);
        when(mockTaskService.find(anyLong())).thenReturn(task);

        when(mockWorkRealizedService.findAll()).thenReturn(workRealizedList);

        Response response = workRealizedEndPoint.getAll();

        checkResponseOK(response);

        List<WorkRealized> workRealizedListReturned = (List<WorkRealized>) response.getEntity();

        assertThat(workRealizedListReturned).isEqualTo(workRealizedList);

        verify(mockWorkRealizedService).findAll();

    }


    @Test
    public void testUpdateWorkRealized() {


        List<WorkRealizedWrapper> workRealizedWrapperList = Lists.newArrayListWithExpectedSize(1);


        List<WorkRealized> workRealizedList = prepareWorkRealizedList();

        List<WorkRealized> workRealizedListReduced = Lists.newArrayList();
        workRealizedListReduced.add(workRealizedList.get(1));
        workRealizedListReduced.add(workRealizedList.get(2));


        WorkRealizedWrapper workRealizedWrapper = new WorkRealizedWrapper(workRealizedEndPoint.transformTask(EntityFactory.createTask()));
        workRealizedWrapper.setWorkRealizedList(workRealizedList);

        workRealizedWrapperList.add(workRealizedWrapper);


        Response response = workRealizedEndPoint.updateWorkRealized(workRealizedWrapperList);

        checkResponseOK(response);
        verify(mockWorkRealizedService).createOrUpdate(eq(workRealizedList));
        assertThat(workRealizedList).containsExactlyElementsOf(workRealizedListReduced);
    }

    private List<WorkRealized> prepareWorkRealizedList() {
        List<WorkRealized> workRealizedList = EntityFactory.createList(EntityFactory::createWorkRealized);
        workRealizedList.get(0).setId(null);
        workRealizedList.get(0).setRealized(0d);
        workRealizedList.get(1).setId(randomId);
        workRealizedList.get(2).setId(randomId);

        for (int i = 3; i < workRealizedList.size(); i++) {
            workRealizedList.get(i).setRealized(0d);
        }
        return workRealizedList;
    }

    @Test
    public void testUpdateWorkRealized_empty() {
        Response response = workRealizedEndPoint.updateWorkRealized(null);
        checkResponseNotAcceptable(response);
        verify(mockWorkRealizedService, never()).createOrUpdate(anyList());
    }

    @Test
    public void getWorkRealized_member() {


        List<WorkRealized> workRealizedList = EntityFactory.createList(EntityFactory::createWorkRealized);
        when(mockWorkRealizedService.getWorkRealizedbyMember(anyLong())).thenReturn(workRealizedList);

        List<Task> tasks = EntityFactory.createList(EntityFactory::createTask);
        tasks.forEach(task -> task.setId(randomId));

        Task task = EntityFactory.createTask();
        task.setId(randomId);
        when(mockTaskService.find(anyLong())).thenReturn(task);

        when(mockTaskService.findTaskWithActivity()).thenReturn(tasks);


        Response response = workRealizedEndPoint.getWorkRealized(randomId);

        checkResponseOK(response);

        List<WorkRealizedWrapper> workRealizedWrapperList = (List<WorkRealizedWrapper>) response.getEntity();


    }

}