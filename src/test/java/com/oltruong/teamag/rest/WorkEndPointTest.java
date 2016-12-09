package com.oltruong.teamag.rest;

import com.google.common.collect.Maps;

import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.TaskWebBean;
import com.oltruong.teamag.webbean.WorkByTaskBean;
import com.oltruong.teamag.webbean.WorkPatch;
import com.oltruong.teamag.webbean.WorkWebBean;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WorkEndPointTest extends AbstractEndPointTest {

    private WorkEndPoint workEndPoint;

    @Mock
    private WorkService mockWorkService;

    @Mock
    private MemberService mockMemberService;

    @Before
    public void setup() {
        super.setup();
        workEndPoint = new WorkEndPoint();
        TestUtils.setPrivateAttribute(workEndPoint, mockWorkService, "workService");
        TestUtils.setPrivateAttribute(workEndPoint, mockMemberService, "memberService");
        assertThat(workEndPoint.getService()).isEqualTo(mockWorkService);
    }


    @Test
    public void getWorksBySearchCriteriaForbidden() throws Exception {
        getWorksBySearchCriteriaForbidden(null);
        getWorksBySearchCriteriaForbidden(randomId);
    }


    private void getWorksBySearchCriteriaForbidden(Long memberId) {
        Member member = EntityFactory.createMember();
        member.setMemberType(MemberType.BASIC);
        when(mockMemberService.find(randomId)).thenReturn(member);
        Response response = workEndPoint.getWorksBySearchCriteria(randomId, randomId, memberId, null, null, null, false);
        checkResponseForbidden(response);
    }

    @Test
    public void getWorksBySearchCriteriaNoFilter() {
        Member member = EntityFactory.createMember();
        member.setMemberType(MemberType.ADMINISTRATOR);
        when(mockMemberService.find(randomId)).thenReturn(member);


        List<Work> workList = EntityFactory.createList(EntityFactory::createWork);

        when(mockWorkService.findWorkByTask(randomId)).thenReturn(workList);

        Response response = workEndPoint.getWorksBySearchCriteria(randomId, randomId, null, null, null, null, false);
        checkResponseOK(response);
        verify(mockWorkService).findWorkByTask(eq(randomId));

        final List<WorkWebBean> workWebBeanList = (List<WorkWebBean>) response.getEntity();

        assertThat(workWebBeanList).hasSameSizeAs(workList);


        for (int index = 0; index < workWebBeanList.size(); index++) {
            Work work = workList.get(index);

            WorkWebBean workWebBean = workWebBeanList.get(index);
            assertThat(workWebBean.getAmount()).isEqualTo(work.getTotal());
            assertThat(workWebBean.getDay()).isEqualTo(work.getDay().toDate());
            assertThat(workWebBean.getMember()).isEqualTo(work.getMember().getName());
            assertThat(workWebBean.getId()).isEqualTo(work.getId());

            TaskWebBean taskWebBean = workWebBean.getTaskBean();

            final Task task = work.getTask();
            assertThat(taskWebBean.getId()).isEqualTo(task.getId());
            assertThat(taskWebBean.getName()).isEqualTo(task.getName());
            assertThat(taskWebBean.getProject()).isEqualTo(task.getProject());

        }

    }


    @Test
    public void getWorksByTaskForbidden() throws Exception {

        Member member = EntityFactory.createMember();
        member.setMemberType(MemberType.BASIC);

        when(mockMemberService.find(anyLong())).thenReturn(member);

        checkResponseForbidden(workEndPoint.getWorksByTask(randomId, EntityFactory.createRandomLong(), null, null));
    }

    @Test
    public void getWorksByTaskNoMemberId() throws Exception {
        testGetWorksByTask(null);
    }

    @Test
    public void getWorksByTaskSameMemberId() throws Exception {

        testGetWorksByTask(randomId);
    }

    private void testGetWorksByTask(Long memberId) {
        Integer randomIntegerMonth = EntityFactory.createRandomMonth();
        Integer randomIntegerYear = EntityFactory.createRandomInteger(2055);
        Map<Task, Double> map = buildMap();
        checkResponseGetWorksByTask(map, () -> mockWorkService.findTaskByMemberMonth(anyLong(), any(DateTime.class)), () -> workEndPoint.getWorksByTask(randomId, memberId, randomIntegerMonth, randomIntegerYear));

        verify(mockWorkService).findTaskByMemberMonth(eq(randomId), eq(new DateTime(randomIntegerYear, randomIntegerMonth, 1, 0, 0)));
    }

    @Test
    public void getWorksByTaskAll() throws Exception {


        Map<Task, Double> map = buildMap();
        checkResponseGetWorksByTask(map, () -> mockWorkService.findTaskByMember(anyLong()), () -> workEndPoint.getWorksByTask(randomId, null, null, null));

        verify(mockWorkService).findTaskByMember(eq(randomId));
    }

    private Map<Task, Double> buildMap() {
        Map<Task, Double> map = Maps.newHashMap();
        final Task task = EntityFactory.createTask();
        final Double total = EntityFactory.createRandomDouble();
        map.put(task, total);
        return map;
    }

    private void checkResponseGetWorksByTask(Map<Task, Double> map, Supplier<Map<Task, Double>> mapSupplier, Supplier<Response> supplier) {

        when(mapSupplier.get()).thenReturn(map);
        Response response = supplier.get();
        checkResponseOK(response);

        List<WorkByTaskBean> listResult = (List<WorkByTaskBean>) response.getEntity();

        assertThat(listResult).hasSize(map.size());
        Task task = map.keySet().iterator().next();
        final WorkByTaskBean workByTaskBean = listResult.get(0);
        final WorkByTaskBean other = new WorkByTaskBean(task.getDescription(), map.get(task));
        assertThat(workByTaskBean.getName()).isEqualTo(other.getName());
        assertThat(workByTaskBean.getTotal()).isEqualTo(other.getTotal());
    }


    @Test
    public void delete() {
        checkResponseNotAllowed(workEndPoint.delete(randomId));
    }

    @Test
    public void getSingle() {
        checkResponseNotAllowed(workEndPoint.getSingle(randomId));
    }

    @Test
    public void create() {
        checkResponseNotAllowed(workEndPoint.create(EntityFactory.createWork()));
    }

    @Test
    public void updateMultipleForbidden() throws Exception {

        Work work = EntityFactory.createWork();
        work.setId(randomId);

        final Response response = updateMultiple(work);
        checkResponseForbidden(response);

        verify(mockWorkService).find(randomId);


    }

    private Response updateMultiple(Work work) {
        when(mockWorkService.find(randomId)).thenReturn(work);

        WorkPatch workPatch = new WorkPatch();
        workPatch.setId(randomId);
        workPatch.setTotal(2d);


        return workEndPoint.updateMultiple(randomId, Collections.singletonList(workPatch));
    }

    @Test
    public void updateMultiple() throws Exception {

        Work work = EntityFactory.createWork();
        work.setId(randomId);

        work.getMember().setId(randomId);

        final Response response = updateMultiple(work);
        checkResponseOK(response);


        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);

        verify(mockWorkService).find(randomId);
        verify(mockWorkService).mergeList(listCaptor.capture());

        final List<Work> listCaptorValue = listCaptor.getValue();

        assertThat(listCaptorValue).hasSize(1);

        assertThat(listCaptorValue.get(0).getTotal()).isEqualTo(2d);

    }
}