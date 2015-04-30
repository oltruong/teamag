package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.service.BusinessCaseService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Olivier Truong
 */
@Path("business")
@Stateless
@AdminChecked
public class BusinessCaseEndPoint extends AbstractEndPoint {

    @Inject
    BusinessCaseService businessCaseService;

    @GET
    public Response get() {
        return get(() -> businessCaseService.findAll());
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long businessCaseId) {
        return get(() -> businessCaseService.find(businessCaseId));
    }


    @POST
    public Response create(BusinessCase businessCase) {
        return create(() -> businessCaseService.persist(businessCase));
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long businessCaseId, BusinessCase businessCase) {
        businessCase.setId(businessCaseId);
        businessCaseService.merge(businessCase);
        return ok();
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long businessCaseId) {
        return delete(businessCaseService::remove, businessCaseId);
    }
}
