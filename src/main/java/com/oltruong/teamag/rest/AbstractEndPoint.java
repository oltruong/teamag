package com.oltruong.teamag.rest;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Olivier Truong
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public abstract class AbstractEndPoint {

    @Inject
    protected static Logger LOGGER;

    protected Response get(Supplier finder) {
        Object result = finder.get();
        if (result == null) {
            return notFound();
        } else {
            return ok(result);
        }
    }


    public Response create(Supplier supplier) {
        try {
            supplier.get();
        } catch (EntityExistsException e) {
            LOGGER.warn("Tyring to create an already existing entity", e);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return created();
    }

    protected Response delete(Consumer<Long> deleter, Long id) {
        try {
            deleter.accept(id);
            return noContent();
        } catch (EntityNotFoundException e) {
            return notFound();
        }
    }


    protected Response ok(Object object) {
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
