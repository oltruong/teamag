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
}
