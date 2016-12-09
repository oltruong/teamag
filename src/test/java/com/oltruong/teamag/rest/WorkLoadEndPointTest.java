package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.WorkLoad;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.BusinessCaseService;
import com.oltruong.teamag.service.WorkLoadService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.WorkLoadContainer;
import com.oltruong.teamag.webbean.WorkLoadMemberContainer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WorkLoadEndPointTest extends AbstractEndPointTest {

    private WorkLoadEndPoint workLoadEndPoint;

    @Mock
    private WorkLoadService mockWorkLoadService;

    @Mock
    private BusinessCaseService mockBusinessCaseService;

    @Before
    public void setup() {
        super.setup();
        workLoadEndPoint = new WorkLoadEndPoint();
        TestUtils.setPrivateAttribute(workLoadEndPoint, mockWorkLoadService, "workLoadService");
        TestUtils.setPrivateAttribute(workLoadEndPoint, mockBusinessCaseService, "businessCaseService");

        assertThat(workLoadEndPoint.getService()).isEqualTo(mockWorkLoadService);

    }

    @Test
    public void updateWorkLoadNull() {
        checkResponseOK(workLoadEndPoint.updateWorkLoad(null));
    }


    @Test
    public void updateWorkLoad() {
        WorkLoadContainer workLoadContainer = new WorkLoadContainer();
        List<WorkLoad> workLoadList = new ArrayList<>();
        workLoadContainer.setWorkLoadList(workLoadList);
        checkResponseOK(workLoadEndPoint.updateWorkLoad(Collections.singletonList(workLoadContainer)));

        verify(mockWorkLoadService).mergeList(eq(workLoadList));

    }


    @Test
    public void getWorkLoad() throws Exception {

        BusinessCase businessCase = EntityFactory.createBusinessCase();
        final List<BusinessCase> businessCaseList = Collections.singletonList(businessCase);
        when(mockBusinessCaseService.findAll()).thenReturn(businessCaseList);
        List<WorkLoad> workLoadList = EntityFactory.createList(EntityFactory::createWorkLoad, 2);
        workLoadList.get(0).setBusinessCase(businessCase);
        when(mockWorkLoadService.findOrCreateAllWorkLoad(businessCaseList)).thenReturn(workLoadList);

        final Response response = workLoadEndPoint.getWorkLoad();

        checkResponseOK(response);

        WorkLoadMemberContainer workLoadMemberContainer = (WorkLoadMemberContainer) response.getEntity();

        assertThat(workLoadMemberContainer).isNotNull();

        assertThat(workLoadMemberContainer.getWorkLoadContainerList()).hasSize(2);

        assertThat(workLoadMemberContainer.getWorkLoadContainerList().get(0).getBusinessCase()).isEqualTo(businessCase);


        verify(mockBusinessCaseService).findAll();
        verify(mockWorkLoadService).findOrCreateAllWorkLoad(businessCaseList);
    }
}
