package com.oltruong.teamag.rest;

import com.google.common.collect.Maps;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.WorkByTaskBean;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
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
    }

    @Test
    public void testGetWorksByTask() throws Exception {

        Long randomLong = EntityFactory.createRandomLong();
        Integer randomIntegerMonth = EntityFactory.createRandomMonth();
        Integer randomIntegerYear = EntityFactory.createRandomInteger(2055);


        Map<Task, Double> map = Maps.newHashMap();


        final Task task = EntityFactory.createTask();
        final Double total = EntityFactory.createRandomDouble();
        map.put(task, total);

        when(mockWorkService.findTaskByMemberMonth(anyLong(), any(DateTime.class))).thenReturn(map);


        Response response = workEndPoint.getWorksByTask(randomLong, null, randomIntegerMonth, randomIntegerYear);

        checkResponseOK(response);

        List<WorkByTaskBean> listResult = (List<WorkByTaskBean>) response.getEntity();

        assertThat(listResult).hasSize(map.size());
        assertThat(listResult.get(0)).isEqualToComparingFieldByField(new WorkByTaskBean(task.getDescription(), total));

        verify(mockWorkService).findTaskByMemberMonth(eq(randomLong), eq(new DateTime(randomIntegerYear, randomIntegerMonth, 1, 0, 0)));
    }
}