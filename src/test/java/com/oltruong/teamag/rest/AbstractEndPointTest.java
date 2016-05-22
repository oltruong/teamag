package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractEndPointTest {


    protected Long randomId;

    @Mock
    protected Logger mockLogger;

    @Mock
    protected UriInfo mockUriInfo;

    @Mock
    private UriBuilder mockUriBuilder;


    public void setup() {

        when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        when(mockUriBuilder.path(anyString())).thenReturn(mockUriBuilder);
        when(mockUriBuilder.build()).thenReturn(URI.create(""));

        randomId = EntityFactory.createRandomLong();

    }


    protected void checkResponseOK(Response response) {
        checkResponse(response, Response.Status.OK);
    }

    protected void checkResponseCreated(Response response) {
        checkResponse(response, Response.Status.CREATED);
        verify(mockUriInfo).getAbsolutePathBuilder();
        verify(mockUriBuilder).path(anyString());
        verify(mockUriBuilder).build();
    }

    protected void checkResponseNotAcceptable(Response response) {
        checkResponse(response, Response.Status.NOT_ACCEPTABLE);
    }

    protected void checkResponseForbidden(Response response) {
        checkResponse(response, Response.Status.FORBIDDEN);
    }

    protected void checkResponseBadRequest(Response response) {
        checkResponse(response, Response.Status.BAD_REQUEST);
    }

    protected void checkResponseNoContent(Response response) {
        checkResponse(response, Response.Status.NO_CONTENT);
    }

    protected void checkResponseNotAllowed(Response response) {
        checkResponse(response, Response.Status.METHOD_NOT_ALLOWED);
    }

    protected void checkResponseNotFound(Response response) {
        checkResponse(response, Response.Status.NOT_FOUND);
    }

    protected void checkResponse(Response response, Response.StatusType expectedStatus) {
        assertThat(response).isNotNull();
        assertThat(response.getStatusInfo()).isEqualTo(expectedStatus);
    }


}
