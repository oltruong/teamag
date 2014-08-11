package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.model.builder.EntityFactory;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public abstract class AbstractEndPointTest {


    protected Long randomId;

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

    protected void checkResponseNotFound(Response response) {
        checkResponse(response, Response.Status.NOT_FOUND);
    }

    protected void checkResponse(Response response, Response.StatusType expectedStatus) {
        assertThat(response).isNotNull();
        assertThat(response.getStatusInfo()).isEqualTo(expectedStatus);
    }


}
