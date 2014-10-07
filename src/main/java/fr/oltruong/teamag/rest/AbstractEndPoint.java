package fr.oltruong.teamag.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public abstract class AbstractEndPoint {

    protected Response buildResponseOK(Object object) {
        return Response.ok(object).build();
    }

    protected Response buildResponseOK() {
        return Response.ok().build();
    }

    protected Response buildResponseCreated() {
        return Response.status(Response.Status.CREATED).build();
    }

    protected Response buildResponseNotAcceptable() {
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    protected Response buildResponseNoContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    protected Response buildResponseNotFound() {
        return buildResponse(Response.Status.NOT_FOUND);
    }

    protected Response buildResponseForbidden() {
        return buildResponse(Response.Status.FORBIDDEN);
    }

    private Response buildResponse(Response.Status status) {
        return Response.status(status).build();
    }

}
