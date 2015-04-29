package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.builder.EntityFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public abstract class AbstractEndPointTest {


    protected Long randomId;

    @Mock
    protected Logger mockLogger;

    public void setup() {
        MockitoAnnotations.initMocks(this);

        randomId = EntityFactory.createRandomLong();

    }


    protected void checkResponseOK(Response response) {
        checkResponse(response, Response.Status.OK);
    }

    protected void checkResponseCreated(Response response) {
        checkResponse(response, Response.Status.CREATED);
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

    protected void checkResponseNotFound(Response response) {
        checkResponse(response, Response.Status.NOT_FOUND);
    }

    protected void checkResponse(Response response, Response.StatusType expectedStatus) {
        assertThat(response).isNotNull();
        assertThat(response.getStatusInfo()).isEqualTo(expectedStatus);
    }


}
