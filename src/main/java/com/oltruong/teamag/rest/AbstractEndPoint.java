package com.oltruong.teamag.rest;

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

    protected Response ok() {
        return Response.ok().build();
    }

    protected Response created() {
        return Response.status(Response.Status.CREATED).build();
    }

    protected Response notAcceptable() {
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    protected Response noContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    protected Response notFound() {
        return buildResponse(Response.Status.NOT_FOUND);
    }

    protected Response forbidden() {
        return buildResponse(Response.Status.FORBIDDEN);
    }

    protected Response badRequest() {
        return buildResponse(Response.Status.BAD_REQUEST);
    }

    private Response buildResponse(Response.Status status) {
        return Response.status(status).build();
    }

}
