package com.oltruong.teamag.service;

import com.google.common.collect.Maps;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.WorkRealized;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkRealizedServiceTest extends AbstractServiceTest {


    private List<WorkRealized> workRealizedList;

    private List<BusinessCase> businessCaseList;

    private WorkRealizedService workRealizedService;

    @Mock
    private WorkLoadService mockWorkLoadService;

    @Mock
    private BusinessCaseService mockBusinessCaseService;


    @Before
    public void prepare() {
        super.setup();
        workRealizedList = EntityFactory.createList(EntityFactory::createWorkRealized);
        businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);

        workRealizedService = new WorkRealizedService();
        prepareService(workRealizedService);

        when(mockTypedQuery.getResultList()).thenReturn(workRealizedList);
        when(mockBusinessCaseService.findAll()).thenReturn(businessCaseList);

        TestUtils.setPrivateAttribute(workRealizedService, mockWorkLoadService, "workLoadService");
        TestUtils.setPrivateAttribute(workRealizedService, mockBusinessCaseService, "businessCaseService");
    }

    @Test
    public void findAll() throws Exception {
        List<WorkRealized> workRealizedServiceReturned = workRealizedService.findAll();

        assertThat(workRealizedServiceReturned).isEqualTo(workRealizedList);

        checkCreateTypedQuery("WorkRealized.FIND_ALL");
    }

    @Test
    public void getWorkRealizedbyMember() throws Exception {
        List<WorkRealized> workRealizedServiceReturned = workRealizedService.getWorkRealizedbyMember(idTest);

        assertThat(workRealizedServiceReturned).isEqualTo(workRealizedList);

        checkCreateTypedQuery("WorkRealized.FIND_BY_MEMBER");
        verify(mockTypedQuery).setParameter(eq("fMemberId"), eq(idTest));

    }

    @Test
    public void createOrUpdate() throws Exception {

        Task task = EntityFactory.createTask();
        Task parentTask = EntityFactory.createTask();
        parentTask.setActivity(EntityFactory.createActivity());
        task.setTask(parentTask);
        when(mockEntityManager.find(eq(Task.class), anyLong())).thenReturn(task);

        Map<Long, Member> memberMap = Maps.newHashMap();
        memberMap.put(workRealizedList.get(0).getMemberId(), EntityFactory.createMember());

        TestUtils.setPrivateAttribute(new MemberService(), memberMap, "memberMap");

        workRealizedList.get(0).setId(idTest);

        workRealizedService.createOrUpdate(workRealizedList);

        verify(mockEntityManager).merge(eq(workRealizedList.get(0)));
        verify(mockEntityManager, never()).persist(eq(workRealizedList.get(0)));

        workRealizedList.remove(0);

        workRealizedList.forEach(workRealized -> {
            verify(mockEntityManager).persist(eq(workRealized));
            verify(mockEntityManager, never()).merge(eq(workRealized));
        });

        verify(mockBusinessCaseService).findAll();
        verify(mockWorkLoadService).updateWorkLoadWithRealized(any(), eq(businessCaseList));

    }

    @Test
    public void createOrUpdateNull() throws Exception {
        workRealizedService.createOrUpdate(null);
        verify(mockEntityManager, never()).persist(any());
        verify(mockEntityManager, never()).merge(any());

    }

}