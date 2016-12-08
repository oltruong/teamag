package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Parameter;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.ParameterService;
import com.oltruong.teamag.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParameterEndPointTest extends AbstractEndPointTest {


    @Mock
    private ParameterService mockParameterService;

    private ParameterEndPoint parameterEndPoint;

    @Before
    public void prepare() {
        super.setup();
        parameterEndPoint = new ParameterEndPoint();
        TestUtils.setPrivateAttribute(parameterEndPoint, mockParameterService, "parameterService");
        TestUtils.setPrivateAttribute(parameterEndPoint, AbstractEndPoint.class, mockLogger, "LOGGER");
        TestUtils.setPrivateAttribute(parameterEndPoint, AbstractEndPoint.class, mockUriInfo, "uriInfo");

    }


    @Test
    public void getAll() throws Exception {
        List<Parameter> parameterList = EntityFactory.createList(EntityFactory::createParameter);
        when(mockParameterService.findAll()).thenReturn(parameterList);
        Response response = parameterEndPoint.getAll();

        checkResponseOK(response);

        List<Parameter> parameterListReturned = (List<Parameter>) response.getEntity();

        assertThat(parameterListReturned).isEqualTo(parameterList);
        verify(mockParameterService).findAll();
    }


    @Test
    public void getActivity() throws Exception {

        Parameter parameter = EntityFactory.createParameter();
        when(mockParameterService.find(eq(randomId))).thenReturn(parameter);

        Response response = parameterEndPoint.getSingle(randomId);
        checkResponseOK(response);

        Parameter parameterReturned = (Parameter) response.getEntity();

        assertThat(parameterReturned).isEqualTo(parameter);
        verify(mockParameterService).find(eq(randomId));
    }


    @Test
    public void update() throws Exception {
        List<Parameter> parameterList = EntityFactory.createList(EntityFactory::createParameter);
        final Response response = parameterEndPoint.update(parameterList);

        checkResponseOK(response);

        parameterList.forEach(parameter-> verify(mockParameterService).merge(parameter));
    }


}